package controller;

import model.application.ITPortal;
import model.domain.IssueBean;
import model.domain.SolutionBean;
import model.domain.UserBean;
import model.domain.UserRole;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "SetSolutionServlet", value = "/solution")
public class SolutionServlet extends ITPortalServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(!checkDatabase(request,response))
            return;

        UserBean user = checkLoggedIn(request,response);
        if(user==null)
            return;

        if(!user.getRole().equals(UserRole.USER)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Access Denied");
            return;
        }

        /**
         *                     <form name="solutionSubmit" action="${pageContext.request.contextPath}/solution" method="get">
         *                         <input type="hidden" name="id" value="<c:out value="${solution.uniqueId}"/>">
         *                         <input type="hidden" name="issue" value="<c:out value="${requestScope.issue.uniqueId}"/>">
         *                         <input type="submit" name="value" value="accept"> <input type="submit" name="value" value="reject">
         *                     </form>
         */

        RequestParameter issueString = new RequestParameter(request.getParameter("issue"));
        if(!issueString.isLong()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Bad Issue Id");
            return;
        }

        RequestParameter solutionString = new RequestParameter(request.getParameter("id"));
        if(!solutionString.isUniqueId()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Bad Solution");
            return;
        }

        IssueBean issue = ITPortal.getInstance().getIssueManager().getIssueById(issueString.asLong());
        if(issue==null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown issue '" + issueString.asLong() + "'");
            return;
        }

        SolutionBean solution = issue.getSolution(solutionString.asUniqueId());
        if(solution==null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Unknown Solution '"+solutionString.asUniqueId()+"'");
            return;
        }

        if(!issue.getReporter().getUniqueId().equals(user.getUniqueId())) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Access Denied");
            return;
        }

        try {
            String acceptance = request.getParameter("value");
            if(acceptance==null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Unknown solution acceptance");
                return;
            }
            switch (acceptance) {
                case "accept":
                    solution.acceptSolution();
                    break;
                case "reject":
                    solution.rejectSolution("Solution Rejected","Your solution for Issue #"+issue.getUniqueId()+" has been rejected. Please review it");
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown acceptance");
                    return;
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,e.getMessage());
            return;
        }

        response.sendRedirect(request.getContextPath()+"/issue?id="+issue.getUniqueId()+"#solutions");
    }
}
