package pkg;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet("/UnResolved")
public class UnResolved extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private Report unResolvedRept;
	
	public UnResolved(){
		this.unResolvedRept = new Report();
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=ISO-8859-1");
		String category = (String)request.getParameter("category");
		request.setAttribute("IssueRecSet", this.unResolvedRept.getUnResolved(category)); 
		load(request, response,"/unresolvedreport.jsp");
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