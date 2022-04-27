package pkg;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/Login")
public class Login extends HttpServlet {
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		session.removeAttribute("ERROR");
		session.removeAttribute("user");
		load(request, response,"/login.jsp");
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession();

		String user = request.getParameter("username");
		String pass = request.getParameter("passwrd");

		UserBean currentuser = new UserBean();
		currentuser.login(user, pass);

		if (currentuser.getStatus() == true ) {
			String currentrole= currentuser.getRole();
			if( currentrole.equals("Student")){
				session.setAttribute("user", user);
				session.removeAttribute("ERROR");
				load(request, response,"/userview.jsp");
			}
			else{
				load(request, response,"/ITStaffview.jsp");
			}
		}
		// else show error message but we need to change it latter
		else {
			session.setAttribute("ERROR", "The Login has failed, the username or password is Incorrect");
			load(request, response,"/login.jsp");
		}

	}
	
	//function to receive jsp page name and load it
	private void load(HttpServletRequest request, HttpServletResponse response, String url) throws ServletException, IOException {
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(url);
		dispatcher.forward(request, response);
	}
}