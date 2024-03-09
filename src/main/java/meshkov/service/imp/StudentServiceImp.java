package meshkov.service.imp;

import lombok.extern.slf4j.Slf4j;
import meshkov.checks.BirthdayCheck;
import meshkov.checks.Middleware;
import meshkov.checks.NameAndSurnameCheck;
import meshkov.checks.PhoneNumberCheck;
import meshkov.dto.StudentRequest;
import meshkov.exception.*;
import meshkov.mapper.StudentMapper;
import meshkov.model.Student;
import meshkov.repository.Repository;
import meshkov.service.StudentService;

import java.io.IOException;
import java.util.List;

@Slf4j
public class StudentServiceImp implements StudentService {

    private final Repository repository;
    private final StudentMapper studentMapper;

    private final Middleware middleware = Middleware.link(
            new NameAndSurnameCheck(),
            new BirthdayCheck(),
            new PhoneNumberCheck()
    );

    public StudentServiceImp(Repository repository, StudentMapper studentMapper) {
        log.debug("constructor method invoked");
        this.repository = repository;
        this.studentMapper = studentMapper;
    }

    @Override
    public List<Student> getAllStudent() {
        log.debug("getAllStudent method invoked");
        return repository.getAllStudents();
    }

    @Override
    public Student getStudentById(int id) throws StudentNotFoundException {
        log.debug("getStudentById method invoked");
        log.debug("id = {}", id);
        return repository.getStudentById(id);
    }

    @Override
    public List<Student> getStudentsByNameAndSurname(String name, String surname) throws StudentNotFoundException {
        log.debug("getStudentsByNameAndSurname method invoked");
        log.debug("name = {}, surname = {}", name, surname);
        return repository.getStudentsByNameAndSurname(name, surname);
    }

    @Override
    public Student createStudent(StudentRequest studentRequest) throws IOException, JsonParseException, TeacherNotFoundException, StudentNotFoundException, GroupNotFoundException, InvalidArgumentsException, TimetableNotFoundException {
        log.debug("createStudent method invoked");
        log.debug("studentRequest = {}", studentRequest);
        Student newStudent = studentMapper.mapToModel(studentRequest);

        if (!middleware.check(newStudent))
            throw new InvalidArgumentsException();

        return repository.createStudent(newStudent);
    }

    @Override
    public Student changeStudentData(int id, StudentRequest studentRequest) throws StudentNotFoundException, TeacherNotFoundException, GroupNotFoundException, InvalidArgumentsException, TimetableNotFoundException {
        log.debug("changeStudentData method invoked");
        log.debug("id = {}, studentRequest = {}", id, studentRequest);
        Student studentUpdate = studentMapper.mapToModel(studentRequest);
        if (!middleware.check(studentUpdate))
            throw new InvalidArgumentsException();
        return repository.changeStudentData(id, studentUpdate);
    }

    @Override
    public Student deleteStudent(int id) throws StudentNotFoundException {
        log.debug("deleteStudent method invoked");
        log.debug("id = {}", id);
        return repository.deleteStudent(id);
    }


}
