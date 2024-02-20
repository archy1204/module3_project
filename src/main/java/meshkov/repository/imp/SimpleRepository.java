package meshkov.repository.imp;

import meshkov.exception.GroupNotFoundException;
import meshkov.exception.JsonParseException;
import meshkov.exception.StudentNotFoundException;
import meshkov.exception.TeacherNotFoundException;
import meshkov.model.Group;
import meshkov.model.Student;
import meshkov.model.Subject;
import meshkov.model.Teacher;
import meshkov.repository.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SimpleRepository implements Repository {

    private static final SimpleRepository INSTANCE = new SimpleRepository();
    private final CopyOnWriteArrayList<Student> students;
    private final CopyOnWriteArrayList<Teacher> teachers;
    private final CopyOnWriteArrayList<Group> groups;

    private SimpleRepository() {
        this.students = new CopyOnWriteArrayList<>();
        this.teachers = new CopyOnWriteArrayList<>();
        this.groups = new CopyOnWriteArrayList<>();
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
        Student student = getStudentById(id);
        student.setName(studentUpdate.getName());
        student.setSurname(studentUpdate.getSurname());
        student.setBirthday(studentUpdate.getBirthday());
        student.setPhoneNumber(studentUpdate.getPhoneNumber());
        return student;
    }

    @Override
    public Student deleteStudent(int id) throws StudentNotFoundException {
        Student studentToRemove = getStudentById(id);
        students.remove(studentToRemove);
        return studentToRemove;
    }

    @Override
    public List<Teacher> getAllTeachers() {
        return new ArrayList<Teacher>(teachers);
    }

    @Override
    public Teacher getTeacherById(int id) throws TeacherNotFoundException {
        return teachers.stream().filter(tc -> tc.getId() == id).findFirst().orElseThrow(TeacherNotFoundException::new);
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
    public Teacher createTeacher(Teacher teacher) {
        teacher.setId(teachers.size() + 1);
        teachers.add(teacher);
        return teacher;
    }

    @Override
    public Teacher addSubjects(int id, List<Subject> subjects) throws TeacherNotFoundException {
        Teacher teacher = getTeacherById(id);
        teacher.getSubjects().addAll(subjects);
        return teacher;
    }

    @Override
    public Teacher deleteTeacher(int id) throws TeacherNotFoundException {
        Teacher teacherToRemove = getTeacherById(id);
        teachers.remove(teacherToRemove);
        return teacherToRemove;
    }

    @Override
    public List<Group> getAllGroups() {
        return new ArrayList<>(groups);
    }

    @Override
    public Group getGroupByNumber(int id) throws GroupNotFoundException {
        return groups.stream().filter(gr -> Integer.parseInt(gr.getNumber()) == id).findFirst().orElseThrow(GroupNotFoundException::new);
    }

    @Override
    public List<Group> getGroupsByNameAndSurname(String name, String surname) throws StudentNotFoundException, GroupNotFoundException {
        List<Group> requiredGroups = new ArrayList<>();
        List<Student> requiredStudents = getStudentsByNameAndSurname(name, surname);
        for (Group group : groups) {
            List<Student> groupStudents = group.getStudents();
            for (Student student : requiredStudents) {
                if (groupStudents.contains(student)) {
                    requiredGroups.add(group);
                    break;
                }
            }
        }
        if (requiredGroups.isEmpty())
            throw new GroupNotFoundException();
        else
            return requiredGroups;
    }

    @Override
    public Group createGroup(Group group) {
        group.setId(groups.size() + 1);
        groups.add(group);
        return group;
    }

    @Override
    public Group addStudentsToGroup(int id, List<Integer> studentsId) throws GroupNotFoundException, StudentNotFoundException {
        Group group = getGroupByNumber(id);
        List<Student> requiredStudents = new ArrayList<>();
        for (Integer st : studentsId) {
            requiredStudents.add(getStudentById(st));
        }
        group.getStudents().addAll(requiredStudents);
        return group;
    }

    @Override
    public Group deleteGroup(int id) throws GroupNotFoundException {
        Group groupToRemove = getGroupByNumber(id);
        groups.remove(groupToRemove);
        return groupToRemove;
    }
}
