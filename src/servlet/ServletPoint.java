package servlet;

import java.io.File;
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
 * Servlet implementation class ServletPoint
 */
@WebServlet("/ServletPoint")
public class ServletPoint extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private IController iCtr;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletPoint() {
        iCtr = Controller.getInstance();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	File test = new File(getServletContext().getContextPath() + "/technicalProperties.properties");
    	
    	System.out.println("Is there a file there: " + test.exists());
    	System.out.println("THE PATH: " + test.getAbsolutePath());
		System.out.println(getServletContext().getContextPath());
		
		if (request.getParameter("PointButton").equals("Hit")) {
			int pID = (int) request.getAttribute("id");
			Participant p = iCtr.getParticipantFromDB(pID);
			iCtr.addHit(p);
		}
		else {
			int pID = (int) request.getAttribute("id");
			Participant p = iCtr.getParticipantFromDB(pID);
			iCtr.addMiss(p);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
