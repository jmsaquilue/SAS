package businesslogic.kitchenTask;

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
        return "Start: " + start + ", End: "+ end + ", Day: " + day+ ".\n";
    }
    public Integer getId() {
        return id;
    }
    public Date getDay() {return day;}
    public int getStart() {return start;}


}
