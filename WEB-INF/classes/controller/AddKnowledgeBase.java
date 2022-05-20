package controller;

import model.application.ITPortal;
import model.domain.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "AddKnowledgeServlet", value = "/addKnowledge")
public class AddKnowledgeBase extends ITPortalServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(!checkDatabase(request,response))
            return;

        UserBean user = checkLoggedIn(request,response);
        if(user==null)
            return;

        if(!user.getRole().equals(UserRole.IT_STAFF)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN,"Access Denied");
            return;
        }

        RequestParameter issueString = new RequestParameter(request.getParameter("issue"));
        if(!issueString.isLong()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Bad Issue");
            return;
        }

        IssueBean issue = ITPortal.getInstance().getIssueManager().getIssueById(issueString.asLong());
        if(issue==null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Unknown Issue '"+issueString.asLong()+"'");
            return;
        }

        if(issue.isInKnowledgeBase()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Issue is already in knowledge base");
            return;
        }

        if(!issue.canBeAddedToKnowledgeBase()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Issue cannot be added to knowledge base");
            return;
        }

        try {
            issue.addToKnowledgeBase();
        } catch (IllegalArgumentException | IllegalStateException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,e.getMessage());
            return;
        }

        response.sendRedirect(request.getContextPath()+"/knowledge?id="+issue.getUniqueId());
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
