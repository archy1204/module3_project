package meshkov.service.imp;

import lombok.extern.slf4j.Slf4j;
import meshkov.checks.*;
import meshkov.dto.TeacherRequest;
import meshkov.exception.*;
import meshkov.mapper.StudentMapper;
import meshkov.mapper.TeacherMapper;
import meshkov.model.Subject;
import meshkov.model.Teacher;
import meshkov.repository.Repository;
import meshkov.service.TeacherService;

import java.util.List;

@Slf4j
public class TeacherServiceImp implements TeacherService {

    private final Repository repository;
    private final TeacherMapper teacherMapper;

    private final Middleware middleware = Middleware.link(
            new NameAndSurnameCheck(),
            new SubjectsCheck()
    );

    public TeacherServiceImp(Repository repository, TeacherMapper teacherMapper) {
        log.debug("constructor method invoked");
        this.repository = repository;
        this.teacherMapper = teacherMapper;
    }

    @Override
    public List<Teacher> getAllTeachers() {
        log.debug("getAllTeachers method invoked");
        return repository.getAllTeachers();
    }

    @Override
    public Teacher getTeacherById(int id) throws TeacherNotFoundException {
        log.debug("getTeacherById method invoked");
        log.debug("id = {}", id);
        return repository.getTeacherById(id);
    }

    @Override
    public List<Teacher> getTeachersByNameAndSurname(String name, String surname) throws TeacherNotFoundException {
        log.debug("getTeachersByNameAndSurname method invoked");
        log.debug("name = {}, surname = {}", name, surname);
        return repository.getTeachersByNameAndSurname(name, surname);
    }

    @Override
    public Teacher createTeacher(TeacherRequest teacherRequest) throws StudentNotFoundException, GroupNotFoundException, InvalidArgumentsException, TeacherNotFoundException, TimetableNotFoundException {
        log.debug("createTeacher method invoked");
        Teacher teacherToCreate = teacherMapper.mapToModel(teacherRequest);
        teacherToCreate.setId(-1);

        if (!middleware.check(teacherToCreate))
            throw new InvalidArgumentsException();

        return repository.createTeacher(teacherToCreate);
    }

    @Override
    public Teacher addSubjects(int id, List<Subject> subjects) throws TeacherNotFoundException, StudentNotFoundException, GroupNotFoundException, InvalidArgumentsException, TimetableNotFoundException {
        log.debug("addSubjects method invoked");
        log.debug("id = {}, subjects = {}", id, subjects);
        Teacher teacherToCheck = new Teacher();
        teacherToCheck.setSubjects(subjects);
        teacherToCheck.setId(id);
        if (!middleware.check(teacherToCheck))
            throw new InvalidArgumentsException();
        return repository.addSubjects(id, subjects);
    }

    @Override
    public Teacher deleteTeacher(int id) throws TeacherNotFoundException {
        log.debug("deleteTeacher method invoked");
        log.debug("id = {}", id);
        return repository.deleteTeacher(id);
    }
}
