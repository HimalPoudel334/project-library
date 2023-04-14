
package UtiltiyClasses;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TatkalKoLagi {
    private Date issue_Date;
    public String returnDate(Date date){/**Returns the date String*/
        String strDate;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        strDate= formatter.format(date);
        return strDate;
    }
    public String returnDate(Date issueDate,int retrundate){
        String strDate;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        strDate= formatter.format(issueDate);
        System.out.println(strDate);
        return strDate;
    }
    public void calculateFine(Date issueDate,int renewAfter){
        issue_Date=issueDate;
        System.out.println("Date difference is "+issue_Date);
        Calendar c=Calendar.getInstance();
        c.setTime(issueDate);
        c.add(Calendar.DAY_OF_MONTH,renewAfter);
        //Date fi=calendar-issue_Date;
        
        
    }
    
}
