package meshkov.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Timetable implements Checkable {
    private int groupNumber;
    private int teacherId;
    private String startDateTime;
    private String endDateTime;
}
