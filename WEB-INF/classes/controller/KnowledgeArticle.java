package controller;

import model.application.ITPortal;
import controller.sorter.CommentSorter;
import controller.sorter.SolutionSorter;
import controller.sorter.SortOption;
import model.domain.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "KnowledgeViewServlet", value="/knowledge")
public class KnowledgeArticle extends ITPortalServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(!checkDatabase(request,response))
            return;

        UserBean user = checkLoggedIn(request,response);
        if(user==null)
            return;

        RequestParameter issueId = new RequestParameter(request.getParameter("id"));
        if(!issueId.isLong()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Bad Issue Id");
            return;
        }

        IssueBean issue = ITPortal.getInstance().getIssueManager().getIssueById(issueId.asLong());
        if(issue==null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Unknown issue '"+issueId.asLong()+"'");
            return;
        }

        if(!issue.isInKnowledgeBase()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Attempting to view an issue that is not in the knowledge base!");
            return;
        }

        SolutionBean sol = null;
        //first find the accepted solution
        for(SolutionBean solution : issue.getSolutions()) {
            if(solution.getState().equals(SolutionState.ACCEPTED)) {
                sol = solution;
                break;
            }
        }
        //if it does not exist (issue is waiting) then get the waiting solution
        if(sol==null) {
            for (SolutionBean solution : issue.getSolutions()) {
                if (solution.getState().equals(SolutionState.WAITING)) {
                    sol = solution;
                    break;
                }
            }
        }

        if(sol==null) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"Issue in knowledge base without solution!");
            return;
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/KnowledgeView.jsp");
        request.setAttribute("issue",issue);
        request.setAttribute("solution",sol);
        request.setAttribute("enumBean",new EnumBean());
        request.setAttribute("commentSorter",new CommentSorter(SortOption.DESCENDING,issue.getComments()));
        dispatcher.forward(request,response);
    }
}
