package businesslogic.kitchenTask;

import businesslogic.recipe.Recipe;

import java.util.ArrayList;

public class Task {
    private int timeEstimate;
    private int quantity;
    private Boolean complete;
    private Recipe isPrepared; //No me gusta mucho el nombre
    private ArrayList<Shift> shifts; // He cambiado el nombre, habría que actualizar los gráficos
}
