package meshkov.service.imp;

import lombok.SneakyThrows;
import meshkov.dto.TeacherRequest;
import meshkov.exception.InvalidArgumentsException;
import meshkov.mapper.TeacherMapperImpl;
import meshkov.model.Teacher;
import meshkov.repository.imp.SimpleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static meshkov.model.Subject.*;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class TeacherServiceImpTest {

    TeacherServiceImp teacherService;

    @Mock
    SimpleRepository repository;

    static Teacher teacher;

    @BeforeEach
    void setTeacher() {
        teacherService = new TeacherServiceImp(repository, new TeacherMapperImpl());
        teacher = new Teacher(0, "John", "Key", Arrays.asList(ENGLISH, HISTORY, SOCIOLOGY), 5);
    }

    @SneakyThrows
    @Test
    @DisplayName("createTeacher should teturn NewTeacher")
    void createTeacherSuccessTest() {
        TeacherRequest request = new TeacherRequest("John", "Key", Arrays.asList(ENGLISH, HISTORY, SOCIOLOGY), 5);
        when(repository.createTeacher(Mockito.any())).thenReturn(teacher);
        Assertions.assertEquals(teacher, teacherService.createTeacher(request));
    }

    @SneakyThrows
    @Test
    @DisplayName("createTeacher should throw Exception")
    void createTeacherFailureTest() {
        TeacherRequest request = new TeacherRequest("Joh1n", "Key", Arrays.asList(ENGLISH, HISTORY, SOCIOLOGY), 5);
        TeacherRequest finalRequest = request;
        Assertions.assertThrows(InvalidArgumentsException.class, () -> teacherService.createTeacher(finalRequest));

        request = new TeacherRequest("John", "K_ey", Arrays.asList(ENGLISH, HISTORY, SOCIOLOGY), 5);
        TeacherRequest finalRequest1 = request;
        Assertions.assertThrows(Exception.class, () -> teacherService.createTeacher(finalRequest1));
    }

    @SneakyThrows
    @Test
    @DisplayName("addSubjects should return NewTeacher")
    void addSubjectsSuccessTest() {
        when(repository.getTeacherById(0)).thenReturn(teacher);
        Teacher teacher1 = new Teacher(0, "John", "Key", Arrays.asList(ENGLISH, HISTORY, SOCIOLOGY, PHILOSOPHY), 5);
        when(repository.addSubjects(0, List.of(PHILOSOPHY))).thenReturn(teacher1);
        Assertions.assertEquals(teacher1, teacherService.addSubjects(0, List.of(PHILOSOPHY)));
    }

    @SneakyThrows
    @Test
    @DisplayName("addSubjects should throw Exception")
    void addSubjectsFailureTest() {
        Teacher teacher1 = new Teacher(0, "John", "Key", Arrays.asList(ENGLISH, HISTORY, SOCIOLOGY, PHILOSOPHY), 5);
        when(repository.getTeacherById(0)).thenReturn(teacher1);
        Assertions.assertThrows(InvalidArgumentsException.class, () -> teacherService.addSubjects(0, List.of(PHILOSOPHY)));
    }
}