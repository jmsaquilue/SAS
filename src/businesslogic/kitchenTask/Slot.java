package businesslogic.kitchenTask;

import businesslogic.user.Cook;

import java.util.ArrayList;

public class Slot implements Comparable<Slot>{
    private Shift s;
    private Task t;
    private Cook c;
    private Boolean available;

    public Slot(){

    }


    public Slot( Shift s, Cook c, Task t, boolean b) {
        this.s = s;
        this.c = c;
        this.t = t;
        this.available = b;
    }
    public String toString(){
        String r="";
        if(s!=null){
            r= r + "Turno: " + s.toString();
        }
        if(t!=null){
            r= r + "Compito: " + t.show();
        }
        if(available){
            r = r + "Disponibile\n";
            r += "Cuochi disponibili: ";
            for (Cook cc: s.getCooks()){
                r += cc.toString() + ", ";
            }
            r = r.substring(0, r.length()-2);
            r += ".";
        }else {
            r= r + "Non disponibile\n";
            if (c != null)
                r = r + "Cuoco:" + c.toString();
            else
                r = r + "Cuoco non assegnato.";
        }

        return r;
    }

    @Override
    public int compareTo(Slot slot1)
    {
        if (slot1.getS().getDay().equals(s.getDay())){
            return Integer.compare(s.getStart(),slot1.getS().getStart());
        }
        else {
            return s.getDay().compareTo(slot1.getS().getDay());
        }
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

    public void removeTask() {
        this.t = null;
    }

    public void setFree() {
        this.available = true;
    }


    public void setCook(Cook c) {
        this.c = c;
    }
}
