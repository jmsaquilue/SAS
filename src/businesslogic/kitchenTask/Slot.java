package businesslogic.kitchenTask;

import businesslogic.user.Cook;

public class Slot {
    private int id;
    private Shift s;
    private Task t;
    private Cook c;
    private Boolean available;

    public Slot(){
        this.id = 0;
    }


    public Slot(int id, Shift s, Cook c, Task t, boolean b) {
        this.id = id;
        this.s = s;
        this.c = c;
        this.t = t;
        this.available = b;
    }
    public String toString(){
        String r="";
        if(s!=null){
            r= r + "Shift: " + s.toString();
        }
        if(t!=null){
            r= r + "Task: " + t.toString();
        }
        if(c!=null){
            r=r + "Cook:" + c.toString() + ".\n";
        }
        if(available){
            r= r + "Disponible";
        }else {
            r= r + "No Disponible";
        }
        return r;
    }

    public Task getT(){
        return t;
    }

    public Cook getC(){
        return c;
    }

    public Shift getS(){
        return s;
    }

    public Boolean getAvailable() {return available;}

    public void disavailable() {
        available = false;
    }

    public void setTask(Task t) {
        this.t = t;
    }

    /*
    public static Slot loadSlot(Task t, Cook c, Shift shift){
        Slot s = new Slot();

        String querry = "SELECT * FROM CookShifts WHERE shift_id='"+ shift.getId()+
                "' and cook_id='"+c.getId()+"';";

        PersistenceManager.executeQuery(querry, new ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                s.id = rs.getInt("id");
                s.t = t;
                s.s = shift;
                s.c = c;
            }
        });

        return s;
    }
     */


}
