/*
 * Kim Lauer
 * 605.481 Principles of Enterprise Web Development
 * Due: August 12, 2016
 */
package com.rbevans;

/**
 *Java Bean - used to set and get hike, duration, date, error strings and to 
 * calculate and return rates.  Works with BookingDay and Rates classes
 * to obtain rate and do error checking
 * @author kpl
 */
public class Booking {
    
    //Fields used to set and get data
    private String date = null;
    private String hike = null;
    private int duration = 0;
    private boolean error = false;
    private Rates gRate = null;
    private BookingDay validDate;
    private int year = 2016;
    private int month = 7;
    private int dayOfMonth = 8;
    private double rate = 0;
    private String errorMessage = "";
    
    /**
     * Sets hike value, as well as error checks the duration selected according
     * to which hike.  Sends rate object to setRate if no errors
     * 
     * @param hike setting client chosen hike
     */
    public void setHike(String hike) {
        this.hike = hike;
        if(hike.equals("Gardiner")){
            if (this.duration == 3 || this.duration == 5){
                //Uses Rates Class to obtain rates
                gRate = new Rates(Rates.HIKE.GARDINER);
                setRate(gRate);
            } else{
                errorMessage = "Invalid number of days entered for the "
                        + "Gardiner Hike.";
                error = true;
            }
        } else if(hike.equals("Hellroaring")){
            if (this.duration>= 2 && this.duration<= 4){
                //Uses Rates Class to obtain rates
                gRate = new Rates(Rates.HIKE.HELLROARING);
                setRate(gRate);
            } else{
                errorMessage = "Invalid number of days entered for the "
                        + "Hellroaring Plateau hike.";
                error = true;
            }
        } else{
            //Checks inputted data if The Beaten Path is selected
            if (this.duration== 5 || this.duration== 7){
                //Uses Rates Class to obtain rates
                Rates gRate = new Rates(Rates.HIKE.BEATEN);
                setRate(gRate);
            } else {
                errorMessage = "Invalid number of days entered for the "
                        + "Beaten Path Hike.";
                error = true;
            }
        }
    }    
    
    /**
     * 
     * @return hike value
     */
    public String getHike() {
        return this.hike;
    }
    
    /**
     * 
     * @param duration - sets length of hike
     */
    public void setDuration(int duration){
        this.duration = duration;
    }
    
    /**
     * 
     * @return duration of hike
     */
    public int getDuration(){
        return duration;
    }
    
    /**
     * 
     * @param date - sets and checks that date entered is valid
     */
    public void setDate(String date){
        this.date = date;
   
        String[] dates = date.split("\\D+");
        //parses dates to year, month, dayOfMonth
        //parsing error if user input not a number
        try{
            month = Integer.parseInt(dates[0].trim());
            dayOfMonth = Integer.parseInt(dates[1].trim());
            year = Integer.parseInt(dates[2].trim());
        } catch(NumberFormatException err){
            error = true;
            errorMessage = "Invalid date.  Date entered was not a valid day";
        }
        
    }

    /**
     * 
     * @param gRate - sets and obtains rate
     */
    public void setRate(Rates gRate){
        //confirms a valud date was entered using BHCBookingDay
        validDate = new BookingDay(year, month, dayOfMonth);
        if(validDate.isValidDate() == false){
            error = true;
            errorMessage = "Invalid date.  The date entered was not a valid day";
        }
        gRate.setBeginDate(validDate);
        gRate.setDuration(this.duration);
        if(gRate.isValidDates()==true){    
            rate  = gRate.getCost();
        } else{
        //outputs error if dates given are not in season
            error = true;
            errorMessage = gRate.getDetails();
        }
    }
    
    /**
     * 
     * @return rate
     */
    public double getRate(){
        return this.rate;
    }
    
    /**
     * 
     * @return true if error, false if none
     */
    public Boolean getError(){
        return this.error;
    }
    
    public void setErrorString(String errorMessage){
        error = true;
        this.errorMessage = errorMessage;
    }
    /**
     * 
     * @return string with error message
     */
    public String getErrorString(){
        //return "Testing";
        return this.errorMessage; 
    }
}
