package meshkov.checks;

import lombok.extern.slf4j.Slf4j;
import meshkov.exception.*;
import meshkov.model.Subject;
import meshkov.model.Teacher;
import meshkov.repository.Repository;
import meshkov.repository.imp.SimpleRepository;

import java.util.List;

@Slf4j
public class SubjectsCheck extends Middleware {

    public SubjectsCheck(Repository repository) {
        super(repository);
    }

    @Override
    public boolean check(Checkable model) throws TeacherNotFoundException {
        log.debug("Subjects check is processing");
        Teacher teacher = (Teacher) model;
        if (teacher.getId() == -1) {
            log.debug("Subjects check SUCCESS");
            return checkNext(model);
        }

        List<Subject> subjects = repository.getTeacherById(teacher.getId()).getSubjects();
        for (Subject addedSubject : teacher.getSubjects()) {
            if (subjects.contains(addedSubject)) {
                log.debug("Subjects check FAIL");
                return false;
            }
        }
        log.debug("Subjects check SUCCESS");
        return checkNext(model);
    }
}
