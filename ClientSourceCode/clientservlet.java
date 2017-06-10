/*
 * Kim Lauer
 * 605.481 Principles of Enterprise Web Development
 * Due: August 12, 2016
 */

package com.rbevans;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.WebServiceRef;

/**
 * This gets information entered on website and sends it to the 
 * BHCWebService's RateCalculator.  Once result is obtained (either
 * error string or cost of the hike), it creates either an 
 * error page or successful booking page
 * 
 * @author kpl
 */
@WebServlet(name = "clientservlet", urlPatterns = {"/clientservlet"})
public class clientservlet extends HttpServlet {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/BHCWebService/RateCalculator.wsdl")
    private RateCalculator_Service service;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        //data entered by user from index.jsp page
        String hike = request.getParameter("hike");
        String date = request.getParameter("date");
        String duration = request.getParameter("duration");
        String numberOfPeople = request.getParameter("people");
        //will become webpage output string
        String output = "";
        
        //information sent to Web Service (BHCWebService)
        String result = operation(hike, duration, date, numberOfPeople);
        
        //All Web Service errors begin with "Invalid" 
        if(result.matches("Invalid(.*)")){
            output = errorString(result);
        }else {
            output = toString(hike, numberOfPeople, duration, date, result);
        }
        
        //output result
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {             
            out.println(output);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
/**
 * 
 * @param hike - hike selected by user
 * @param duration - length of hike selected by user
 * @param startDay - user entered start day for hike
 * @param numberOfPeople - number of people on hike
 * @return either cost of hike or error string
 */
    private String operation(java.lang.String hike, java.lang.String duration, java.lang.String startDay, java.lang.String numberOfPeople) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        com.rbevans.RateCalculator port = service.getRateCalculatorPort();
        return port.operation(hike, duration, startDay, numberOfPeople);
    }
/**
 * Web page outputted with cost of hike if valid information obtained from user 
 * 
 * @param hike - hike selected by user
 * @param people - number of people on hike selected by user
 * @param duration - length of hike selected by user
 * @param date - start day of hike selected by user
 * @param cost - cost of hike as determined by Web Service
 * @return - Web page with result to be outputted 
 */    
     public String toString(String hike, String people, String duration, 
             String date, String cost){
        
        String htmlBegin = "<!DOCTYPE html> <html> <head>";
        String htmlTitle = ("<title> Beartooth Hiking </title></head> <body>");
        String htmlHeader1 = ("<h1>Congratulations on Booking at Beartooth "
                + "Hiking </h1>");
        String htmlHeader2 = ("<h2>Please confirm your booking arrangements "
                + "below: </h2>");
        String htmlHike = ("Hike Selected: " + hike + "<br />");
        String htmlPeople = ("Number of People in Group: " + people + "<br />");
        String htmlDuration = ("Duration of Hike: " + duration + "<br />");
        String htmlDate = ("Starting on " + date + "<br />");
        String htmlCost = ("The total cost for this hike is " + cost + "<br />");
        String htmlReturn = 
                ("<a href='http://localhost:8080/BHC_Client/'>Click here to select another hike</a>");
        String htmlEnd = ("</body> </html>");
        
        String htmlResponse = htmlBegin + htmlTitle + htmlHeader1 + 
                htmlHeader2 + htmlHike + htmlPeople + htmlDuration + htmlDate + 
                htmlCost + htmlReturn + htmlEnd;
        
        return htmlResponse;
    }
    
    /**
     * Web page outputted if their was an error in user selected information
     * 
     * @param nameOfError - error message based on what type of error
     * @return error String to be outputted to user
     */
    public String errorString(String nameOfError){
        String htmlBegin = "<!DOCTYPE html> <html> <head>";
        String htmlCSS = "<link rel=\"stylesheet\" href=\"css/bhc_error.css\" type=\"text/css\" media=\"print, projection, screen\" />";
        String htmlTitle = ("<title> Beartooth Hiking Error Page </title></head> <body>");
        String htmlHeader = "<h1> Oops!! <br /> We didn't quite get that <br /> <br /> </h1>";
        String htmlError = ("<h2>" + nameOfError + "<br />");
        String htmlReturn = 
                ("<a href='http://localhost:8080/BHC_Client/'>Click here to re-enter information</a>");
        String htmlEnd = ("</h2></body> </html>");
        String response = htmlBegin + htmlCSS + htmlTitle + htmlHeader + htmlError + htmlReturn + 
                htmlEnd;
        
        return response;
    }

}
