package meshkov.service;

import meshkov.dto.StudentResponse;
import meshkov.exception.StudentNotFoundException;

import java.util.List;

public interface StudentService {

    List<StudentResponse> getAllStudent();

    StudentResponse getStudentById(int id) throws StudentNotFoundException;
}
