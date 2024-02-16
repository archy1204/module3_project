package meshkov.service.imp;

import meshkov.dto.StudentRequest;
import meshkov.dto.StudentResponse;
import meshkov.exception.JsonParseException;
import meshkov.exception.StudentNotFoundException;
import meshkov.mapper.StudentMapper;
import meshkov.model.Student;
import meshkov.repository.Repository;
import meshkov.service.StudentService;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class StudentServiceImp implements StudentService {

    Repository repository;
    StudentMapper studentMapper;

    public StudentServiceImp(Repository repository, StudentMapper studentMapper) {
        this.repository = repository;
        this.studentMapper = studentMapper;
    }

    @Override
    public List<StudentResponse> getAllStudent() {
        return repository.getAllStudents().stream().map(studentMapper::mapToResponse).toList(); //странное исключение - class java.util.LinkedHashMap cannot be cast to class meshkov.model.Student
    }

    @Override
    public StudentResponse getStudentById(int id) throws StudentNotFoundException {
        return studentMapper.mapToResponse(repository.getStudentById(id));
    }

    @Override
    public StudentResponse createStudent(StudentRequest studentRequest) throws IOException, JsonParseException {
        Student student = studentMapper.mapToModel(studentRequest);
        return studentMapper.mapToResponse(repository.createStudent(student));
    }

    @Override
    public List<StudentResponse> getStudentsByNameAndSurname(String name, String surname) throws StudentNotFoundException {
        return repository.getStudentsByNameAndSurname(name, surname).stream().map(studentMapper::mapToResponse).toList();
    }

    @Override
    public StudentResponse changeStudentData(int id, StudentRequest studentRequest) throws StudentNotFoundException {
        Student studentUpdate = studentMapper.mapToModel(studentRequest);
        Student updatedStudent = repository.changeStudentData(id, studentUpdate);
        return studentMapper.mapToResponse(updatedStudent);
    }

    @Override
    public StudentResponse deleteStudent(int id) throws StudentNotFoundException {
        Student student = repository.deleteStudent(id);
        return studentMapper.mapToResponse(student);
    }


}
