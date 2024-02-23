package meshkov.mapper;

import javax.annotation.processing.Generated;
import meshkov.dto.StudentRequest;
import meshkov.dto.StudentResponse;
import meshkov.model.Student;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-02-23T18:29:18+0300",
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

    @Override
    public StudentResponse mapToResponse(Student student) {
        if ( student == null ) {
            return null;
        }

        StudentResponse.StudentResponseBuilder studentResponse = StudentResponse.builder();

        studentResponse.id( (long) student.getId() );
        studentResponse.name( student.getName() );
        studentResponse.surname( student.getSurname() );
        studentResponse.birthday( student.getBirthday() );
        studentResponse.phoneNumber( student.getPhoneNumber() );

        return studentResponse.build();
    }
}
