package meshkov.checks;

import meshkov.model.Checkable;

public abstract class Middleware {
    private Middleware next;


    public static Middleware link(Middleware first, Middleware... chain) {
        Middleware head = first;
        for (Middleware nextInChain : chain) {
            head.next = nextInChain;
            head = nextInChain;
        }
        return first;
    }

    public abstract boolean check(Checkable model);


    protected boolean checkNext(Checkable model) {
        if (next == null) {
            return true;
        }
        return next.check(model);
    }
}
