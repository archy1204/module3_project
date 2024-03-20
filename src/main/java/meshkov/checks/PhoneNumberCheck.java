package meshkov.checks;

import lombok.extern.slf4j.Slf4j;
import meshkov.consts.RegexConsts;
import meshkov.model.Student;
import meshkov.repository.Repository;

@Slf4j
public class PhoneNumberCheck extends Middleware {
    public PhoneNumberCheck(Repository repository) {
        super(repository);
    }

    @Override
    public boolean check(Checkable model) {
        log.debug("PhoneNumber check is processing");
        Student student = (Student) model;
        if (student.getPhoneNumber().matches(RegexConsts.PHONENUMBER)) {
            log.debug("PhoneNumber check SUCCESS");
            return checkNext(model);
        } else {
            log.debug("PhoneNumber check FAIL");
            return false;
        }
    }
}
