package controller;

import model.application.ITPortal;
import controller.sorter.CommentSorter;
import controller.sorter.SolutionSorter;
import controller.sorter.SortOption;
import model.domain.EnumBean;
import model.domain.IssueBean;
import model.domain.UserBean;
import model.domain.UserRole;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "IssueViewServlet", value="/issue")
public class IssueView extends ITPortalServlet {

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

        if(user.getRole().equals(UserRole.USER)) {
            if(!issue.getReporter().getUniqueId().equals(user.getUniqueId())) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Access Denied, You may not view this issue");
                return;
            }
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/IssueView.jsp");
        request.setAttribute("issue",issue);
        request.setAttribute("sorter",new SolutionSorter(SortOption.DESCENDING,issue.getSolutions()));
        request.setAttribute("enumBean",new EnumBean());
        request.setAttribute("commentSorter",new CommentSorter(SortOption.DESCENDING,issue.getComments()));
        dispatcher.forward(request,response);
    }
}
