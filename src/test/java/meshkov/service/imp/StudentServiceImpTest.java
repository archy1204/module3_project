package meshkov.service.imp;

import lombok.SneakyThrows;
import meshkov.dto.StudentRequest;
import meshkov.exception.InvalidArgumentsException;
import meshkov.mapper.StudentMapperImpl;
import meshkov.model.Student;
import meshkov.repository.imp.SimpleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class StudentServiceImpTest {

    static List<Student> students;
    StudentServiceImp studentService;

    @Mock
    SimpleRepository repository;

    static Student student1;
    static Student student2;
    static Student student3;
    static Student student4;
    static Student student5;

    @BeforeEach
    void setStudents() {
        studentService = new StudentServiceImp(repository, new StudentMapperImpl());
        students = new ArrayList<>();

        student1 = new Student(0, "John", "Key", "01.06.1999", "89080967456", "1142");
        student2 = new Student(1, "Jenny", "Law", "12.09.1995", "89080957823", "1142");
        student3 = new Student(2, "Kerri", "Malem", "23.01.2000", "89193412753", "1143");
        student4 = new Student(3, "Michael", "Warshtok", "26.10.1998", "89080967249", "1145");
        student5 = new Student(4, "Van", "Grat", "05.08.1997", "89110456523", "1145");

        students.add(student1);
        students.add(student2);
        students.add(student3);
        students.add(student4);
        students.add(student5);
    }

    @SneakyThrows
    @Test
    @DisplayName("CreateStudent should Return NewStudent")
    void CreateStudentSuccessTest() {
        StudentRequest request = new StudentRequest("John", "Key", "01.06.1999", "89080967456");
        when(repository.createStudent(Mockito.any())).thenReturn(student1);
        Assertions.assertEquals(student1, studentService.createStudent(request));
    }

    @SneakyThrows
    @Test
    @DisplayName("CreateStudent should Throw Exception")
    void CreateStudentFailureTest() {
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
    @DisplayName("ChangeStudentData should Return NewStudent")
    void ChangeStudentDataSuccessTest() {
        StudentRequest request = new StudentRequest("John", "Key", "01.06.1999", "89080967456");
        when(repository.changeStudentData(eq(0), Mockito.any())).thenReturn(student1);
        Assertions.assertEquals(student1, studentService.changeStudentData(0, request));
    }

    @SneakyThrows
    @Test
    @DisplayName("ChangeStudentData should Throw Exception")
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