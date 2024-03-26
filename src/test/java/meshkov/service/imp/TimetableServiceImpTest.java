package meshkov.service.imp;

import lombok.SneakyThrows;
import meshkov.exception.InvalidAmountException;
import meshkov.exception.InvalidArgumentsException;
import meshkov.exception.TimetableNotFoundException;
import meshkov.model.Group;
import meshkov.model.Student;
import meshkov.model.Timetable;
import meshkov.repository.imp.SimpleRepository;
import meshkov.service.TimetableService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class TimetableServiceImpTest {

    TimetableService timetableService;

    @Mock
    SimpleRepository repository;

    static Timetable timetable;
    static Group group;
    Student student1;
    Student student2;


    @BeforeEach
    void setTimetable() {
        timetableService = new TimetableServiceImp(repository);
        student1 = new Student(0, "John", "Key", "01.06.1999", "89080967456", null);
        student2 = new Student(1, "Jenny", "Law", "12.09.1995", "89080957823", null);
        group = new Group(0, "1142", List.of(student1, student2));
    }

    @SneakyThrows
    @Test
    @DisplayName("createTimetable should return NewTeacher")
    void createTimetableSuccessTest() {
        timetable = new Timetable("1142", 0, "2024-07-11 14:00", "2024-07-11 16:00");
        when(repository.getTimetableByGroupNumber("1142")).thenThrow(TimetableNotFoundException.class);
        when(repository.getTimetableByTeacher(0)).thenReturn(new ArrayList<>());
        when(repository.createTimetable(timetable)).thenReturn(timetable);
        Assertions.assertEquals(timetable, timetableService.createTimetable(timetable));
    }

    @SneakyThrows
    @Test
    @DisplayName("createTimetable should throw Exception")
    void createTimetableFailureTest() {
        timetable = new Timetable("1142", 0, "2024/07/11 14:00", "2024-07-11 16:00");
        Assertions.assertThrows(InvalidArgumentsException.class, () -> timetableService.createTimetable(timetable));

        timetable = new Timetable("1142", 0, "2024-07-11 14:00", "2024-07-11 16:00");
        Timetable timetable2 = new Timetable("1142", 0, "2024-07-11 14:00", "2024-07-11 16:00");

        when(repository.getTimetableByGroupNumber("1142")).thenReturn(List.of(timetable2));
        Assertions.assertThrows(InvalidArgumentsException.class, () -> timetableService.createTimetable(timetable));

        when(repository.getTimetableByGroupNumber("1142")).thenThrow(TimetableNotFoundException.class);
        when(repository.getTimetableByTeacher(0)).thenReturn(List.of(timetable2));
        Assertions.assertThrows(InvalidArgumentsException.class, () -> timetableService.createTimetable(timetable));

        when(repository.getTimetableByTeacher(0)).thenReturn(new ArrayList<>());
        when(repository.getTimetableByDate(Mockito.anyString())).thenReturn(Arrays.asList(timetable, timetable, timetable, timetable, timetable));
        Assertions.assertThrows(InvalidAmountException.class, () -> timetableService.createTimetable(timetable));
    }

    @SneakyThrows
    @Test
    @DisplayName("updateTimetable should return NewTeacher")
    void updateTimetableSuccessTest() {
        timetable = new Timetable("1142", 0, "2024-07-11 14:00", "2024-07-11 16:00");
        List<Timetable> timetables = List.of(timetable);

        when(repository.getTimetableByGroupNumber("1142")).thenThrow(TimetableNotFoundException.class);
        when(repository.getTimetableByTeacher(0)).thenReturn(new ArrayList<>());
        when(repository.updateTimetable("12/07/2024", timetables)).thenReturn(timetables);
        Assertions.assertEquals(timetables, timetableService.updateTimetable("12/07/2024", timetables));
    }

    @SneakyThrows
    @Test
    @DisplayName("updateTimetable should throw InvalidArgumentException")
    void updateTimetableFailureTest() {
        timetable = new Timetable("1142", 0, "2024/07/11 14:00", "2024-07-11 16:00");
        Assertions.assertThrows(InvalidArgumentsException.class, () -> timetableService.updateTimetable("12/07/2024", List.of(timetable)));

        timetable = new Timetable("1142", 0, "2024-07-11 14:00", "2024-07-11 16:00");
        Timetable timetable2 = new Timetable("1142", 0, "2024-07-11 14:00", "2024-07-11 16:00");

        when(repository.getTimetableByGroupNumber("1142")).thenReturn(List.of(timetable2));
        Assertions.assertThrows(InvalidArgumentsException.class, () -> timetableService.updateTimetable("12/07/2024", List.of(timetable)));

        when(repository.getTimetableByGroupNumber("1142")).thenThrow(TimetableNotFoundException.class);
        when(repository.getTimetableByTeacher(0)).thenReturn(List.of(timetable2));
        Assertions.assertThrows(InvalidArgumentsException.class, () -> timetableService.updateTimetable("12/07/2024", List.of(timetable)));

        when(repository.getTimetableByTeacher(0)).thenReturn(new ArrayList<>());
        Assertions.assertThrows(InvalidAmountException.class, () -> timetableService.updateTimetable("12/07/2024", Arrays.asList(timetable, timetable, timetable, timetable, timetable, timetable)));
    }
}