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
			if (request.getParameter("Score_Lanes") != null && request.getParameter("Score_Lanes").equals("GetParticipants")) {
				String value = request.getParameter("Lanes");
				if (value != null) 
					lanePick = Integer.parseInt(value.replaceAll("!*[\\D]", ""));
			}
			else if (request.getParameter("Score_PointButton") != null && request.getParameter("Score_PointButton").equals("Ramt")) {
				int pID = Integer.parseInt(request.getParameter("Score_PointID"));
				Participant p = iCtr.getParticipantFromDB(pID);
				iCtr.addHit(p);
			}
			else if (request.getParameter("Score_PointButton") != null && request.getParameter("Score_PointButton").equals("Forbier")) {
				int pID = Integer.parseInt(request.getParameter("Score_PointID"));
				Participant p = iCtr.getParticipantFromDB(pID);
				iCtr.addMiss(p);
			}
			else if (request.getParameter("Score_PointButton") != null && request.getParameter("Score_PointButton").equals("Fortryd")) {
				int pID = Integer.parseInt(request.getParameter("Score_PointID"));
				Participant p = iCtr.getParticipantFromDB(pID);
				iCtr.undoScore(p);
			}
			html = generateHTML(new File(getServletContext().getRealPath("/score.html")), request);
			break;
		case "register":
			Participant p = iCtr.createParticipantToDB(request.getParameter("FirstName"), request.getParameter("LastName"), request.getParameter("AgeGroup"), request.getParameter("Email"));
			iCtr.addParticipant(p);
			html = generateHTML(new File(getServletContext().getRealPath("/register.html")), request);
			break;
		case "createlanes":
			if (request.getParameter("Lanes_Color") != null && request.getParameter("Lanes_Color").equals("addColor")) {
				Integer shirtAmount = stringToInteger(request.getParameter("NumberOfShirts"));
				if (shirtAmount != null)
					iCtr.createColorToDB(request.getParameter("Colors"), shirtAmount, 0);
			}
			else if (request.getParameter("Lanes_Color") != null && request.getParameter("Lanes_Color").equals("generate")) {
				Integer laneAmount = stringToInteger(request.getParameter("NumberOfLanes"));
				if (laneAmount != null)
					iCtr.laneGeneration(laneAmount);
			}
			html = generateHTML(new File(getServletContext().getRealPath("/createLanes.html")), request);
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
			else if (line.startsWith("<!--DELETE.LINE")) {
				s.nextLine();
				continue;
			}
			else if (line.startsWith("<!--DATABASE.GET ")) {
				switch (line.substring("<!--DATABASE.GET ".length(), line.length()-3)) {
				case "ScoreView":
					Lane lane = iCtr.getLaneFromID(lanePick);
					List<Participant> pList;
					if (lane != null)
						pList = lane.getParticipants();
					else 
						pList = iCtr.getParticipants();
					
					for (Participant p : pList) {
						if (lane != null && !p.getAgeRange().equals(lane.getAgeGroup()))
							continue;
						String color = ((p.getShirtColor() != null) ? p.getShirtColor() : "red");
						String hitScore = iCtr.getScoreFromDB(p.getScore().getScoreID()).getHitScore();
						htmlBuild += String.format("<tr>\n"
								+ "<td><font color=\"%1$s\">&#9930;</font> %2$d <font color=\"%1$s\">&#9930;</font></td>\n"
								+ "<td>%3$s</td>\n"
								+ "<td>%4$s</td>\n"
								+ "<td>\n"
								+ "<form action=\"ServletHandler\" method=\"post\">\n"
								+ "<input type=\"hidden\" value=\"" + p.getId() + "\" name=\"Score_PointID\"/>\n"
								+ "<input type=\"submit\" name=\"Score_PointButton\" value=\"Ramt\">\n"
								+ "<input type=\"submit\" name=\"Score_PointButton\" value=\"Forbier\">\n"
								+ "<input type=\"submit\" name=\"Score_PointButton\" value=\"Fortryd\">\n"
								+ "<input type=\"hidden\" value=\"score\" name=\"file\">\n"
								+ "</form>\n"
								+ "</td>\n"
								+ "</tr>\n", color, p.getShirtNumber(), p.getFName() + " " + p.getLName(), hitScore);
					}
					break;
				case "Lanes":
					List<Lane> lList = iCtr.getLanesFromDB();
					for (Lane l : lList)
						if (l.getLaneID() == lanePick)
							htmlBuild += String.format("<option value=\"lane %d\" selected>%s for %s år</option>", l.getLaneID(), "Bane " + l.getLaneNr(), l.getAgeGroup());
						else
							htmlBuild += String.format("<option value=\"lane %d\">%s for %s år</option>", l.getLaneID(), "Bane " + l.getLaneNr(), l.getAgeGroup());
					break;
				case "SearchView":
					String fName = (request.getParameter("Search_FirstName") == null) ? "" : request.getParameter("Search_FirstName");
					String lName = (request.getParameter("Search_LastName") == null) ? "" : request.getParameter("Search_LastName");
					String ageRange = (request.getParameter("Search_AgeGroup").equals("none")) ? "" : request.getParameter("Search_AgeGroup");
					String shirtColor = (request.getParameter("Search_Colors").equals("none")) ? "" : request.getParameter("Search_Colors");
					Integer shirtNumber = stringToInteger(request.getParameter("Search_ShirtNumber"));
					List<Participant> search = iCtr.searchParticipant(fName, lName, ageRange, shirtColor, shirtNumber);
					System.out.println("Searching with criteria: " + fName +", "+lName+", "+ageRange+", "+shirtColor+", "+shirtNumber + " Found: " + search.size());
					for (Participant p : search) {
						String color = ((p.getShirtColor() != null) ? p.getShirtColor() : "red");
						htmlBuild += String.format("<tr>\n"
								+ "<td><font color=\"%1$s\">&#9930;</font> %2$d <font color=\"%1$s\">&#9930;</font></td>\n"
								+ "<td>%3$s</td>\n"
								+ "<td>%4$d</td>\n"
								+ "<td>%5$s</td>\n"
								+ "</tr>\n", color, p.getShirtNumber(), p.getFName() + " " + p.getLName(), p.getScore().getScore(), p.getScore().getHitScore());
					}
					
					//Keeping search values.
					htmlBuild = htmlBuild.replaceFirst("name=\"Search_FirstName\"", "name=\"Search_FirstName\" value=\"" + fName + "\"");
					htmlBuild = htmlBuild.replaceFirst("name=\"Search_LastName\"", "name=\"Search_LastName\" value=\"" + lName + "\"");
					htmlBuild = htmlBuild.replaceFirst("name=\"Search_ShirtNumber\"", "name=\"Search_ShirtNumber\" value=\"" + ((shirtNumber == null) ? "" : shirtNumber) + "\"");
					htmlBuild = htmlBuild.replaceFirst("value=\"" + shirtColor + "\"", "value=\"" + shirtColor + "\" selected");
					htmlBuild = htmlBuild.replaceFirst("value=\"" + ageRange + "\"", "value=\"" + ageRange + "\" selected");
					
					break;
				case "Colors":
					for (EColor eC : EColor.values()) {
						htmlBuild += String.format("<option value=\"%s\">%s</option>",eC.name(),setCapitalLetter(eC.danishValue()));
					}
					break;
				case "ColorChoices":
					List<String> colors = iCtr.getColorsFromDB();
					for (String color : colors) {
						String[] colorData = color.split(",");
						htmlBuild += String.format("<tr>\n"
								+ "<td>%s</td>\n"
								+ "<td>%s</td>\n"
								+ "</tr>\n", setCapitalLetter(EColor.getDanishValue(colorData[0])), colorData[1]);
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
	
	private Integer stringToInteger(String s) {
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	private String setCapitalLetter(String s) {
		return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
	}
}
