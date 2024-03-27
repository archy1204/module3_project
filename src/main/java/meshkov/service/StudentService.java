package meshkov.service;

import meshkov.dto.StudentRequest;
import meshkov.exception.*;
import meshkov.model.Student;

import java.io.IOException;
import java.util.List;

public interface StudentService {

    List<Student> getAllStudent();

    Student getStudentById(int id) throws StudentNotFoundException;

    Student createStudent(StudentRequest studentRequest) throws IOException, JsonParseException, TeacherNotFoundException, StudentNotFoundException, GroupNotFoundException, InvalidArgumentsException, TimetableNotFoundException;

    List<Student> getStudentsByNameAndSurname(String name, String surname) throws StudentNotFoundException;

    Student changeStudentData(int id, StudentRequest studentRequest) throws StudentNotFoundException, TeacherNotFoundException, GroupNotFoundException, InvalidArgumentsException, TimetableNotFoundException;

    Student deleteStudent(int id) throws StudentNotFoundException;
}
