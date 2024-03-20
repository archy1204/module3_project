package meshkov.checks;

import lombok.extern.slf4j.Slf4j;
import meshkov.exception.*;
import meshkov.model.Group;
import meshkov.model.Student;
import meshkov.repository.Repository;
import meshkov.repository.imp.SimpleRepository;

import java.util.List;

@Slf4j
public class StudentsInGroupCheck extends Middleware {

    public StudentsInGroupCheck(Repository repository) {
        super(repository);
    }

    @Override
    public boolean check(Checkable model) throws GroupNotFoundException {
        log.debug("StudentsInGroup check is processing");
        Group groupToCheck = (Group) model;

            for (Student student : groupToCheck.getStudents()) {
                if (student.getGroupNumber() != null)
                    return false;
            }
            return checkNext(model);


        /*List<Integer> oldStudentsIds = repository.getGroupByNumber(groupToCheck.getNumber()).getStudents()
                .stream().map(Student::getId).toList();
        List<Integer> newStudentsIds = groupToCheck.getStudents().stream().map(Student::getId).toList();


        for (int newStudentId : newStudentsIds) {
            if (oldStudentsIds.contains(newStudentId)) {
                log.debug("StudentsInGroup check FAIL");
                return false;
            }
        }
        log.debug("StudentsInGroup check SUCCESS");
        return checkNext(model);*/
    }
}
