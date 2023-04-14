/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projectlibrary;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author DELL-PC
 */
public class NewClass {
    private Date issue_Date;
    public void subtractDate(Date retdate){
        Date date=new Date();
        System.out.println("The return Date is "+date.compareTo(retdate));
        
    }
    public void calculateFine(){
        //Adding one Day to the current date
       LocalDate date =  LocalDate.now().plusDays(1);
       System.out.println("Adding one day to current date: "+date);
	  
	//Adding number of Days to the current date
	LocalDate date2 =  LocalDate.now().plusDays(7);
	System.out.println("Adding days to the current date: "+date2);
	  
	//Adding one Day to the given date
	LocalDate date3 = LocalDate.of(2016, 10, 14).plusDays(1);
	System.out.println("Adding one day to the given date: "+date3);
	  
	//Adding number of Days to the given date
	LocalDate date4 = LocalDate.of(2016, 10, 14).plusDays(9);
	System.out.println("Adding days to the given date: "+date4);
        LocalDate date5=LocalDate.parse("2018-08-15").minusDays(15);
        System.out.println("Parsing date "+date5);
    }
    public static void main(String args[]){
        NewClass nc=new NewClass();
        nc.calculateFine();
        
    }
    
}
