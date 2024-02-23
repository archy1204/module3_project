package meshkov.model;

import ch.qos.logback.core.model.Model;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Group implements Checkable {
    private int id;
    private String number;
    private List<Student> students;
}
