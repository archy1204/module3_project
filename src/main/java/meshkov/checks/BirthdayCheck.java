package meshkov.checks;

import lombok.extern.slf4j.Slf4j;
import meshkov.model.Student;
import meshkov.repository.Repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
public class BirthdayCheck extends Middleware {
    public BirthdayCheck(Repository repository) {
        super(repository);
    }

    @Override
    public boolean check(Checkable model) {
        log.debug("Birthday check is processing");
        Student student = (Student) model;
        DateTimeFormatter requiredFormat = DateTimeFormatter.ofPattern("d.MM.yyyy");
        LocalDate birthday = LocalDate.parse(student.getBirthday(), requiredFormat);
        if (birthday.isBefore(LocalDate.now())) {
            log.debug("Birthday check SUCCESS");
            return checkNext(model);
        }
        else {
            log.debug("Birthday check FAIL");
            return false;
        }
    }
}
