package meshkov.checks;

import lombok.SneakyThrows;
import meshkov.exception.*;
import meshkov.repository.Repository;

public abstract class Middleware {
    private Middleware next;
    protected Repository repository;

    public Middleware() {
    }

    public Middleware(Repository repository) {
        this.repository = repository;
    }

    public static Middleware link(Middleware first, Middleware... chain) {
        Middleware head = first;
        for (Middleware nextInChain : chain) {
            head.next = nextInChain;
            head = nextInChain;
        }
        return first;
    }

    public abstract boolean check(Checkable model) throws TimetableNotFoundException, GroupNotFoundException, TeacherNotFoundException;

@SneakyThrows
    protected boolean checkNext(Checkable model) {
        if (next == null) {
            return true;
        }
        return next.check(model);
    }
}
