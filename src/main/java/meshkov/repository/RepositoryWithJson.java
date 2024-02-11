package meshkov.repository;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import meshkov.consts.FileConstants;
import meshkov.exception.JsonParseException;
import meshkov.exception.StudentNotFoundException;
import meshkov.model.Group;
import meshkov.model.Student;
import meshkov.service.JsonService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class RepositoryWithJson implements Repository {
    @JsonDeserialize(contentAs = Student.class)
    private final CopyOnWriteArrayList<Student> students;

    @JsonDeserialize(contentAs = Student.class)
    private final CopyOnWriteArrayList<Group> groups;

    private JsonService jsonService;

    Path studentFile = Path.of(FileConstants.STUDENT_JSON_FILE);
    Path groupFile = Path.of(FileConstants.GROUP_JSON_FILE);

    public RepositoryWithJson(JsonService jsonService) throws IOException, JsonParseException {
        this.jsonService = jsonService;
        if (Files.exists(studentFile)) {
            String json = Files.readString(studentFile);
            students = (CopyOnWriteArrayList<Student>) jsonService.createObject(json, CopyOnWriteArrayList.class);
        } else {
            students = new CopyOnWriteArrayList<Student>();
            Files.createFile(studentFile);
        }

        if (Files.exists(groupFile)) {
            String json = Files.readString(groupFile);
            groups = (CopyOnWriteArrayList<Group>) jsonService.createObject(json, CopyOnWriteArrayList.class);
        } else {
            groups = new CopyOnWriteArrayList<Group>();
            Files.createFile(studentFile);
        }
    }

    @Override
    public List<Student> getAllStudents() {
        return new CopyOnWriteArrayList<Student>(students);
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
}
