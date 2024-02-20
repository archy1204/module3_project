package meshkov.mapper;

import meshkov.dto.GroupRequest;
import meshkov.model.Group;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface GroupMapper {

    GroupMapper INSTANCE = Mappers.getMapper(GroupMapper.class);

    @Mapping(target = "students", source = "dto.studentObjects")
    Group mapToModel(GroupRequest dto);
}
