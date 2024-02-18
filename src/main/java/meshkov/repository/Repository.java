package meshkov.repository;

import meshkov.dto.StudentRequest;
import meshkov.dto.StudentResponse;
import meshkov.dto.TeacherRequest;
import meshkov.exception.JsonParseException;
import meshkov.exception.StudentNotFoundException;
import meshkov.exception.TeacherNotFoundException;
import meshkov.model.Student;
import meshkov.model.Subject;
import meshkov.model.Teacher;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public interface Repository {
    List<Student> getAllStudents();

    Student getStudentById(int id) throws StudentNotFoundException;

    List<Student> getStudentsByNameAndSurname(String name, String surname) throws StudentNotFoundException;

    Student createStudent(Student student) throws JsonParseException, IOException;

    Student changeStudentData(int id, Student studentUpdate) throws StudentNotFoundException;

    Student deleteStudent(int id) throws StudentNotFoundException;

    List<Teacher> getAllTeachers();

    Teacher getTeacherById(int id) throws TeacherNotFoundException;

    Teacher createTeacher(Teacher teacher);

    List<Teacher> getTeachersByNameAndSurname(String name, String surname) throws TeacherNotFoundException;

    Teacher addSubjects(int id, List<Subject> subjects) throws TeacherNotFoundException;

    Teacher deleteTeacher(int id) throws TeacherNotFoundException;
}
