package businesslogic.kitchenTask;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Shift {
    private int id;
    private int start;
    private int end;
    private Date day;
    //private Boolean available;
    //private Cook cook;
    //private Task task;
    public Shift(){

    }
    public Shift(int id,int start,int end,Date day){
        this.id=id;
        this.start=start;
        this.end=end;
        this.day=day;
    }

    public String toString(){
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
        String date = DATE_FORMAT.format(day);
        return "Start: " + start + ", End: "+ end + ", Day: " + date+ ".\n";
    }
    public Integer getId() {
        return id;
    }
    public Date getDay() {return day;}
    public int getStart() {return start;}


}
