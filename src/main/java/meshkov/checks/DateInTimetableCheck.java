package meshkov.checks;

import lombok.extern.slf4j.Slf4j;
import meshkov.model.Timetable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class DateInTimetableCheck extends Middleware {
    @Override
    public boolean check(Checkable model) {
        log.debug("DateInTimetable check is processing");
        Timetable timetableToCheck = (Timetable) model;
        DateTimeFormatter timetableFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm");

        try {
            LocalDateTime startTimeToCheck = LocalDateTime.parse(timetableToCheck.getStartDateTime(), timetableFormat);
            LocalDateTime endTimeToCheck = LocalDateTime.parse(timetableToCheck.getEndDateTime(), timetableFormat);
        } catch (Exception e) {
            log.debug("DateInTimetable check FAIL");
            return false;
        }
        log.debug("DateInTimetable check SUCCESS");
        return checkNext(model);
    }
}
