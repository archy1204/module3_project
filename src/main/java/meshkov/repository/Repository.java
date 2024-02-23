package meshkov.repository;

import meshkov.exception.*;
import meshkov.model.*;

import java.io.IOException;
import java.util.List;

public interface Repository {
    List<Student> getAllStudents();

    Student getStudentById(int id) throws StudentNotFoundException;

    List<Student> getStudentsByNameAndSurname(String name, String surname) throws StudentNotFoundException;

    Student createStudent(Student student) throws JsonParseException, IOException;

    Student changeStudentData(int id, Student studentUpdate) throws StudentNotFoundException;

    Student deleteStudent(int id) throws StudentNotFoundException;


    List<Teacher> getAllTeachers();

    Teacher getTeacherById(int id) throws TeacherNotFoundException;

    List<Teacher> getTeachersByNameAndSurname(String name, String surname) throws TeacherNotFoundException;

    Teacher createTeacher(Teacher teacher);

    Teacher addSubjects(int id, List<Subject> subjects) throws TeacherNotFoundException;

    Teacher deleteTeacher(int id) throws TeacherNotFoundException;


    List<Group> getAllGroups();

    Group getGroupByNumber(int id) throws GroupNotFoundException;

    List<Group> getGroupsByNameAndSurname(String name, String surname) throws StudentNotFoundException, GroupNotFoundException;

    Group createGroup(Group group);

    public Group addStudentsToGroup(int id, List<Integer> studentsId) throws GroupNotFoundException, StudentNotFoundException;

    Group deleteGroup(int id) throws GroupNotFoundException;


    List<Timetable> getAllTimetable();

    List<Timetable> getTimetableByGroupNumber(int number) throws TimetableNotFoundException;

    List<Timetable> getTimetableByStudent(String name, String surname) throws StudentNotFoundException, GroupNotFoundException;

    List<Timetable> getTimetableByTeacher(String name, String surname) throws TeacherNotFoundException;

    List<Timetable> getTimetableByDate(String date);

    Timetable createTimetable(Timetable timetable);

    public List<Timetable> updateTimetable(String date, List<Timetable> newTimetables);

    List<Timetable> deleteTimetableByDateAndGroupNumber(String date, int groupNumber);

    public void deleteAllTimetable();
}
