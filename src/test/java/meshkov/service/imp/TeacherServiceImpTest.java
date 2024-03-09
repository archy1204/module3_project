package meshkov.service.imp;

import lombok.SneakyThrows;
import meshkov.dto.StudentRequest;
import meshkov.dto.TeacherRequest;
import meshkov.exception.InvalidArgumentsException;
import meshkov.mapper.StudentMapperImpl;
import meshkov.model.Teacher;
import meshkov.repository.imp.SimpleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static meshkov.model.Subject.ENGLISH;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class TeacherServiceImpTest {

    static List<Teacher> teachers;
    StudentServiceImp studentService;

    @Mock
    SimpleRepository repository;

    static Teacher teacher1;
    static Teacher teacher2;
    static Teacher teacher3;
    static Teacher teacher4;
    static Teacher teacher5;

    @BeforeEach
    void setStudents() {
        studentService = new StudentServiceImp(repository, new StudentMapperImpl());
        teachers = new ArrayList<>();

        teacher1 = new Teacher(0, "John", "Key", Arrays.asList(ENGLISH), );
        teacher2 = new Teacher(1, "Jenny", "Law", "12.09.1995", "89080957823", "1142");
        teacher3 = new Teacher(2, "Kerri", "Malem", "23.01.2000", "89193412753", "1143");
        teacher4 = new Teacher(3, "Michael", "Warshtok", "26.10.1998", "89080967249", "1145");
        teacher5 = new Teacher(4, "Van", "Grat", "05.08.1997", "89110456523", "1145");

        teachers.add(teacher1);
        teachers.add(teacher2);
        teachers.add(teacher3);
        teachers.add(teacher4);
        teachers.add(teacher5);
    }

    @SneakyThrows
    @Test
    @DisplayName("createTeacher should Return NewStudent")
    void createTeacherSuccessTest() {
        TeacherRequest request = new TeacherRequest("John", "Key", "01.06.1999", "89080967456");
        when(repository.createStudent(Mockito.any())).thenReturn(student1);
        Assertions.assertEquals(student1, studentService.createStudent(request));
    }

    @SneakyThrows
    @Test
    @DisplayName("createTeacher should Throw Exception")
    void createTeacherFailureTest() {
        StudentRequest request = new StudentRequest("John", "Key", "01.06.1999", "345678");
        StudentRequest finalRequest = request;
        Assertions.assertThrows(InvalidArgumentsException.class, () ->  studentService.createStudent(finalRequest));

        request = new StudentRequest("John", "Key", "01/06/1999", "89080967456");
        StudentRequest finalRequest1 = request;
        Assertions.assertThrows(Exception.class, () ->  studentService.createStudent(finalRequest1));

        request = new StudentRequest("John", "K5ey", "01.06.1999", "89080967456");
        StudentRequest finalRequest2 = request;
        Assertions.assertThrows(InvalidArgumentsException.class, () ->  studentService.createStudent(finalRequest2));

        request = new StudentRequest("John", "Key", "01.06.2025", "89080967456");
        StudentRequest finalRequest3 = request;
        Assertions.assertThrows(InvalidArgumentsException.class, () ->  studentService.createStudent(finalRequest3));
    }

    @SneakyThrows
    @Test
    @DisplayName("addSubjects should Return NewStudent")
    void addSubjectsSuccessTest() {
        StudentRequest request = new StudentRequest("John", "Key", "01.06.1999", "89080967456");
        when(repository.changeStudentData(eq(0), Mockito.any())).thenReturn(student1);
        Assertions.assertEquals(student1, studentService.changeStudentData(0, request));
    }

    @SneakyThrows
    @Test
    @DisplayName("addSubjects should Throw Exception")
    void shouldThrowExceptionFailureTest() {
        StudentRequest request = new StudentRequest("John", "Key", "01.06.1999", "345678");
        StudentRequest finalRequest = request;
        Assertions.assertThrows(InvalidArgumentsException.class, () ->  studentService.createStudent(finalRequest));

        request = new StudentRequest("John", "Key", "01/06/1999", "89080967456");
        StudentRequest finalRequest1 = request;
        Assertions.assertThrows(Exception.class, () ->  studentService.createStudent(finalRequest1));

        request = new StudentRequest("John", "K5ey", "01.06.1999", "89080967456");
        StudentRequest finalRequest2 = request;
        Assertions.assertThrows(InvalidArgumentsException.class, () ->  studentService.createStudent(finalRequest2));

        request = new StudentRequest("John", "Key", "01.06.2025", "89080967456");
        StudentRequest finalRequest3 = request;
        Assertions.assertThrows(InvalidArgumentsException.class, () ->  studentService.createStudent(finalRequest3));
    }
}