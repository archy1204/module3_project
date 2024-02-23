package meshkov.checks;

import meshkov.model.Checkable;

public class SubjectsCheck extends Middleware {
    @Override
    public boolean check(Checkable model) {
        return false;
    }
}
