package pkg;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet("/StressRate")
public class StressRate extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private Report stressRateRept;
	
	public StressRate(){
		this.stressRateRept = new Report();
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("StressRate", this.stressRateRept.getStressRate()); 
		System.out.println(" From StressRate.java" + this.stressRateRept.getStressRate()); //DEBUG CODE
		load(request, response,"/stressratereport.jsp");
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	//function to receive jsp page name and load it
	private void load(HttpServletRequest request, HttpServletResponse response, String url) throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher(url);
		dispatcher.forward(request, response);
	}
}