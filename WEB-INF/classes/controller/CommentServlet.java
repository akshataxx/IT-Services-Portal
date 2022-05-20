package controller;

import model.application.ITPortal;
import model.domain.CommentBean;
import model.domain.IssueBean;
import model.domain.UserBean;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "CommentServlet", value="/comment")
public class CommentServlet extends ITPortalServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(!checkDatabase(request,response))
            return;

        UserBean user = checkLoggedIn(request,response);
        if(user==null)
            return;

        RequestParameter issueParam = new RequestParameter(request.getParameter("issue"));
        if(!issueParam.isLong()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"No issue provided");
            return;
        }

        IssueBean issue = ITPortal.getInstance().getIssueManager().getIssueById(issueParam.asLong());
        if(issue==null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Unknown Issue '"+issueParam.asLong()+"'");
            return;
        }

        RequestParameter text = new RequestParameter(request.getParameter("text"));
        if(!text.isString()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Unknown Comment Content");
            return;
        }

        try {
            issue.commentOnIssue(new CommentBean(user,text.asString()));
        } catch (IllegalArgumentException | IllegalStateException e) {
            request.setAttribute("err",e.getMessage());
        }

        response.sendRedirect(request.getContextPath()+"/issue?id="+issue.getUniqueId()+"#comments");
    }
}