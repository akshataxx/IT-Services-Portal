package controller;

import model.application.ITPortal;
import model.application.LoginStatus;
import model.domain.UserBean;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "LoginServlet", value = "/login")
public class Login extends ITPortalServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ITPortal portal = ITPortal.getInstance();

        if(!checkDatabase(request,response))
            return;

        RequestParameter username = new RequestParameter(request.getParameter("username"));
        RequestParameter password = new RequestParameter(request.getParameter("password"));

        if(!username.isString()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"No username provided");
            return;
        }

        if(!password.isString()) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"No Password provided");
            return;
        }

        LoginStatus login = portal.getUserManager().login(username.asString(),password.asString());
        if(login.isSuccess()) {
            UserBean user = login.getUserBean();
            request.getSession().setAttribute("user",user);
            response.sendRedirect(request.getContextPath()+"/user");
        } else {
            RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/login.jsp");
            request.setAttribute("login_err","Unknown Username and password combination");
            dispatcher.forward(request,response);
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
