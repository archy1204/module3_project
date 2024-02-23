package meshkov.model;

import ch.qos.logback.core.model.Model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Student implements Checkable {
    private int id;
    private String name;
    private String surname;
    private String birthday;
    private String phoneNumber;
    private String groupNumber;
}
