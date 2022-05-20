package controller;

import model.application.StatisticsReport;
import model.domain.*;
import util.CountMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "StatisticsServlet", value = "/statistics")
public class ViewStatistics extends ITPortalServlet {

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

        StatisticsReport report = new StatisticsReport();

        Map<Category,Integer> unsolved = new LinkedHashMap<>();
        Map<Category,Integer> lastDays = new LinkedHashMap<>();
        CountMap<Category> unsolvedCategory = report.getUnsolvedEachCategory();
        for(Category category : Category.values()) {
            unsolved.put(category,unsolvedCategory.get(category));
        }
        unsolvedCategory = report.getSolvedEachCategoryLastDays();
        for(Category category : Category.values()) {
            lastDays.put(category,unsolvedCategory.get(category));
        }

        request.setAttribute("report",report);
        request.setAttribute("unsolved",unsolved);
        request.setAttribute("solved",lastDays);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/ViewStats.jsp");
        dispatcher.forward(request,response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
