package meshkov.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import meshkov.dto.TeacherRequest;
import meshkov.model.Subject;
import meshkov.model.Teacher;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-03-25T19:51:21+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 18.0.1.1 (Oracle Corporation)"
)
public class TeacherMapperImpl implements TeacherMapper {

    @Override
    public Teacher mapToModel(TeacherRequest dto) {
        if ( dto == null ) {
            return null;
        }

        Teacher.TeacherBuilder teacher = Teacher.builder();

        teacher.name( dto.getName() );
        teacher.surname( dto.getSurname() );
        List<Subject> list = dto.getSubjects();
        if ( list != null ) {
            teacher.subjects( new ArrayList<Subject>( list ) );
        }
        teacher.experience( dto.getExperience() );

        return teacher.build();
    }
}
