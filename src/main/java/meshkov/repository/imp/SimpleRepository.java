package meshkov.repository.imp;

import lombok.extern.slf4j.Slf4j;
import meshkov.exception.*;
import meshkov.model.*;
import meshkov.repository.Repository;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
public class SimpleRepository implements Repository {

    private static final SimpleRepository INSTANCE = new SimpleRepository();
    private final CopyOnWriteArrayList<Student> students;
    private final CopyOnWriteArrayList<Teacher> teachers;
    private final CopyOnWriteArrayList<Group> groups;
    private final CopyOnWriteArrayList<Timetable> timetables;

    private SimpleRepository() {
        log.debug("SimpleRepository - constructor method invoked");
        this.students = new CopyOnWriteArrayList<>();
        this.teachers = new CopyOnWriteArrayList<>();
        this.groups = new CopyOnWriteArrayList<>();
        this.timetables = new CopyOnWriteArrayList<>();
    }

    public static SimpleRepository getInstance() {
        log.debug("SimpleRepository - getInstance method invoked");
        return INSTANCE;
    }

    @Override
    public List<Student> getAllStudents() {
        log.debug("SimpleRepository - getAllStudents method invoked");
        return new ArrayList<Student>(students);
    }

    @Override
    public List<Student> getStudentListById(List<Integer> ids) throws StudentNotFoundException {
        log.debug("SimpleRepository - getStudentListById method invoked");
        log.debug("ids = {}", ids);
        List<Student> list = new ArrayList<>();
        for (Integer id : ids) {
            Student studentById = getStudentById(id);
            list.add(studentById);
        }
        return list;
    }

    @Override
    public Student getStudentById(int id) throws StudentNotFoundException {
        log.debug("SimpleRepository - getStudentById method invoked");
        log.debug("id = {}", id);
        return students.stream().filter(st -> st.getId() == id).findFirst().orElseThrow(StudentNotFoundException::new);
    }

    @Override
    public List<Student> getStudentsByNameAndSurname(String name, String surname) throws StudentNotFoundException {
        log.debug("SimpleRepository - getStudentsByNameAndSurname method invoked");
        log.debug("name = {}, surname = {}", name, surname);
        List<Student> requiredList = students.stream().filter(st -> st.getName().equals(name) && st.getSurname().equals(surname)).toList();
        if (requiredList.isEmpty())
            throw new StudentNotFoundException();
        else
            return requiredList;
    }

    @Override
    public Student createStudent(Student student) throws JsonParseException, IOException {
        log.debug("SimpleRepository - createStudent method invoked");
        log.debug("student = {}", student);
        student.setId(students.size() + 1);
        students.add(student);
        return student;
    }

    @Override
    public Student changeStudentData(int id, Student studentUpdate) throws StudentNotFoundException {
        log.debug("SimpleRepository - changeStudentData method invoked");
        log.debug("id = {}, studentUpdate = {}", id, studentUpdate);
        Student student = getStudentById(id);
        student.setName(studentUpdate.getName());
        student.setSurname(studentUpdate.getSurname());
        student.setBirthday(studentUpdate.getBirthday());
        student.setPhoneNumber(studentUpdate.getPhoneNumber());
        return student;
    }

    @Override
    public Student deleteStudent(int id) throws StudentNotFoundException {
        log.debug("SimpleRepository - deleteStudent method invoked");
        log.debug("id = {}", id);
        Student studentToRemove = getStudentById(id);
        students.remove(studentToRemove);
        return studentToRemove;
    }

    @Override
    public List<Teacher> getAllTeachers() {
        log.debug("SimpleRepository - getAllTeachers method invoked");
        return new ArrayList<Teacher>(teachers);
    }

    @Override
    public Teacher getTeacherById(int id) throws TeacherNotFoundException {
        log.debug("SimpleRepository - getTeacherById method invoked");
        log.debug("id = {}", id);
        return teachers.stream().filter(tc -> tc.getId() == id).findFirst().orElseThrow(TeacherNotFoundException::new);
    }

    @Override
    public List<Teacher> getTeachersByNameAndSurname(String name, String surname) throws TeacherNotFoundException {
        log.debug("SimpleRepository - getTeachersByNameAndSurname method invoked");
        log.debug("name = {}, surname = {}", name, surname);
        List<Teacher> requiredList = teachers.stream().filter(st -> st.getName().equals(name) && st.getSurname().equals(surname)).toList();
        if (requiredList.isEmpty())
            throw new TeacherNotFoundException();
        else
            return requiredList;
    }

    @Override
    public Teacher createTeacher(Teacher teacher) {
        log.debug("SimpleRepository - createTeacher method invoked");
        log.debug("teacher = {}", teacher);
        teacher.setId(teachers.size() + 1);
        teachers.add(teacher);
        return teacher;
    }

    @Override
    public Teacher addSubjects(int id, List<Subject> subjects) throws TeacherNotFoundException {
        log.debug("SimpleRepository - addSubjects method invoked");
        log.debug("id = {}, subjects = {}", id, subjects);
        Teacher teacher = getTeacherById(id);
        teacher.getSubjects().addAll(subjects);
        return teacher;
    }

    @Override
    public Teacher deleteTeacher(int id) throws TeacherNotFoundException {
        log.debug("SimpleRepository - deleteTeacher method invoked");
        log.debug("id = {}", id);
        Teacher teacherToRemove = getTeacherById(id);
        teachers.remove(teacherToRemove);
        return teacherToRemove;
    }

    @Override
    public List<Group> getAllGroups() {
        log.debug("SimpleRepository - getAllGroups method invoked");
        return new ArrayList<>(groups);
    }

    @Override
    public Group getGroupByNumber(String number) throws GroupNotFoundException {
        log.debug("SimpleRepository - getGroupByNumber method invoked");
        log.debug("id = {}", number);
        return groups.stream().filter(gr -> gr.getNumber().equals(number)).findFirst().orElseThrow(GroupNotFoundException::new);
    }

    @Override
    public List<Group> getGroupsByNameAndSurname(String name, String surname) throws StudentNotFoundException, GroupNotFoundException {
        log.debug("SimpleRepository - getGroupsByNameAndSurname method invoked");
        log.debug("name = {}, surname = {}", name, surname);
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
        log.debug("SimpleRepository - createGroup method invoked");
        log.debug("timetable = {}", group);
        group.setId(groups.size() + 1);
        group.getStudents().forEach(student -> student.setGroupNumber(group.getNumber()));
        groups.add(group);
        return group;
    }

    @Override
    public Group addStudentsToGroup(String number, List<Integer> studentsId) throws GroupNotFoundException, StudentNotFoundException {
        log.debug("SimpleRepository - addStudentsToGroup method invoked");
        log.debug("number = {}, studentsId = {}", number, studentsId);
        Group group = getGroupByNumber(number);
        List<Student> StudentsToAdd = new ArrayList<>();
        for (Integer st : studentsId) {
            StudentsToAdd.add(getStudentById(st));
        }
        group.getStudents().addAll(StudentsToAdd);
        StudentsToAdd.forEach(student -> student.setGroupNumber(group.getNumber()));
        return group;
    }

    @Override
    public Group deleteGroup(String number) throws GroupNotFoundException {
        log.debug("SimpleRepository - deleteGroup method invoked");
        log.debug("id = {}", number);
        Group groupToRemove = getGroupByNumber(number);
        groupToRemove.getStudents().forEach(student -> student.setGroupNumber(null));
        groups.remove(groupToRemove);
        return groupToRemove;
    }


    @Override
    public List<Timetable> getAllTimetable() {
        log.debug("SimpleRepository - getAllTimetable method invoked");
        return new ArrayList<>(timetables);
    }

    @Override
    public List<Timetable> getTimetableByGroupNumber(String number) throws TimetableNotFoundException {
        log.debug("SimpleRepository - getTimetableByGroupNumber method invoked");
        log.debug("number = {}", number);
        List<Timetable> requiredTimetable = timetables.stream().filter(tb -> tb.getGroupNumber().equals(number)).toList();
        if (requiredTimetable.isEmpty())
            throw new TimetableNotFoundException();
        else
            return requiredTimetable;
    }

    @Override
    public List<Timetable> getTimetableByStudent(String name, String surname) throws StudentNotFoundException, GroupNotFoundException {
        log.debug("SimpleRepository - getTimetableByStudent method invoked");
        log.debug("name = {}, surname = {}", name, surname);
        List<Group> requiredGroups = getGroupsByNameAndSurname(name, surname);
        List<Timetable> requiredTimetables = new ArrayList<>();
        for (Timetable timetable : timetables) {
            for (Group group : requiredGroups) {
                if (timetable.getGroupNumber().equals(group.getNumber())) {
                    requiredTimetables.add(timetable);
                    break;
                }
            }
        }
        return requiredTimetables;
    }

    @Override
    public List<Timetable> getTimetableByTeacher(String name, String surname) throws TeacherNotFoundException {
        log.debug("SimpleRepository - getTimetableByTeacher method invoked");
        log.debug("name = {}, surname = {}", name, surname);
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
    public List<Timetable> getTimetableByTeacher(int id) throws TeacherNotFoundException {
        log.debug("SimpleRepository - getTimetableByTeacher method invoked");
        log.debug("id = {}", id);
        Teacher requiredTeacher = getTeacherById(id);
        List<Timetable> requiredTimetables = new ArrayList<>();
        int teacherId = requiredTeacher.getId();
        for (Timetable timetable : timetables) {
            if (timetable.getTeacherId() == teacherId)
                requiredTimetables.add(timetable);
        }
        return requiredTimetables;
    }

    @Override
    public List<Timetable> getTimetableByDate(String date) {
        log.debug("SimpleRepository - getTimetableByDate method invoked");
        log.debug("date = {}", date);
        DateTimeFormatter requiredFormat = DateTimeFormatter.ofPattern("d/MM/yyyy");
        DateTimeFormatter timetableFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm");
        int requiredDayOfYear = LocalDate.parse(date, requiredFormat).getDayOfYear();
        return timetables.stream().filter(tb -> LocalDate.parse(tb.getStartDateTime(), timetableFormat).getDayOfYear() == requiredDayOfYear).toList();
    }

    @Override
    public Timetable createTimetable(Timetable timetable) {
        log.debug("SimpleRepository - createTimetable method invoked");
        log.debug("timetable = {}", timetable);
        timetables.add(timetable);
        return timetable;
    }

    @Override
    public List<Timetable> updateTimetable(String date, List<Timetable> newTimetables) {
        log.debug("SimpleRepository - updateTimetable method invoked");
        log.debug("date = {}, newTimetables = {}", date, newTimetables);
        List<Timetable> timetablesToRemove = getTimetableByDate(date);
        timetablesToRemove.forEach(timetables::remove);
        timetables.addAll(newTimetables);
        return newTimetables;
    }

    @Override
    public List<Timetable> deleteTimetableByDateAndGroupNumber(String date, String groupNumber) {
        log.debug("SimpleRepository - deleteTimetableByDateAndGroupNumber method invoked");
        log.debug("date = {}, groupNumber = {}", date, groupNumber);
        List<Timetable> timetableToDelete = getTimetableByDate(date).stream().filter(tb -> tb.getGroupNumber().equals(groupNumber)).toList();
        timetableToDelete.forEach(timetables::remove);
        return timetableToDelete;
    }

    @Override
    public void deleteAllTimetable() {
        log.debug("SimpleRepository - deleteAllTimetable method invoked");
        timetables.clear();
    }

    public void clearDB() {
        students.clear();
        teachers.clear();
        groups.clear();
        timetables.clear();
    }
}
