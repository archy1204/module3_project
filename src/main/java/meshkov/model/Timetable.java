package meshkov.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import meshkov.checks.Checkable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Timetable implements Checkable {
    private String groupNumber;
    private int teacherId;
    private String startDateTime;
    private String endDateTime;
}
