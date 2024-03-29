package pkg;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet("/Resolved")
public class Resolved extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private Report resolvedRept;
	
	public Resolved(){
		this.resolvedRept = new Report();
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("IssueRecSet", this.resolvedRept.getResolved()); 
		System.out.println(" From Resolved.java" + this.resolvedRept.getResolved()); //DEBUG CODE
		load(request, response,"/resolvedreport.jsp");
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