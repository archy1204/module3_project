package meshkov.service;

import meshkov.exception.*;
import meshkov.model.Timetable;

import java.util.List;

public interface TimetableService {

    List<Timetable> getAllTimetable();

    List<Timetable> getTimetableByGroupNumber(String number) throws TimetableNotFoundException;

    List<Timetable> getTimetableByStudent(String name, String surname) throws StudentNotFoundException, GroupNotFoundException;

    List<Timetable> getTimetableByTeacher(String name, String surname) throws TeacherNotFoundException;

    List<Timetable> getTimetableByDate(String date);

    Timetable createTimetable(Timetable timetable) throws TeacherNotFoundException, StudentNotFoundException, GroupNotFoundException, InvalidArgumentsException, TimetableNotFoundException, InvalidAmountException;

    public List<Timetable> updateTimetable(String date, List<Timetable> newTimetables) throws TeacherNotFoundException, StudentNotFoundException, GroupNotFoundException, InvalidArgumentsException, TimetableNotFoundException, InvalidAmountException;

    List<Timetable> deleteTimetableByDateAndGroupNumber(String date, String groupNumber);

    public void deleteAllTimetable();
}
