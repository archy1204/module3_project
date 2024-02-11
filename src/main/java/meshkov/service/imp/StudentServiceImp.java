package meshkov.service.imp;

import meshkov.dto.StudentResponse;
import meshkov.exception.StudentNotFoundException;
import meshkov.mapper.StudentMapper;
import meshkov.repository.Repository;
import meshkov.service.StudentService;

import java.util.List;

public class StudentServiceImp implements StudentService {

    Repository repository;
    StudentMapper studentMapper;

    public StudentServiceImp(Repository repository) {
        this.repository = repository;
    }

    @Override
    public List<StudentResponse> getAllStudent() {
        return repository.getAllStudents().stream().map(studentMapper::mapToResponse).toList();
    }

    @Override
    public StudentResponse getStudentById(int id) throws StudentNotFoundException {
        return studentMapper.mapToResponse(repository.getStudentById(id));
    }
}
