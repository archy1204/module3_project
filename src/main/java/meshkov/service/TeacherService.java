package meshkov.service;

import meshkov.dto.TeacherRequest;
import meshkov.exception.*;
import meshkov.model.Subject;
import meshkov.model.Teacher;

import java.util.List;

public interface TeacherService {

    List<Teacher> getAllTeachers();

    Teacher getTeacherById(int id) throws TeacherNotFoundException;

    List<Teacher> getTeachersByNameAndSurname(String name, String surname) throws TeacherNotFoundException;

    Teacher createTeacher(TeacherRequest teacherRequest) throws StudentNotFoundException, GroupNotFoundException, InvalidArgumentsException, TeacherNotFoundException, TimetableNotFoundException;

    Teacher addSubjects(int id, List<Subject> subjects) throws TeacherNotFoundException, StudentNotFoundException, GroupNotFoundException, InvalidArgumentsException, TimetableNotFoundException;

    Teacher deleteTeacher(int id) throws TeacherNotFoundException;
}
