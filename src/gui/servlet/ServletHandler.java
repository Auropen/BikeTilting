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
import domain.Lane;
import domain.Participant;
import technical.DBHandler;

/**
 * Servlet implementation class ServletPoint
 */
@WebServlet("/ServletHandler")
public class ServletHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private IController iCtr;
	private int lanePick;


	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ServletHandler() {
		iCtr = Controller.getInstance();
		lanePick = 1;
	}

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DBHandler.getProperties(new File(getServletContext().getRealPath("/technicalProperties.properties")));
		iCtr.storeDBToMemory();
		String html = "";

		switch(request.getParameter("file")) {
		case "view":
			html = generateHTML(new File(getServletContext().getRealPath("/view.html")), request);
			break;
		case "score":
			if (request.getParameter("Point_Lanes") != null && request.getParameter("Point_Lanes").equals("GetParticipants")) {
				String value = request.getParameter("Lanes");
				if (value != null) 
					lanePick = Integer.parseInt(value.replaceAll("!*[\\D]", ""));
			}
			else if (request.getParameter("Point_PointButton") != null && request.getParameter("Point_PointButton").equals("Ramt")) {
				int pID = Integer.parseInt(request.getParameter("Point_PointID"));
				Participant p = iCtr.getParticipantFromDB(pID);
				iCtr.addHit(p);
			}
			else if (request.getParameter("Point_PointButton") != null && request.getParameter("Point_PointButton").equals("Forbier")) {
				int pID = Integer.parseInt(request.getParameter("Point_PointID"));
				Participant p = iCtr.getParticipantFromDB(pID);
				iCtr.addMiss(p);
			}
			else if (request.getParameter("Point_PointButton") != null && request.getParameter("Point_PointButton").equals("Fortryd")) {
				int pID = Integer.parseInt(request.getParameter("Point_PointID"));
				Participant p = iCtr.getParticipantFromDB(pID);
				//iCtr.addMiss(p);
			}
			html = generateHTML(new File(getServletContext().getRealPath("/score.html")), request);
			break;
		case "register":
			html = generateHTML(new File(getServletContext().getRealPath("/register.html")), request);
			break;
		default:
			
		}
		
		response.getWriter().append(html);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	private String generateHTML(File html, HttpServletRequest request) throws FileNotFoundException {
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
				case "ScoreView":
					List<Participant> pList = iCtr.getLaneFromLaneNr(lanePick).getParticipants();
					for (Participant p : pList) {
						String color = ((p.getShirtColor() != null) ? p.getShirtColor() : "red");
						htmlBuild += String.format("<tr>\n"
								+ "<td><font color=\"%1$s\">&#9930;</font> %2$d <font color=\"%1$s\">&#9930;</font></td>\n"
								+ "<td>%3$s</td>\n"
								+ "<td>%4$d</td>\n"
								+ "<td>%5$s</td>\n"
								+ "</tr>\n", color, p.getShirtNumber(), p.getFName() + " " + p.getLName(), p.getScore().getScore(), p.getScore().getHitScore());
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
				case "SearchView":
					String fName = request.getParameter("FirstName");
					String lName = request.getParameter("LastName");
					String ageRange = request.getParameter("AgeGroup");
					String shirtColor = request.getParameter("Colors");
					Integer shirtNumber = Integer.parseInt(request.getParameter("ShirtNumber"));
					List<Participant> search = iCtr.searchParticipant(fName, lName, ageRange, shirtColor, shirtNumber);
					
					for (Participant p : search) {
						String color = ((p.getShirtColor() != null) ? p.getShirtColor() : "red");
						htmlBuild += String.format("<tr>\n"
								+ "<td><font color=\"%s\">&#9930;</font> %d <font color=\"%s\">&#9930;</font></td>\n"
								+ "<td>%s</td>\n"
								+ "<td>%s</td>\n"
								+ "<td>\n"
								+ "<form action=\"ServletHandler\" method=\"post\">\n"
								+ "<input type=\"hidden\" value=\"GetScoreBtn\" name=\"Mani_Point\"/>\n"
								+ "<input type=\"hidden\" value=\"" + p.getId() + "\" name=\"Point_PointID\"/>\n"
								+ "<input type=\"submit\" name=\"Point_PointButton\" value=\"Ramt\">\n"
								+ "<input type=\"submit\" name=\"Point_PointButton\" value=\"Forbier\">\n"
								+ "<input type=\"submit\" name=\"Point_PointButton\" value=\"Fortryd\">\n"
								+ "<input type=\"hidden\" value=\"score\" name=\"file\">\n"
								+ "</form>\n"
								+ "</td>\n"
								+ "</tr>\n", color, p.getShirtNumber(), color, p.getFName() + " " + p.getLName(), p.getScore().getHitScore());
					}
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
