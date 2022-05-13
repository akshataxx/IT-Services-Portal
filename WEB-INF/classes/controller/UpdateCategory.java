package controller;

import model.application.ITPortal;
import model.application.StatisticsReport;
import model.domain.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "UpdateCategoryServlet", value = "/updateCategory")
public class UpdateCategory extends ITPortalServlet {

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

        /**
         *         <form name="categorySubmit" action="${pageContext.request.contextPath}/updateCategory" method="get">
         *             <label for="category">Update Category</label>
         *             <select name="category" id="category">
         *                 <c:forEach var="category" items="${requestScope.enumBean.categoryValues}">
         *                     <option value="${category.name}"><c:out value="${category.displayName}"/></option>
         *                 </c:forEach>
         *             </select>
         *             <label for="subCategory">Sub-Category</label>
         *             <select name="subCategory" id="subCategory">
         *             </select>
         *             <script>
         *                 addDynamicSelect("category", "subCategory", collections);
         *             </script>
         *             <input type="submit" value="Update Category">
         *         </form>
         */

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

        String category = request.getParameter("category");
        String subCategory = request.getParameter("subCategory");

        try {
            Category cat = new Category(CategoryDefinition.valueOf(category),CategoryDefinition.valueOf(subCategory));
            issue.setCategory(cat);
        } catch (IllegalArgumentException | IllegalStateException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,e.getMessage());
            return;
        }

        response.sendRedirect(request.getContextPath()+"/issue?id="+issue.getUniqueId());
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
