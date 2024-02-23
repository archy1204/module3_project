package meshkov.repository.imp;

import meshkov.consts.FileConstants;
import meshkov.exception.*;
import meshkov.model.*;
import meshkov.repository.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;

public class SimpleRepository implements Repository {

    private static final SimpleRepository INSTANCE = new SimpleRepository();
    private final CopyOnWriteArrayList<Student> students;
    private final CopyOnWriteArrayList<Teacher> teachers;
    private final CopyOnWriteArrayList<Group> groups;
    private final CopyOnWriteArrayList<Timetable> timetables;
    private final int minStudents;
    private final int maxStudents;
    private final int minLessons;
    private final int maxLessons;


    private SimpleRepository() {
        Properties properties = new Properties();
        try (BufferedReader propertyReader = Files.newBufferedReader(Path.of(FileConstants.PROPERTY_FILE_NAME))) {
            properties.load(propertyReader);
            minStudents = Integer.parseInt(properties.getProperty("minStudents"));
            maxStudents = Integer.parseInt(properties.getProperty("maxStudents"));
            minLessons = Integer.parseInt(properties.getProperty("minLessons"));
            maxLessons = Integer.parseInt(properties.getProperty("maxLessons"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.students = new CopyOnWriteArrayList<>();
        this.teachers = new CopyOnWriteArrayList<>();
        this.groups = new CopyOnWriteArrayList<>();
        this.timetables = new CopyOnWriteArrayList<>();
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
        group.getStudents().forEach(student -> student.setGroupNumber(group.getNumber()));
        groups.add(group);
        return group;
    }

    @Override
    public Group addStudentsToGroup(int id, List<Integer> studentsId) throws GroupNotFoundException, StudentNotFoundException {
        Group group = getGroupByNumber(id);
        List<Student> StudentsToAdd = new ArrayList<>();
        for (Integer st : studentsId) {
            StudentsToAdd.add(getStudentById(st));
        }
        group.getStudents().addAll(StudentsToAdd);
        StudentsToAdd.forEach(student -> student.setGroupNumber(group.getNumber()));
        return group;
    }

    @Override
    public Group deleteGroup(int id) throws GroupNotFoundException {
        Group groupToRemove = getGroupByNumber(id);
        groupToRemove.getStudents().forEach(student -> student.setGroupNumber(null));
        groups.remove(groupToRemove);
        return groupToRemove;
    }


    @Override
    public List<Timetable> getAllTimetable() {
        return new ArrayList<>(timetables);
    }

    @Override
    public List<Timetable> getTimetableByGroupNumber(int number) throws TimetableNotFoundException {
        List<Timetable> requiredTimetable = timetables.stream().filter(tb -> tb.getGroupNumber() == number).toList();
        if (requiredTimetable.isEmpty())
            throw new TimetableNotFoundException();
        else
            return requiredTimetable;
    }

    @Override
    public List<Timetable> getTimetableByStudent(String name, String surname) throws StudentNotFoundException, GroupNotFoundException {
        List<Group> requiredGroups = getGroupsByNameAndSurname(name, surname);
        List<Timetable> requiredTimetables = new ArrayList<>();
        for (Timetable timetable : timetables) {
            for (Group group : requiredGroups) {
                if (timetable.getGroupNumber() == Integer.parseInt(group.getNumber())) {
                    requiredTimetables.add(timetable);
                    break;
                }
            }
        }
        return requiredTimetables;
    }

    @Override
    public List<Timetable> getTimetableByTeacher(String name, String surname) throws TeacherNotFoundException {
        List<Teacher> requiredTeachers = getTeachersByNameAndSurname(name, surname);
        List<Timetable> requiredTimetables = new ArrayList<>();
        for (Timetable timetable : timetables) {
            for (Teacher teacher : requiredTeachers) {
                if (timetable.getTeacherId() == teacher.getId()) {
                    requiredTimetables.add(timetable);
                    break;
                }
            }
        }
        return requiredTimetables;
    }

    @Override
    public List<Timetable> getTimetableByDate(String date) {
        DateTimeFormatter requiredFormat = DateTimeFormatter.ofPattern("d/MM/yyyy");
        DateTimeFormatter timetableFormat = DateTimeFormatter.ofPattern("yyyy-MM-d H:mm");
        int requiredDayOfYear = LocalDate.parse(date, requiredFormat).getDayOfYear();
        return timetables.stream().filter(tb -> LocalDate.parse(tb.getStartDateTime(), timetableFormat).getDayOfYear() == requiredDayOfYear).toList();
    }

    @Override
    public Timetable createTimetable(Timetable timetable) {
        timetables.add(timetable);
        return timetable;
    }

    @Override
    public List<Timetable> updateTimetable(String date, List<Timetable> newTimetables) {
        List<Timetable> timetablesToRemove = getTimetableByDate(date);
        timetablesToRemove.forEach(timetables::remove);
        timetables.addAll(newTimetables);
        return newTimetables;
    }

    @Override
    public List<Timetable> deleteTimetableByDateAndGroupNumber(String date, int groupNumber) {
        List<Timetable> timetableToDelete = getTimetableByDate(date).stream().filter(tb -> tb.getGroupNumber() == groupNumber).toList();
        timetableToDelete.forEach(timetables::remove);
        return timetableToDelete;
    }

    @Override
    public void deleteAllTimetable() {
        timetables.clear();
    }
}
