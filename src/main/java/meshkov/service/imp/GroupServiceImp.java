package meshkov.service.imp;

import lombok.extern.slf4j.Slf4j;
import meshkov.checks.*;
import meshkov.consts.FileConstants;
import meshkov.dto.GroupRequest;
import meshkov.exception.*;
import meshkov.mapper.GroupMapper;
import meshkov.model.Group;
import meshkov.repository.Repository;
import meshkov.service.GroupService;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;

@Slf4j
public class GroupServiceImp implements GroupService {

    private final Repository repository;
    private final GroupMapper groupMapper;
    public final int minStudents;
    public final int maxStudents;

    private final Middleware middleware;

    public GroupServiceImp(Repository repository, GroupMapper groupMapper) {
        log.debug("constructor method invoked");
        this.repository = repository;
        this.groupMapper = groupMapper;

        middleware = Middleware.link(
                new GroupNumberCheck(repository),
                new StudentsInGroupCheck(repository)
        );

        Properties properties = new Properties();
        try (BufferedReader propertyReader = Files.newBufferedReader(Path.of(FileConstants.PROPERTY_FILE_NAME))) {
            properties.load(propertyReader);
            minStudents = Integer.parseInt(properties.getProperty("minStudents"));
            maxStudents = Integer.parseInt(properties.getProperty("maxStudents"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Group> getAllGroups() {
        log.debug("getAllGroups method invoked");
        return repository.getAllGroups();
    }

    @Override
    public Group getGroupByNumber(String number) throws GroupNotFoundException {
        log.debug("getGroupByNumber method invoked");
        log.debug("id = {}", number);
        return repository.getGroupByNumber(number);
    }

    @Override
    public List<Group> getGroupsByNameAndSurname(String name, String surname) throws StudentNotFoundException, GroupNotFoundException {
        log.debug("getGroupsByNameAndSurname method invoked");
        log.debug("name = {}, surname = {}", name, surname);
        return repository.getGroupsByNameAndSurname(name, surname);
    }

    @Override
    public Group createGroup(GroupRequest groupRequest) throws GroupNotFoundException, InvalidArgumentsException, TeacherNotFoundException, TimetableNotFoundException, InvalidAmountException {
        log.debug("createGroup method invoked");
        Group groupToCreate = groupMapper.mapToModel(groupRequest);
        groupToCreate.setId(-1);

        if (!middleware.check(groupToCreate))
            throw new InvalidArgumentsException();

        int size = groupToCreate.getStudents().size();
        if (size >= minStudents && size <= maxStudents)
            return repository.createGroup(groupToCreate);
        else
            throw new InvalidAmountException();
    }

    @Override
    public Group addStudentsToGroup(String number, List<Integer> studentsId) throws StudentNotFoundException, GroupNotFoundException, InvalidArgumentsException, TeacherNotFoundException, TimetableNotFoundException, InvalidAmountException {
        log.debug("addStudentsToGroup method invoked");
        log.debug("number = {}, studentsId = {}", number, studentsId);
        Group groupWithNewStudentsToCheck = new Group();
        groupWithNewStudentsToCheck.setStudents(repository.getStudentListById(studentsId));
        groupWithNewStudentsToCheck.setNumber(number);
        groupWithNewStudentsToCheck.setId(-2);

        if (!middleware.check(groupWithNewStudentsToCheck))
            throw new InvalidArgumentsException();

        if ((repository.getGroupByNumber(number).getStudents().size() + studentsId.size()) <= maxStudents)
            return repository.addStudentsToGroup(number, studentsId);
        else
            throw new InvalidAmountException();
    }

    @Override
    public Group deleteGroup(String number) throws GroupNotFoundException {
        log.debug("deleteGroup method invoked");
        log.debug("id = {}", number);
        return repository.deleteGroup(number);
    }
}
