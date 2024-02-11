package meshkov.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import meshkov.model.Subject;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Teacher {
    private String name;
    private List<Subject> subjects;
    private int experience;
}
