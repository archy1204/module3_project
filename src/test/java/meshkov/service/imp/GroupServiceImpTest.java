package meshkov.service.imp;

import lombok.SneakyThrows;
import meshkov.dto.GroupRequest;
import meshkov.exception.GroupNotFoundException;
import meshkov.exception.InvalidAmountException;
import meshkov.exception.InvalidArgumentsException;
import meshkov.mapper.GroupMapperImpl;
import meshkov.model.Group;
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

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class GroupServiceImpTest {

    GroupServiceImp groupService;

    @Mock
    SimpleRepository repository;

    static Group group;
    Student student1;
    Student student2;


    @BeforeEach
    void setGroup() {
        groupService = new GroupServiceImp(repository, new GroupMapperImpl());
        student1 = new Student(0, "John", "Key", "01.06.1999", "89080967456", null);
        student2 = new Student(1, "Jenny", "Law", "12.09.1995", "89080957823", null);
        group = new Group(0, "1142", List.of(student1, student2));
    }

    @SneakyThrows
    @Test
    @DisplayName("createGroup should return NewTeacher")
    void createGroupSuccessTest() {
        GroupRequest request = new GroupRequest("1142", null, List.of(student1, student2));
        when(repository.getGroupByNumber("1142")).thenThrow(GroupNotFoundException.class);
        when(repository.createGroup(Mockito.any())).thenReturn(group);
        Assertions.assertEquals(group, groupService.createGroup(request));
    }

    @SneakyThrows
    @Test
    @DisplayName("createGroup should throw Exception")
    void createGroupFailureTest() {
        GroupRequest request = new GroupRequest("1142", null, List.of(student1, student2));
        when(repository.getGroupByNumber("1142")).thenReturn(group);
        Assertions.assertThrows(InvalidArgumentsException.class, () -> groupService.createGroup(request));

        Student student3 = new Student(3, "Michael", "Warshtok", "26.10.1998", "89080967249", "1145");
        GroupRequest request1 = new GroupRequest("1142", null, List.of(student1, student2, student3));
        when(repository.getGroupByNumber("1142")).thenThrow(GroupNotFoundException.class);
        Assertions.assertThrows(InvalidArgumentsException.class, () -> groupService.createGroup(request1));

        if (groupService.minStudents > 1) {
            Student[] students = new Student[groupService.minStudents - 1];
            Arrays.fill(students, student2);
            GroupRequest request3 = new GroupRequest("1142", null, Arrays.asList(students));
            Assertions.assertThrows(InvalidAmountException.class, () -> groupService.createGroup(request3));
        }
    }

    @SneakyThrows
    @Test
    @DisplayName("addStudentsToGroup should return NewTeacher")
    void addStudentsToGroupSuccessTest() {
        Student student3 = new Student(2, "Kerri", "Malem", "23.01.2000", "89193412753", null);
        Group group1 = new Group(0, "1142", List.of(student1, student2, student3));
        when(repository.getGroupByNumber("1142")).thenReturn(group);
        when(repository.addStudentsToGroup("1142", List.of(2))).thenReturn(group1);
        Assertions.assertEquals(group1, groupService.addStudentsToGroup("1142", List.of(student3.getId())));
    }

    @SneakyThrows
    @Test
    @DisplayName("addStudentsToGroup should throw InvalidArgumentException")
    void addStudentsToGroupFailureTest() {
        Student student3 = new Student(2, "Kerri", "Malem", "23.01.2000", "89193412753", null);
        int id = student3.getId();
        when(repository.getGroupByNumber("1142")).thenThrow(GroupNotFoundException.class);
        Assertions.assertThrows(InvalidArgumentsException.class, () -> groupService.addStudentsToGroup("1142", List.of(id)));

        student3 = new Student(2, "Kerri", "Malem", "23.01.2000", "89193412753", "1443");
        doReturn(group).when(repository).getGroupByNumber("1142");
        when(repository.getStudentListById(List.of(id))).thenReturn(List.of(student3));
        Assertions.assertThrows(InvalidArgumentsException.class, () -> groupService.addStudentsToGroup("1142", List.of(id)));
    }

    @SneakyThrows
    @Test
    @DisplayName("addStudentsToGroup should throw InvalidAmountException")
    void addStudentsToGroupFailureTest2() {
        Student student3 = new Student(2, "Kerri", "Malem", "23.01.2000", "89193412753", null);
        int id = student3.getId();

        Student[] students = new Student[groupService.maxStudents - 1];
        Arrays.fill(students, student3);

        List<Integer> ids = Arrays.stream(students).map(Student::getId).toList();
        doReturn(group).when(repository).getGroupByNumber("1142");
        when(repository.getStudentListById(ids)).thenReturn(Arrays.asList(students));
        Assertions.assertThrows(InvalidAmountException.class, () -> groupService.addStudentsToGroup("1142", ids));
    }
}