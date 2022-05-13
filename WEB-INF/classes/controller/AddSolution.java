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

@WebServlet(name = "AddSolutionServlet", value = "/addSolution")
public class AddSolution extends ITPortalServlet {

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
         *     <form class="form-style-2" name="solutionForm" method="get" action="${pageContext.request.contextPath}/addSolution">
         *         <fieldset>
         *             <legend>New Solution</legend>
         *             <label for="text_solution">Resolution Details <span class="required">*</span></label><br/>
         *             <input type="hidden" name="issue" value="<c:out value="${requestScope.issue.uniqueId}"/>">
         *             <textarea id="text_solution" name="solution" rows="5"></textarea>
         *             <p id="solution" class="err">Lorem Ipsum</p>
         *             <input type="submit" value="Post Solution"/>
         *         </fieldset>
         *     </form>
         */

        RequestParameter issue = new RequestParameter(request.getParameter("issue"));
        if(!issue.isLong()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Bad Issue");
            return;
        }

        RequestParameter resolutionDetails = new RequestParameter(request.getParameter("solution"));
        if(!resolutionDetails.isString()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Bad Resolution Details");
            return;
        }

        IssueBean issueBean = ITPortal.getInstance().getIssueManager().getIssueById(issue.asLong());

        try {
            SolutionBean solutionBean = new SolutionBean(resolutionDetails.asString(),user,issueBean);
            issueBean.solveIssue(solutionBean,"New Solution",user.getFirstName()+" "+user.getSurname()+" has added a solution to your issue #"+issueBean.getUniqueId()+". Please review the solution and accept or reject it");
        } catch (IllegalArgumentException | IllegalStateException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,e.getMessage());
            return;
        }

        response.sendRedirect(request.getContextPath()+"/issue?id="+issueBean.getUniqueId()+"#solutions");
    }
}
