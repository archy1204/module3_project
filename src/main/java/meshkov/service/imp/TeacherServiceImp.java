package meshkov.service.imp;

import meshkov.dto.TeacherRequest;
import meshkov.exception.TeacherNotFoundException;
import meshkov.mapper.StudentMapper;
import meshkov.mapper.TeacherMapper;
import meshkov.model.Subject;
import meshkov.model.Teacher;
import meshkov.repository.Repository;
import meshkov.service.TeacherService;

import java.util.List;

public class TeacherServiceImp implements TeacherService {

    Repository repository;
    TeacherMapper teacherMapper;

    public TeacherServiceImp(Repository repository, TeacherMapper teacherMapper) {
        this.repository = repository;
        this.teacherMapper = teacherMapper;
    }

    @Override
    public List<Teacher> getAllTeachers() {
        return repository.getAllTeachers();
    }

    @Override
    public Teacher getTeacherById(int id) throws TeacherNotFoundException {
        return repository.getTeacherById(id);
    }

    @Override
    public Teacher createTeacher(TeacherRequest teacherRequest) {
        return repository.createTeacher(teacherMapper.mapToModel(teacherRequest));
    }

    @Override
    public List<Teacher> getTeachersByNameAndSurname(String name, String surname) throws TeacherNotFoundException {
        return repository.getTeachersByNameAndSurname(name, surname);
    }

    @Override
    public Teacher addSubjects(int id, List<Subject> subjects) {
        return null;
    }

    @Override
    public Teacher deleteTeacher(int id) {
        return null;
    }
}
