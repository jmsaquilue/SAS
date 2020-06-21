package businesslogic.kitchenTask;

import businesslogic.event.Event;
import businesslogic.user.Cook;
import businesslogic.user.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.PersistenceManager;
import persistence.ResultHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class Shift {
    private int id;
    private int start;
    private int end;
    private Date day;
    //private Boolean available;
    //private Cook cook;
    //private Task task;

    public Shift(int id,int start,int end,Date day){

    }


    public Integer getId() {
        return id;
    }
}
