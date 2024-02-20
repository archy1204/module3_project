package meshkov.repository.imp;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import meshkov.consts.FileConstants;
import meshkov.exception.GroupNotFoundException;
import meshkov.exception.JsonParseException;
import meshkov.exception.StudentNotFoundException;
import meshkov.exception.TeacherNotFoundException;
import meshkov.model.Group;
import meshkov.model.Student;
import meshkov.model.Subject;
import meshkov.model.Teacher;
import meshkov.repository.Repository;
import meshkov.service.JsonService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class RepositoryWithJson implements Repository {
    @JsonDeserialize(contentAs = CopyOnWriteArrayList.class)
    private final CopyOnWriteArrayList<Student> students;

    //@JsonDeserialize(contentAs = Group.class)
    private final CopyOnWriteArrayList<Group> groups;

    private JsonService jsonService;

    Path studentFile = Path.of(FileConstants.STUDENT_JSON_FILE);
    Path groupFile = Path.of(FileConstants.GROUP_JSON_FILE);

    public RepositoryWithJson(JsonService jsonService) throws IOException, JsonParseException {
        this.jsonService = jsonService;
        String json;

        if (!Files.exists(studentFile))
            Files.createFile(studentFile);

        if (!Files.exists(groupFile))
            Files.createFile(groupFile);

        if (!(json = Files.readString(studentFile)).equals(""))
            students = (CopyOnWriteArrayList<Student>) jsonService.createObject(json, CopyOnWriteArrayList.class);
        else
            students = new CopyOnWriteArrayList<Student>();


        if (!(json = Files.readString(groupFile)).equals(""))
            groups = (CopyOnWriteArrayList<Group>) jsonService.createObject(json, CopyOnWriteArrayList.class);
        else
            groups = new CopyOnWriteArrayList<Group>();

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
        Files.writeString(studentFile, jsonService.createJson(students));
        return student;
    }

    @Override
    public Student changeStudentData(int id, Student student) {
        return null;
    }

    @Override
    public Student deleteStudent(int id) throws StudentNotFoundException {
        return null;
    }

    @Override
    public List<Teacher> getAllTeachers() {
        return null;
    }

    @Override
    public Teacher getTeacherById(int id) throws TeacherNotFoundException {
        return null;
    }

    @Override
    public Teacher createTeacher(Teacher teacher) {
        return null;
    }

    @Override
    public List<Teacher> getTeachersByNameAndSurname(String name, String surname) throws TeacherNotFoundException {
        return null;
    }

    @Override
    public Teacher addSubjects(int id, List<Subject> subjects) throws TeacherNotFoundException {
        return null;
    }

    @Override
    public Teacher deleteTeacher(int id) throws TeacherNotFoundException {
        return null;
    }

    @Override
    public List<Group> getAllGroups() {
        return null;
    }

    @Override
    public Group getGroupByNumber(int id) throws GroupNotFoundException {
        return null;
    }

    @Override
    public List<Group> getGroupsByNameAndSurname(String name, String surname) throws StudentNotFoundException, GroupNotFoundException {
        return null;
    }

    @Override
    public Group createGroup(Group group) {
        return null;
    }

    @Override
    public Group addStudentsToGroup(int id, List<Integer> studentsId) throws GroupNotFoundException, StudentNotFoundException {
        return null;
    }

    @Override
    public Group deleteGroup(int id) throws GroupNotFoundException {
        return null;
    }
}
