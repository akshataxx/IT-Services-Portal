package controller;

import model.application.ITPortal;
import controller.sorter.CommentSorter;
import controller.sorter.SolutionSorter;
import controller.sorter.SortOption;
import model.domain.EnumBean;
import model.domain.IssueBean;
import model.domain.UserBean;
import model.domain.UserRole;
import controller.sorter.RejectionGrouper;
import controller.sorter.SortController;
import model.domain.*;


import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "KBViewServlet", value="/KBView")
public class KBView extends ITPortalServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(!checkDatabase(request,response))
            return;

        UserBean user = checkLoggedIn(request,response);
        if(user==null)
            return;

        SortController controller = new SortController(ITPortal.getInstance().getIssueManager().getKnowledgeBase());
        if(request.getParameter("sort-type")!=null) {
            if(!manageSort(controller,request,response))
                return;
        }

        if(!manageGroup(controller,request,response))
            return;

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/KBView.jsp");
        request.setAttribute("issues", controller);
        request.setAttribute("enumBean",new EnumBean());
        dispatcher.forward(request,response);
    }
        private boolean manageGroup(SortController controller, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            if(request.getParameter("group-rejected")!=null) {
                controller.groupRejected(RejectionGrouper.GroupOption.valueOf(request.getParameter("rejected-option")));
            }
            if(request.getParameter("group-state")!=null) {
                controller.groupState(IssueState.valueOf(request.getParameter("state-option")));
            }
            if(request.getParameter("group-category")!=null) {
                CategoryDefinition main = CategoryDefinition.valueOf(request.getParameter("category"));
                String sub = request.getParameter("subCategory");
                if(sub==null || sub.equals("")) {
                    controller.groupCategory(new Category(main));
                } else {
                    controller.groupCategory(new Category(main,CategoryDefinition.valueOf(sub)));
                }
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,e.getMessage());
            return false;
        }
        return true;
    }

    private boolean manageSort(SortController controller, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String sortType = request.getParameter("sort-type");
        try {
            switch (sortType) {
                case "alphabetical":
                    controller.sortAlphabetical(SortOption.valueOf(request.getParameter("alphabetical-option")));
                    break;
                case "reportDate":
                    controller.sortReportDate(SortOption.valueOf(request.getParameter("date-option")));
                    break;
                case "none":
                    controller.sortBy(null);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown Sort Type");
                    return false;
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,e.getMessage());
            return false;
        }
        return true;
    }
}
