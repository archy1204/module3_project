package meshkov.mapper;

import meshkov.dto.StudentRequest;
import meshkov.dto.TeacherRequest;
import meshkov.model.Teacher;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TeacherMapper {

    TeacherMapper INSTANCE = Mappers.getMapper(TeacherMapper.class);

    Teacher mapToModel(TeacherRequest dto);
}
