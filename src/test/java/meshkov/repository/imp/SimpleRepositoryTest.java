package meshkov.repository.imp;

import lombok.SneakyThrows;
import meshkov.model.Group;
import meshkov.model.Student;
import meshkov.model.Teacher;
import meshkov.model.Timetable;
import org.junit.jupiter.api.*;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static meshkov.model.Subject.*;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SimpleRepositoryTest {

    static SimpleRepository repository;
    static Student student1;
    static Student student2;
    static Student student3;
    static Student student4;
    static Student student5;
    static Teacher teacher1;
    static Teacher teacher2;
    static Teacher teacher3;
    static Timetable timetable1;
    static Timetable timetable2;
    static Timetable timetable3;
    static Timetable timetable4;
    static Timetable timetable5;
    static Group group1;
    static Group group2;
    static List<Student> students;
    static List<Teacher> teachers;
    static List<Group> groups;
    static List<Timetable> timetables;

    @BeforeAll
    static void setUp() {
        repository = SimpleRepository.getInstance();
        student1 = new Student(1, "John", "Key", "01.06.1999", "89080967456", "1142");
        student2 = new Student(2, "Jenny", "Law", "12.09.1995", "89080957823", "1142");
        student3 = new Student(3, "Kerri", "Malem", "23.01.2000", "89193412753", "1143");
        student4 = new Student(4, "Michael", "Warshtok", "26.10.1998", "89080967249", "1145");
        student5 = new Student(5, "Van", "Grat", "05.08.1997", "89110456523", "1145");
        teacher1 = new Teacher(1, "John", "Key", new ArrayList<>(Arrays.asList(ENGLISH, HISTORY, SOCIOLOGY)), 5);
        teacher2 = new Teacher(2, "Marry", "Roana", new ArrayList<>(Arrays.asList(PROGRAMMING, MATH)), 9);
        teacher3 = new Teacher(3, "Alex", "Aleman", new ArrayList<>(Arrays.asList(PHYSICS, MATH)), 5);
        group1 = new Group(1, "1142", new ArrayList<>(List.of(student1, student2)));
        group2 = new Group(2, "1145", new ArrayList<>(List.of(student4, student5)));
        timetable1 = new Timetable("1142", 1, "2024-07-11 14:00", "2024-07-11 16:00");
        timetable2 = new Timetable("1142", 2, "2024-07-11 18:00", "2024-07-11 20:00");
        timetable3 = new Timetable("1145", 1, "2024-07-11 14:00", "2024-07-11 16:00");
        timetable4 = new Timetable("1145", 3, "2024-07-12 14:00", "2024-07-12 16:00");
        timetable5 = new Timetable("1145", 3, "2024-07-12 10:00", "2024-07-12 12:00");
        students = new ArrayList<>(Arrays.asList(student1, student2, student3, student4, student5));
        teachers = new ArrayList<>(Arrays.asList(teacher1, teacher2, teacher3));
        groups = new ArrayList<>(Arrays.asList(group1, group2));
        timetables = new ArrayList<>(Arrays.asList(timetable1, timetable2, timetable3, timetable4));
    }

    @AfterAll
    static void clearDB() {
        repository.clearDB();
    }

    @Test
    @Order(2)
    void getAllStudentsTest() {
        Assertions.assertEquals(students, repository.getAllStudents());
    }

    @SneakyThrows
    @Test
    @Order(5)
    void getStudentListByIdTest() {
        Assertions.assertEquals(Arrays.asList(student2, student3, student4), repository.getStudentListById(Arrays.asList(student2.getId(), student3.getId(), student4.getId())));
    }

    @SneakyThrows
    @Test
    @Order(3)
    void getStudentByIdTest() {
        Assertions.assertEquals(student1, repository.getStudentById(1));
    }

    @SneakyThrows
    @Test
    @Order(4)
    void getStudentsByNameAndSurnameTest() {
        Assertions.assertEquals(List.of(student3), repository.getStudentsByNameAndSurname("Kerri", "Malem"));
    }

    @SneakyThrows
    @Test
    @Order(1)
    void createStudentTest() {
        repository.createStudent(student1);
        repository.createStudent(student2);
        repository.createStudent(student3);
        repository.createStudent(student4);
        Assertions.assertEquals(student5, repository.createStudent(student5));
    }

    @SneakyThrows
    @Test
    @Order(6)
    void changeStudentDataTest() {
        student5.setName("Radar");
        Assertions.assertEquals(student5, repository.changeStudentData(5, student5));
    }

    @SneakyThrows
    @Test
    @Order(7)
    void deleteStudentTest() {
        repository.deleteStudent(5);
        students.remove(4);
        Assertions.assertEquals(students, repository.getAllStudents());
        repository.createStudent(student5);
        students.add(student5);
    }

    @SneakyThrows
    @Test
    @Order(9)
    void getAllTeachersTest() {
        Assertions.assertEquals(teachers, repository.getAllTeachers());
    }

    @SneakyThrows
    @Test
    @Order(10)
    void getTeacherByIdTest() {
        Assertions.assertEquals(teacher2, repository.getTeacherById(2));
    }

    @SneakyThrows
    @Test
    @Order(11)
    void getTeachersByNameAndSurnameTest() {
        Assertions.assertEquals(List.of(teacher2), repository.getTeachersByNameAndSurname("Marry", "Roana"));
    }

    @SneakyThrows
    @Test
    @Order(8)
    void createTeacherTest() {
        repository.createTeacher(teacher1);
        repository.createTeacher(teacher2);
        Assertions.assertEquals(teacher3, repository.createTeacher(teacher3));
    }

    @SneakyThrows
    @Test
    @Order(12)
    void addSubjectsTest() {
        Assertions.assertEquals(Arrays.asList(PHYSICS, MATH, PHILOSOPHY), repository.addSubjects(3, List.of(PHILOSOPHY)).getSubjects());
    }

    @SneakyThrows
    @Test
    @Order(13)
    void deleteTeacherTest() {
        Assertions.assertEquals(teacher3, repository.deleteTeacher(3));
        repository.createTeacher(teacher3);
    }

    @Test
    @Order(15)
    void getAllGroupsTest() {
        Assertions.assertEquals(groups, repository.getAllGroups());
    }

    @SneakyThrows
    @Test
    @Order(16)
    void getGroupByNumberTest() {
        Assertions.assertEquals(group1, repository.getGroupByNumber("1142"));
    }

    @SneakyThrows
    @Test
    @Order(17)
    void getGroupsByNameAndSurnameTest() {
        Assertions.assertEquals(List.of(group1), repository.getGroupsByNameAndSurname("Jenny", "Law"));
    }

    @Test
    @Order(14)
    void createGroupTest() {
        repository.createGroup(group1);
        Assertions.assertEquals(group2, repository.createGroup(group2));
    }

    @SneakyThrows
    @Test
    @Order(18)
    void addStudentsToGroupTest() {
        Assertions.assertEquals(Arrays.asList(student1, student2, student3), repository.addStudentsToGroup("1142", List.of(3)).getStudents());
    }

    @SneakyThrows
    @Test
    @Order(19)
    void deleteGroupTest() {
        Assertions.assertEquals(group2, repository.deleteGroup("1145"));
        repository.createGroup(group2);
    }

    @Test
    @Order(21)
    void getAllTimetableTest() {
        Assertions.assertEquals(timetables, repository.getAllTimetable());
    }

    @SneakyThrows
    @Test
    @Order(22)
    void getTimetableByGroupNumberTest() {
        Assertions.assertEquals(Arrays.asList(timetable3, timetable4), repository.getTimetableByGroupNumber("1145"));
    }

    @SneakyThrows
    @Test
    @Order(23)
    void getTimetableByStudentTest() {
        Assertions.assertEquals(Arrays.asList(timetable1, timetable2), repository.getTimetableByStudent("Jenny", "Law"));
    }

    @SneakyThrows
    @Test
    @Order(24)
    void getTimetableByTeacherTest() {
        Assertions.assertEquals(Arrays.asList(timetable1, timetable3), repository.getTimetableByTeacher("John", "Key"));
    }

    @SneakyThrows
    @Test
    @Order(25)
    void getTimetableByTeacherTest2() {
        Assertions.assertEquals(List.of(timetable2), repository.getTimetableByTeacher(2));
    }

    @Test
    @Order(26)
    void getTimetableByDateTest() {
        Assertions.assertEquals(List.of(timetable1, timetable2, timetable3), repository.getTimetableByDate("11/07/2024"));
    }

    @Test
    @Order(20)
    void createTimetableTest() {
        repository.createTimetable(timetable1);
        repository.createTimetable(timetable2);
        repository.createTimetable(timetable3);
        Assertions.assertEquals(timetable4, repository.createTimetable(timetable4));
    }

    @Test
    @Order(27)
    void updateTimetableTest() {
        Assertions.assertEquals(List.of(timetable5) ,repository.updateTimetable("12/07/2024", List.of(timetable5)));
    }

    @Test
    @Order(28)
    void deleteTimetableByDateAndGroupNumberTest() {
        Assertions.assertEquals(List.of(timetable5), repository.deleteTimetableByDateAndGroupNumber("12/07/2024", "1145"));
    }

    @Test
    @Order(29)
    void deleteAllTimetableTest() {
        repository.deleteAllTimetable();
        Assertions.assertEquals(new ArrayList<Timetable>(), repository.getAllTimetable());
    }
}