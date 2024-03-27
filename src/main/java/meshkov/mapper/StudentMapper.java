package meshkov.mapper;

import meshkov.dto.StudentRequest;
import meshkov.model.Student;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StudentMapper {

    StudentMapper INSTANCE = Mappers.getMapper(StudentMapper.class);

    Student mapToModel(StudentRequest dto);
}
