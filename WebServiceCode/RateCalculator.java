/*
 * Kim Lauer
 * 605.481 Principles of Enterprise Web Development
 * Due: August 12, 2016
 */

package com.rbevans;

import java.text.NumberFormat;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 * Server side Web Service. 
 * Obtains user entered data from client.  Sends data to Booking, which 
 * works with BookingDay and Rate classes to obtain either error string
 * or cost of hike.
 * All returned error strings begin with "Invalid..." 
 * @author kpl
 */
@WebService(serviceName = "RateCalculator")
public class RateCalculator {

    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "hello")
    public String hello(@WebParam(name = "name") String txt) {
        return "Hello " + txt + " !";
    }

    /**
     * Web service operation.  Accepts hike, duration, startDay, numberOfPeople
     * from client side.  Obtains either cost of hike or error string if 
     * user inputted data incorrect
     */
    @WebMethod(operationName = "operation")
    public String operation(@WebParam(name = "hike") String hike, 
            @WebParam(name = "duration") String duration, 
            @WebParam(name = "startDay") String startDay, 
            @WebParam(name = "numberOfPeople") String numberOfPeople) {
        
        //creates new booking object to check user information and obtain rates
        Booking start = new Booking();
        String response = "";
        int intPeople = 0;
        
        //checks that duration entered was int and if yes, set duration of 
        //hike in Booking
        try{
            int intDuration = Integer.parseInt(duration);
            start.setDuration(intDuration);
        } catch(NumberFormatException excep){
            start.setErrorString("Invalid entry for number of days.  "
                    + "You entered " + duration + " for number of days.");
        }
        
        //sets start day and hike in Booking
        start.setDate(startDay);
        start.setHike(hike);
        
        //parses number of people and checks that data entered was int
        try{
            intPeople = Integer.parseInt(numberOfPeople);
        } catch(NumberFormatException excep){
            start.setErrorString("Invalid entry for number of people.  " 
                    + "Number of people entered " + numberOfPeople 
                    + " Please enter a number between 1 to 10");
        }
        
        //checks if error message and if so sends back error string
        if(start.getError() == true){
            String errorMessage = start.getErrorString();
            response = errorMessage;
        //if information entered was valid, sends back rate    
        } else{
            double rate = intPeople * start.getRate();
            NumberFormat formatCost = NumberFormat.getCurrencyInstance();
            String cost = formatCost.format(rate);
            response = cost;
        }
        //returns either rate or error string
        return response;
    }
}
