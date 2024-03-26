package meshkov.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import meshkov.checks.Checkable;

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
