package businesslogic.kitchenTask;

import businesslogic.user.Cook;

import java.util.ArrayList;
import java.util.Date;

public class Shift {
    private int start;
    private int end;
    private Date day;
    private Boolean available;
    private ArrayList<Cook> cooks; // He vuelto a cambiar el nombre
}
