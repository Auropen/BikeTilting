package gui.servlet;

import java.io.File;
import java.io.FileInputStream;
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
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.err.println(getServletContext().getRealPath("/technicalProperties.properties"));
        DBHandler.getProperties(new File(getServletContext().getRealPath("/technicalProperties.properties")));
        System.out.println("whaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaat?");
        boolean insideScript = false;
        
        String htmlBuild = "";
        /*for (String s : request.getParameterMap().keySet()) {
            System.out.println("Parameter: " + s + " -> ");
            for (String s2 : request.getParameterValues(s)) {
                System.out.println("    Value: " + s2);
			}
		}*/
        //Checks if
        if (request.getParameter("Mani_Lanes").equals("GetParticipants")) {
            List<Participant> pList = iCtr.getParticipantsFromDB();
            List<Lane> lList = iCtr.getLanesFromDB();
            System.out.println("Lanes size = " + lList.size() + " & Parts size = " + pList.size());
        	Scanner s = new Scanner(new FileInputStream(new File(getServletContext().getRealPath("/ManipulateParticipants.html"))));
            while (s.hasNext()) {
                String line = s.nextLine().trim();
                System.out.println(line);
                if (line.startsWith("<!--DELETE.SCRIPT") || insideScript) {
                	insideScript = !line.contains("</script>");
                	System.out.println(insideScript + " -> " + line);
                	continue;
                }
                else if (line.startsWith("<!--DATABASE.GET ")) {
                	switch (line.substring("<!--DATABASE.GET ".length(), line.length()-3)) {
					case "Participant":
						for (Participant p : pList) {
							htmlBuild += String.format("<tr>"
									+ "<td>%d</td>"
									+ "<td>%s</td>"
									+ "<td>%s</td>"
									+ "<td>"
									+ "<form action=\"ServletPoint\" method=\"post\">"
									+ "<input type=\"submit\" value=\"Hit\" name=\"Mani_PointButton\" id=\"" + p.getId() + "\">"
									+ "<input type=\"submit\" value=\"Miss\" name=\"Mani_PointButton\" id=\"" + p.getId() + "\">"
									+ "<input type=\"submit\" value=\"Undo\" name=\"Mani_PointButton\" id=\"" + p.getId() + "\">"
									+ "</form>"
									+ "</td>"
									+ "</tr>", p.getShirtNumber(), p.getFName() + " " + p.getLName(), p.getScore().getHitScore());
						}
						break;
					case "Lanes":
						for (Lane l : lList)
							htmlBuild += String.format("<option value=\"lane %d\" selected>%s</option>", l.getLaneNr(), "Bane " + l.getLaneNr()) + "\n";
						break;
					default:
						break;
					}
                }
                else
                    htmlBuild += line + "\n";
                	//response.getWriter().append(line);
            }
            s.close();
            System.out.println(htmlBuild);
            response.getWriter().append(htmlBuild);
        }
        
        /*if (request.getParameter("PointButton").equals("Hit")) {
         int pID = (int) request.getAttribute("id");
         Participant p = iCtr.getParticipantFromDB(pID);
         iCtr.addHit(p);
         }
         else {
         int pID = (int) request.getAttribute("id");
         Participant p = iCtr.getParticipantFromDB(pID);
         iCtr.addMiss(p);
         }*/
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
