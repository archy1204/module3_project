package meshkov.checks;

import lombok.extern.slf4j.Slf4j;
import meshkov.exception.TeacherNotFoundException;
import meshkov.model.Timetable;
import meshkov.repository.Repository;
import meshkov.repository.imp.SimpleRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
public class TeacherInTimetableCheck extends Middleware {

    public TeacherInTimetableCheck(Repository repository) {
        super(repository);
    }

    @Override
    public boolean check(Checkable model) throws TeacherNotFoundException {
        log.debug("TeacherInTimetable check is processing");
        Timetable timetableToCheck = (Timetable) model;
        List<Timetable> teacherTimetables = repository.getTimetableByTeacher(timetableToCheck.getTeacherId());

        DateTimeFormatter timetableFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm");
        LocalDateTime startTimeToCheck = LocalDateTime.parse(timetableToCheck.getStartDateTime(), timetableFormat);
        LocalDateTime endTimeToCheck = LocalDateTime.parse(timetableToCheck.getEndDateTime(), timetableFormat);

        for (Timetable timetable : teacherTimetables) {
            LocalDateTime startTime = LocalDateTime.parse(timetable.getStartDateTime(), timetableFormat);
            LocalDateTime endTime = LocalDateTime.parse(timetable.getEndDateTime(), timetableFormat);
            if (startTimeToCheck.equals(startTime) && endTimeToCheck.equals(endTime)) {
                log.debug("TeacherInTimetable check FAIL");
                return false;
            }
        }
        log.debug("TeacherInTimetable check SUCCESS");
        return checkNext(model);
    }
}