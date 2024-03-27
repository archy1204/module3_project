package meshkov.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import meshkov.dto.GroupRequest;
import meshkov.model.Group;
import meshkov.model.Student;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-03-27T02:13:49+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 18.0.1.1 (Oracle Corporation)"
)
public class GroupMapperImpl implements GroupMapper {

    @Override
    public Group mapToModel(GroupRequest dto) {
        if ( dto == null ) {
            return null;
        }

        Group.GroupBuilder group = Group.builder();

        List<Student> list = dto.getStudentObjects();
        if ( list != null ) {
            group.students( new ArrayList<Student>( list ) );
        }
        group.number( dto.getNumber() );

        return group.build();
    }
}
