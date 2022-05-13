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

@WebServlet(name = "UpdateIssueServlet", value = "/updateIssue")
public class UpdateIssue extends ITPortalServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(!checkDatabase(request,response))
            return;

        UserBean user = checkLoggedIn(request,response);
        if(user==null)
            return;

        if(!user.getRole().equals(UserRole.IT_STAFF)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Access Denied");
            return;
        }

        /**
         <c:if test="${ user.role.name() == 'IT_STAFF'}">
         <c:if test="${requestScope.issue.state.name() ne 'COMPLETED' && requestScope.issue.state.name() ne 'RESOLVED'}">
         <form name="solutionSubmit" action="${pageContext.request.contextPath}/updateIssue" method="get">
         <input type="hidden" name="issue" value="<c:out value="${requestScope.issue.uniqueId}"/>">
         <input type="submit" name="value" value="Set In Progress"> <input type="submit" name="value" value="Wait for Third Party"> <input type="submit" name="value" value="Wait for Reporter">
         </form>
         </c:if>
         </c:if>
         */

        RequestParameter issue = new RequestParameter(request.getParameter("issue"));
        if(!issue.isLong()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Bad Issue");
            return;
        }

        String update = request.getParameter("value");
        if(update==null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Unknown update");
            return;
        }

        IssueBean issueBean = ITPortal.getInstance().getIssueManager().getIssueById(issue.asLong());
        if(issueBean==null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Unknown issue '"+issue.asLong()+"'");
        }

        try {
            switch (update) {
                case "Set In Progress":
                    issueBean.setInProgress();
                    break;
                case "Wait for Third Party":
                    issueBean.waitOnThirdParty();
                    break;
                case "Wait for Reporter":
                    issueBean.waitOnReporter("Waiting on you","Your issue #"+issueBean.getUniqueId()+"' is currently waiting on your for furthur comment or input");
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Unknown Update");
                    return;
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,e.getMessage());
            return;
        }

        response.sendRedirect(request.getContextPath()+"/issue?id="+issueBean.getUniqueId());
    }
}
