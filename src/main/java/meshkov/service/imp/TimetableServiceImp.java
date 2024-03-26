package meshkov.service.imp;

import lombok.extern.slf4j.Slf4j;
import meshkov.checks.DateInTimetableCheck;
import meshkov.checks.GroupInTimeTableCheck;
import meshkov.checks.Middleware;
import meshkov.checks.TeacherInTimetableCheck;
import meshkov.consts.FileConstants;
import meshkov.exception.*;
import meshkov.model.Timetable;
import meshkov.repository.Repository;
import meshkov.service.TimetableService;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

@Slf4j
public class TimetableServiceImp implements TimetableService {

    private final Repository repository;
    private final int maxLessons;


    private final Middleware middleware;

    public TimetableServiceImp(Repository repository) {
        log.debug("constructor method invoked");
        this.repository = repository;

        Properties properties = new Properties();
        try (BufferedReader propertyReader = Files.newBufferedReader(Path.of(FileConstants.PROPERTY_FILE_NAME))) {
            properties.load(propertyReader);
            maxLessons = Integer.parseInt(properties.getProperty("maxLessons"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        middleware = Middleware.link(
                new DateInTimetableCheck(),
                new GroupInTimeTableCheck(repository),
                new TeacherInTimetableCheck(repository)
        );
    }

    @Override
    public List<Timetable> getAllTimetable() {
        log.debug("getAllTimetable method invoked");
        return repository.getAllTimetable();
    }

    @Override
    public List<Timetable> getTimetableByGroupNumber(String number) throws TimetableNotFoundException {
        log.debug("getTimetableByGroupNumber method invoked");
        log.debug("number = {}", number);
        return repository.getTimetableByGroupNumber(number);
    }

    @Override
    public List<Timetable> getTimetableByStudent(String name, String surname) throws StudentNotFoundException, GroupNotFoundException {
        log.debug("getTimetableByStudent method invoked");
        log.debug("name = {}, surname = {}", name, surname);
        return repository.getTimetableByStudent(name, surname);
    }

    @Override
    public List<Timetable> getTimetableByTeacher(String name, String surname) throws TeacherNotFoundException {
        log.debug("getTimetableByTeacher method invoked");
        log.debug("name = {}, surname = {}", name, surname);
        return repository.getTimetableByTeacher(name, surname);
    }

    @Override
    public List<Timetable> getTimetableByDate(String date) {
        log.debug("getTimetableByDate method invoked");
        log.debug("date = {}", date);
        return repository.getTimetableByDate(date);
    }

    @Override
    public Timetable createTimetable(Timetable timetable) throws TeacherNotFoundException, StudentNotFoundException, GroupNotFoundException, InvalidArgumentsException, TimetableNotFoundException, InvalidAmountException {
        log.debug("createTimetable method invoked");
        log.debug("timetable = {}", timetable);
        if (!middleware.check(timetable))
            throw new InvalidArgumentsException();

        DateTimeFormatter originFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm");
        LocalDate date = LocalDate.parse(timetable.getStartDateTime(), originFormat);

        DateTimeFormatter requiredFormat = DateTimeFormatter.ofPattern("d/MM/yyyy");
        String reqDate = requiredFormat.format(date);

        String groupNumber = timetable.getGroupNumber();
        long amount = repository.getTimetableByDate(reqDate).stream().filter(tb -> tb.getGroupNumber().equals(groupNumber)).count();

        if ((amount + 1) <= maxLessons)
            return repository.createTimetable(timetable);
        else
            throw new InvalidAmountException();
    }


    @Override
    public List<Timetable> updateTimetable(String date, List<Timetable> newTimetables) throws TeacherNotFoundException, StudentNotFoundException, GroupNotFoundException, InvalidArgumentsException, TimetableNotFoundException, InvalidAmountException {
        log.debug("TimetableServiceImp - updateTimetable method invoked");
        log.debug("date = {}, newTimetables = {}", date, newTimetables);

        for (Timetable newTimetable : newTimetables) {
            if (!middleware.check(newTimetable))
                throw new InvalidArgumentsException();
        }

        int count;
        HashMap<String, Integer> counts = new HashMap<>();
        for (Timetable newTimetable : newTimetables) {
            String groupNumber = newTimetable.getGroupNumber();
            count = counts.get(groupNumber) != null ? counts.get(groupNumber) : 0;
            counts.put(groupNumber, count + 1);


            if (counts.get(groupNumber) > maxLessons)
                throw new InvalidAmountException();
        }

        return repository.updateTimetable(date, newTimetables);
    }

    @Override
    public List<Timetable> deleteTimetableByDateAndGroupNumber(String date, String groupNumber) {
        log.debug("TimetableServiceImp - deleteTimetableByDateAndGroupNumber method invoked");
        log.debug("date = {}, groupNumber = {}", date, groupNumber);
        return repository.deleteTimetableByDateAndGroupNumber(date, groupNumber);
    }

    @Override
    public void deleteAllTimetable() {
        log.debug("TimetableServiceImp - deleteAllTimetable method invoked");
        repository.deleteAllTimetable();
    }
}