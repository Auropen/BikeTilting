package gui.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import application.Controller;
import application.IController;
import domain.BikeTilting;
import domain.Lane;
import domain.Participant;
import technical.DBHandler;

/**
 * Servlet implementation class ServletPoint
 */
@WebServlet("/ServletPoint")
public class ServletPoint extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private IController iCtr;
	private int lanePick;


	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ServletPoint() {
		iCtr = Controller.getInstance();
		lanePick = 1;
	}

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.err.println(getServletContext().getRealPath("/technicalProperties.properties"));
        DBHandler.getProperties(new File(getServletContext().getRealPath("/technicalProperties.properties")));
		BikeTilting.getInstance().storeDBToMemory();

		if (request.getParameter("Mani_Lanes") != null && request.getParameter("Mani_Lanes").equals("GetParticipants")) {
			String value = request.getParameter("Lanes");
			if (value != null) {
				lanePick = Integer.parseInt(value.replaceAll("!*[\\D]", ""));
				iCtr.getLanes();
			}
		}
		else if (request.getParameter("Mani_Point") != null && request.getParameter("Mani_Point").equals("GetScoreBtn")) {
			if (request.getParameter("PointButton") != null && request.getParameter("PointButton").equals("Ramt")) {
				int pID = (int) request.getAttribute("id");
				Participant p = iCtr.getParticipantFromDB(pID);
				iCtr.addHit(p);
			}
			else if (request.getParameter("PointButton") != null && request.getParameter("PointButton").equals("Forbier")) {
				int pID = (int) request.getAttribute("id");
				Participant p = iCtr.getParticipantFromDB(pID);
				iCtr.addMiss(p);
			}
			else if (request.getParameter("PointButton") != null && request.getParameter("PointButton").equals("Fortryd")) {
				int pID = (int) request.getAttribute("id");
				Participant p = iCtr.getParticipantFromDB(pID);
				iCtr.addMiss(p);
			}
		}
		String html = generateHTML(new File(getServletContext().getRealPath("/ParticipantScore.html")));
		response.getWriter().append(html);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	private String generateHTML(File html) throws FileNotFoundException {
		boolean insideScript = false;
		String htmlBuild = "";
		
		Scanner s = new Scanner(new FileInputStream(html));
		while (s.hasNext()) {
			String line = s.nextLine().trim();
			if (line.startsWith("<!--DELETE.SCRIPT") || insideScript) {
				insideScript = !line.contains("</script>");
				continue;
			}
			else if (line.startsWith("<!--DATABASE.GET ")) {
				switch (line.substring("<!--DATABASE.GET ".length(), line.length()-3)) {
				case "Participant":
					List<Participant> pList = iCtr.getLaneFromLaneNr(lanePick).getParticipants();
					for (Participant p : pList) {
						String color = ((p.getShirtColor() != null) ? p.getShirtColor().toString() : "red");
						htmlBuild += String.format("<tr>\n"
								+ "<td><font color=\"%s\">&#9930;</font> %d <font color=\"%s\">&#9930;</font></td>\n"
								+ "<td>%s</td>\n"
								+ "<td>%s</td>\n"
								+ "<td>\n"
								+ "<form action=\"ServletPoint\" method=\"post\">\n"
								+ "<input type=\"hidden\" value=\"GetScoreBtn\" name=\"Mani_Point\"/>"
								+ "<input type=\"submit\" value=\"Ramt\" id=\"" + p.getId() + "\">\n"
								+ "<input type=\"submit\" value=\"Forbier\" id=\"" + p.getId() + "\">\n"
								+ "<input type=\"submit\" value=\"Fortryd\" id=\"" + p.getId() + "\">\n"
								+ "</form>\n"
								+ "</td>\n"
								+ "</tr>\n", color, p.getShirtNumber(), color, p.getFName() + " " + p.getLName(), p.getScore().getHitScore());
					}
					break;
				case "Lanes":
					List<Lane> lList = iCtr.getLanesFromDB();
					for (Lane l : lList)
						if (l.getLaneNr() == lanePick)
							htmlBuild += String.format("<option value=\"lane %d\" selected>%s</option>", l.getLaneNr(), "Bane " + l.getLaneNr()) + "\n";
						else
							htmlBuild += String.format("<option value=\"lane %d\">%s</option>", l.getLaneNr(), "Bane " + l.getLaneNr()) + "\n";
					break;
				default:
					break;
				}
			}
			else
				htmlBuild += line + "\n";
		}
		s.close();
		return htmlBuild;
	}
}
