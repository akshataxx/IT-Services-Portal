package controller;

import model.application.ITPortal;
import model.domain.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ReportServlet", value = "/report")
public class NewReport extends ITPortalServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(!checkDatabase(request,response))
            return;

        UserBean user = checkLoggedIn(request,response);
        if(user==null)
            return;

        if(!user.getRole().equals(UserRole.USER)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Only users may report issues");
            return;
        }

        RequestParameter title = new RequestParameter(request.getParameter("title"));
        RequestParameter description = new RequestParameter(request.getParameter("description"));
        RequestParameter category = new RequestParameter(request.getParameter("category"));
        RequestParameter subCategory = new RequestParameter(request.getParameter("subCategory"));

        if(!title.isString()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Title not found");
            return;
        }

        if(!description.isString()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Description not found");
            return;
        }

        if(!category.isString()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Category not found!");
            return;
        }

        if(!subCategory.isString()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"subCategory not found!");
            return;
        }

        try {
            CategoryDefinition main = CategoryDefinition.valueOf(category.asString());
            CategoryDefinition sub = CategoryDefinition.valueOf(subCategory.asString());
            IssueBean newReport = new IssueBean(title.asString(), description.asString(), user, new Category(main,sub));
            ITPortal.getInstance().getIssueManager().reportIssue(newReport);
            response.sendRedirect(request.getContextPath()+"/user");
        } catch (IllegalArgumentException | IllegalStateException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,e.getMessage());
            return;
        }
    }

}
