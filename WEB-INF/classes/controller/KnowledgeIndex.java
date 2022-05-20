package controller;

import controller.sorter.RejectionGrouper;
import model.application.ITPortal;
import controller.sorter.SortController;
import controller.sorter.SortOption;
import model.domain.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "KnowledgeIndexServlet", value="/knowledgeBase")
public class KnowledgeIndex extends ITPortalServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(!checkDatabase(request,response))
            return;

        UserBean user = checkLoggedIn(request,response);
        if(user==null)
            return;

        SortController controller = new SortController(ITPortal.getInstance().getIssueManager().getKnowledgeBase());
        if(!manageGroup(controller,request,response))
            return;

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/KnowledgeIndex.jsp");
        request.setAttribute("issues", controller);
        request.setAttribute("enumBean",new EnumBean());
        dispatcher.forward(request,response);
    }

    private boolean manageGroup(SortController controller, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String categoryOption = request.getParameter("category");
            if(categoryOption != null && !categoryOption.equals("")) {
                CategoryDefinition main = CategoryDefinition.valueOf(categoryOption);
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

}
