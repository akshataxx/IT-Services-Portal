package controller;

import controller.sorter.NotificationSorter;
import controller.sorter.SortOption;
import model.application.ITPortal;
import model.domain.IssueBean;
import model.domain.SolutionBean;
import model.domain.UserBean;
import model.domain.UserRole;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "NotificationServlet", value = "/notifications")
public class NotificationView extends ITPortalServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(!checkDatabase(request,response))
            return;

        UserBean user = checkLoggedIn(request,response);
        if(user==null)
            return;

        user.readNotifications();

        NotificationSorter sorter = new NotificationSorter(SortOption.DESCENDING,user.getNotifications());
        request.setAttribute("sorted",sorter);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/NotificationView.jsp");
        dispatcher.forward(request,response);
    }
}
