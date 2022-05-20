package controller;

import model.application.ITPortal;
import model.application.storage.ConnectionFactory;
import model.application.storage.ContextConnectionFactory;
import model.application.storage.StorageImplementation;
import model.application.storage.TsqlStorage;
import model.domain.UserBean;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class ITPortalServlet extends HttpServlet {

    @Override
    public void init() {
        ITPortal portal = ITPortal.getInstance();
        if(portal.isInitialised() || portal.isFailedInitialisation())
            return;

        ConnectionFactory factory = new ContextConnectionFactory();
        StorageImplementation implementation = new TsqlStorage(factory);
        portal.setStorageImplementation(implementation);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doGet(request,response);
    }

    public UserBean checkLoggedIn(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Object userObj = request.getSession().getAttribute("user");
        if(!(userObj instanceof UserBean)) {
            response.sendRedirect(request.getContextPath());
            return null;
        }
        return (UserBean) userObj;
    }

    public boolean checkDatabase(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ITPortal portal = ITPortal.getInstance();
        if(!portal.isInitialised()) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"Fatal Database Error");
            return false;
        }
        return true;
    }

    @Override
    public void destroy() {
        if(!ITPortal.getInstance().isShutdown()) {
            ITPortal.getInstance().shutdown();
        }
    }
}
