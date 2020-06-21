package businesslogic.kitchenTask;

import businesslogic.user.Cook;

public class Foo {
    private Shift s;
    private Task t;
    private Cook c;
    private Boolean available;


    public Foo(Shift s, Cook c, Task t, boolean b) {
        this.s = s;
        this.c = c;
        this.t = t;
        this.available = b;
    }
}
