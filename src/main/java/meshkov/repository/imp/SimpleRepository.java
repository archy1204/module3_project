package meshkov.repository.imp;

import meshkov.dto.StudentRequest;
import meshkov.dto.StudentResponse;
import meshkov.exception.JsonParseException;
import meshkov.exception.StudentNotFoundException;
import meshkov.exception.TeacherNotFoundException;
import meshkov.model.Student;
import meshkov.model.Subject;
import meshkov.model.Teacher;
import meshkov.repository.Repository;
import meshkov.service.JsonService;
import meshkov.service.imp.JsonServiceImp;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SimpleRepository implements Repository {

    private static final SimpleRepository INSTANCE = new SimpleRepository();
    private final CopyOnWriteArrayList<Student> students;
    private final CopyOnWriteArrayList<Teacher> teachers;

    private SimpleRepository() {
        this.students = new CopyOnWriteArrayList<>();
        this.teachers = new CopyOnWriteArrayList<>();
    }

    public static SimpleRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Student> getAllStudents() {
        return new ArrayList<Student>(students);
    }

    @Override
    public Student getStudentById(int id) throws StudentNotFoundException {
        return students.stream().filter(st -> st.getId() == id).findFirst().orElseThrow(StudentNotFoundException::new);
    }

    @Override
    public List<Student> getStudentsByNameAndSurname(String name, String surname) throws StudentNotFoundException {
        List<Student> requiredList = students.stream().filter(st -> st.getName().equals(name) && st.getSurname().equals(surname)).toList();
        if (requiredList.isEmpty())
            throw new StudentNotFoundException();
        else
            return requiredList;
    }

    @Override
    public Student createStudent(Student student) throws JsonParseException, IOException {
        student.setId(students.size() + 1);
        students.add(student);
        return student;
    }

    @Override
    public Student changeStudentData(int id, Student studentUpdate) throws StudentNotFoundException {
        Student student = students.stream().filter(st -> st.getId() == id).findFirst().orElseThrow(StudentNotFoundException::new);
        student.setName(studentUpdate.getName());
        student.setSurname(studentUpdate.getSurname());
        student.setBirthday(studentUpdate.getBirthday());
        student.setPhoneNumber(studentUpdate.getPhoneNumber());
        return student;
    }

    @Override
    public Student deleteStudent(int id) throws StudentNotFoundException {
        Student studentToRemove = students.stream().filter(st -> st.getId() == id).findFirst().orElseThrow(StudentNotFoundException::new);
        students.remove(studentToRemove);
        return studentToRemove;
    }

    @Override
    public List<Teacher> getAllTeachers() {
        return new ArrayList<Teacher>(teachers);
    }

    @Override
    public Teacher getTeacherById(int id) throws TeacherNotFoundException {
        return teachers.stream().filter(st -> st.getId() == id).findFirst().orElseThrow(TeacherNotFoundException::new);
    }

    @Override
    public Teacher createTeacher(Teacher teacher) {
        teacher.setId(teachers.size() + 1);
        teachers.add(teacher);
        return teacher;
    }

    @Override
    public List<Teacher> getTeachersByNameAndSurname(String name, String surname) throws TeacherNotFoundException {
        List<Teacher> requiredList = teachers.stream().filter(st -> st.getName().equals(name) && st.getSurname().equals(surname)).toList();
        if (requiredList.isEmpty())
            throw new TeacherNotFoundException();
        else
            return requiredList;
    }

    @Override
    public Teacher addSubjects(int id, List<Subject> subjects) throws TeacherNotFoundException {
        Teacher teacher = teachers.stream().filter(st -> st.getId() == id).findFirst().orElseThrow(TeacherNotFoundException::new);
        teacher.getSubjects().addAll(subjects);
        return teacher;
    }

    @Override
    public Teacher deleteTeacher(int id) throws TeacherNotFoundException {
        Teacher teacherToRemove = teachers.stream().filter(st -> st.getId() == id).findFirst().orElseThrow(TeacherNotFoundException::new);
        teachers.remove(teacherToRemove);
        return teacherToRemove;
    }
}
