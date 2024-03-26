package meshkov.checks;

import lombok.extern.slf4j.Slf4j;
import meshkov.exception.*;
import meshkov.model.Timetable;
import meshkov.repository.Repository;
import meshkov.repository.imp.SimpleRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class GroupInTimeTableCheck extends Middleware {

    public GroupInTimeTableCheck(Repository repository) {
        super(repository);
    }

    @Override
    public boolean check(Checkable model) throws TimetableNotFoundException {
        log.debug("GroupInTimeTable check is processing");
        Timetable timetableToCheck = (Timetable) model;
        List<Timetable> groupTimetables;

        try {
            groupTimetables = repository.getTimetableByGroupNumber(timetableToCheck.getGroupNumber());
        } catch (TimetableNotFoundException e) {
            groupTimetables = new ArrayList<>();
        }

        DateTimeFormatter timetableFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm");
        LocalDateTime startTimeToCheck = LocalDateTime.parse(timetableToCheck.getStartDateTime(), timetableFormat);
        LocalDateTime endTimeToCheck = LocalDateTime.parse(timetableToCheck.getEndDateTime(), timetableFormat);

        for (Timetable timetable : groupTimetables) {
            LocalDateTime startTime = LocalDateTime.parse(timetable.getStartDateTime(), timetableFormat);
            LocalDateTime endTime = LocalDateTime.parse(timetable.getEndDateTime(), timetableFormat);
            if (startTimeToCheck.equals(startTime) || endTimeToCheck.equals(endTime)
                    || startTimeToCheck.equals(endTime) || endTimeToCheck.equals(startTime)
                    || startTimeToCheck.isAfter(startTime) && startTimeToCheck.isBefore(endTime)
                    || endTimeToCheck.isAfter(startTime) && endTimeToCheck.isBefore(endTime)
                    || startTimeToCheck.isBefore(startTime) && endTimeToCheck.isAfter(endTime)) {
                log.debug("GroupInTimeTable check FAIL");
                return false;
            }
        }
        log.debug("GroupInTimeTable check SUCCESS");
        return checkNext(model);
    }
}