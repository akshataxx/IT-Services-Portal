package controller;

import model.application.ITPortal;
import model.application.storage.ConnectionFactory;
import model.application.storage.ContextConnectionFactory;
import model.application.storage.StorageImplementation;
import model.application.storage.TsqlStorage;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "LoginHomeServlet", value = "")
public class LoginHome extends HttpServlet {

	@Override
	public void init() {
		System.out.println("--- Initialising Database ---");
		ConnectionFactory factory = new ContextConnectionFactory();
		StorageImplementation implementation = new TsqlStorage(factory);
		ITPortal.getInstance().setStorageImplementation(implementation);
		if(ITPortal.getInstance().isInitialised()) {
			System.out.println("--- oO Database Successfully Initialised Oo ---");
		} else {
			System.err.println("Database Initialization failed");
		}
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ITPortal portal = ITPortal.getInstance();
		if(!portal.isInitialised()) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"Fatal Database Error");
			return;
		}

		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/login.jsp");
		dispatcher.forward(request,response);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}

}