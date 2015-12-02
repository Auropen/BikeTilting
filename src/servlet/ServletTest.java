package servlet;


import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import application.Controller;
import application.IController;
import domain.Participant;

/**
 * Servlet implementation class ServletTest
 */
@WebServlet("/ServletTest")
public class ServletTest extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private IController iCtr;
	
	
	public ServletTest() {
		iCtr = Controller.getInstance();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Participant p = new Participant(request.getParameter("FirstName"), request.getParameter("LastName"), request.getParameter("AgeGroup"), request.getParameter("Email"), null, null, null);
		response.getWriter().append("Servlet was loadet, with " + request.getParameterMap().size() + " parameters.\n "+ request.getParameter("AgeGroup"));
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
