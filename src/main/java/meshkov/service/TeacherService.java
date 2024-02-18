package meshkov.service;

import meshkov.dto.TeacherRequest;
import meshkov.exception.TeacherNotFoundException;
import meshkov.model.Subject;
import meshkov.model.Teacher;

import java.util.List;

public interface TeacherService {

    List<Teacher> getAllTeachers();

    Teacher getTeacherById(int id) throws TeacherNotFoundException;

    Teacher createTeacher(TeacherRequest teacherRequest);

    List<Teacher> getTeachersByNameAndSurname(String name, String surname) throws TeacherNotFoundException;

    Teacher addSubjects(int id, List<Subject> subjects);

    Teacher deleteTeacher(int id);
}
