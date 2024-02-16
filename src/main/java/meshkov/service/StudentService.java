package meshkov.service;

import meshkov.dto.StudentRequest;
import meshkov.dto.StudentResponse;
import meshkov.exception.JsonParseException;
import meshkov.exception.StudentNotFoundException;
import meshkov.model.Student;

import java.io.IOException;
import java.util.List;

public interface StudentService {

    List<StudentResponse> getAllStudent();

    StudentResponse getStudentById(int id) throws StudentNotFoundException;

    StudentResponse createStudent(StudentRequest studentRequest) throws IOException, JsonParseException;

    List<StudentResponse> getStudentsByNameAndSurname(String name, String surname) throws StudentNotFoundException;

    StudentResponse changeStudentData(int id, StudentRequest studentRequest) throws StudentNotFoundException;

    StudentResponse deleteStudent(int id) throws StudentNotFoundException;
}
