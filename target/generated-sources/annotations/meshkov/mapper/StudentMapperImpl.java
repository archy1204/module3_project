package meshkov.mapper;

import javax.annotation.processing.Generated;
import meshkov.dto.StudentRequest;
import meshkov.model.Student;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-03-16T19:48:10+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 18.0.1.1 (Oracle Corporation)"
)
public class StudentMapperImpl implements StudentMapper {

    @Override
    public Student mapToModel(StudentRequest dto) {
        if ( dto == null ) {
            return null;
        }

        Student.StudentBuilder student = Student.builder();

        student.name( dto.getName() );
        student.surname( dto.getSurname() );
        student.birthday( dto.getBirthday() );
        student.phoneNumber( dto.getPhoneNumber() );

        return student.build();
    }
}
