package controller;

import model.domain.EnumBean;
import model.domain.UserBean;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "UserView", value = "/user")
public class UserView extends ITPortalServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(!checkDatabase(request,response))
            return;

        UserBean user = checkLoggedIn(request,response);
        if(user==null)
            return;

        RequestDispatcher dispatcher = null;

        switch (user.getRole()) {
            case USER:
                dispatcher = request.getRequestDispatcher("/WEB-INF/UserView.jsp");
                break;
            case IT_STAFF:
                dispatcher = request.getRequestDispatcher("/WEB-INF/ITStaffView.jsp");
                break;
            default:
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"Unknown User Role");
                return;
        }
        request.setAttribute("enumBean",new EnumBean());
        dispatcher.forward(request,response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
