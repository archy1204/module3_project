package meshkov.checks;

import meshkov.model.Checkable;

public class PhoneNumberCheck extends Middleware {
    @Override
    public boolean check(Checkable model) {
        return false;
    }
}
