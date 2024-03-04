package meshkov.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import meshkov.consts.FileConstants;
import meshkov.dto.ErrorResponse;
import meshkov.exception.InvalidAmountException;
import meshkov.exception.InvalidArgumentsException;
import meshkov.exception.InvalidRequestException;
import meshkov.exception.JsonParseException;
import meshkov.model.Timetable;
import meshkov.repository.Repository;
import meshkov.repository.imp.SimpleRepository;
import meshkov.service.JsonService;
import meshkov.service.TimetableService;
import meshkov.service.imp.JsonServiceImp;
import meshkov.service.imp.TimetableServiceImp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

@Slf4j
@WebServlet(name = "TimetableServlet", urlPatterns = "/timetable/*")
public class TimetableServlet extends HttpServlet {

    private JsonService jsonService;
    private TimetableService timetableService;
    private int maxLessons;


    @Override
    public void init() throws ServletException {
        log.debug("init method invoked");
        super.init();
        jsonService = new JsonServiceImp();
        timetableService = new TimetableServiceImp(SimpleRepository.getInstance());

        Properties properties = new Properties();
        try (BufferedReader propertyReader = Files.newBufferedReader(Path.of(FileConstants.PROPERTY_FILE_NAME))) {
            properties.load(propertyReader);
            maxLessons = Integer.parseInt(properties.getProperty("maxLessons"));
        } catch (IOException e) {
            log.error("Exception " + e.getClass().getSimpleName() + " was thrown. Error - " + new ErrorResponse(e.getClass().getSimpleName()));
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        log.debug("doGet method invoked");
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        String jsonResponse;

        String pathInfo = req.getPathInfo();
        String queryString = req.getQueryString();
        try {
            if (pathInfo == null && queryString == null) {
                List<Timetable> responseList = timetableService.getAllTimetable();
                jsonResponse = jsonService.createJson(responseList);
                resp.setStatus(200);
                out.println(jsonResponse);
                out.flush();
            } else {
                String groupNumber = req.getParameter("groupNumber");
                String date = req.getParameter("date");
                String studentName = req.getParameter("studentName");
                String studentSurname = req.getParameter("studentSurname");
                String teachedName = req.getParameter("teachedName");
                String teachedSurname = req.getParameter("teachedSurname");
                List<Timetable> requiredTimetable;
                if (groupNumber != null) {
                    log.debug("groupNumber = {}", groupNumber);
                    requiredTimetable = timetableService.getTimetableByGroupNumber(groupNumber);
                } else if (date != null) {
                    log.debug("date = {}", date);
                    requiredTimetable = timetableService.getTimetableByDate(date);
                } else if (studentName != null && studentSurname != null) {
                    log.debug("studentName = {}, studentSurname = {}", studentName, studentSurname);
                    requiredTimetable = timetableService.getTimetableByStudent(studentName, studentSurname);
                } else if (teachedName != null && teachedSurname != null) {
                    log.debug("teachedName = {}, teachedSurname = {}", teachedName, teachedSurname);
                    requiredTimetable = timetableService.getTimetableByTeacher(teachedName, teachedSurname);
                } else {
                    throw new InvalidRequestException();
                }

                out.println(jsonService.createJson(requiredTimetable));
                out.flush();
                resp.setStatus(200);
            }
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getClass().getSimpleName());
            log.error("Exception " + e.getClass().getSimpleName() + " was thrown. Error - " + errorResponse.getErrorId());
            out.println(errorResponse);
            out.flush();
            resp.setStatus(400);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        log.debug("doPost method invoked");
        resp.setContentType("application/json");
        String reqBody = req.getReader().lines().collect(Collectors.joining());
        PrintWriter out = resp.getWriter();
        try {
            Timetable timetable = (Timetable) jsonService.createObject(reqBody, Timetable.class);
            out.println(jsonService.createJson(timetableService.createTimetable(timetable)));
            out.flush();
            resp.setStatus(201);
        } catch (InvalidArgumentsException e) {
            ErrorResponse errorResponse = new ErrorResponse("Wrong parameters");
            log.error("Exception " + e.getClass().getSimpleName() + " was thrown. Error - " + errorResponse.getErrorId());
            out.println(errorResponse);
            out.flush();
            resp.setStatus(400);
        } catch (InvalidAmountException e) {
            ErrorResponse errorResponse = new ErrorResponse("Wrong amount of students. It must be fewer than " + maxLessons);
            log.error("Exception " + e.getClass().getSimpleName() + " was thrown. Error - " + errorResponse.getErrorId());
            out.println(errorResponse);
            out.flush();
            resp.setStatus(400);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getClass().getSimpleName());
            log.error("Exception " + e.getClass().getSimpleName() + " was thrown. Error - " + errorResponse.getErrorId());
            out.println(errorResponse);
            out.flush();
            resp.setStatus(400);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        log.debug("doPut method invoked");
        String reqBody = req.getReader().lines().collect(Collectors.joining());
        PrintWriter out = resp.getWriter();

        String date = req.getParameter("date");
        log.debug("date = {}", date);

        try {
            List<Timetable> newTimetable = (List<Timetable>) jsonService.createObject(reqBody, List.class);
            List<Timetable> timetable = timetableService.updateTimetable(date, newTimetable); // убрать ненужную переменную
            out.println(jsonService.createJson(timetable));
            out.flush();
            resp.setStatus(204);
        } catch (InvalidArgumentsException e) {
            ErrorResponse errorResponse = new ErrorResponse("Wrong parameters");
            log.error("Exception " + e.getClass().getSimpleName() + " was thrown. Error - " + errorResponse.getErrorId());
            out.println(errorResponse);
            out.flush();
            resp.setStatus(400);
        } catch (InvalidAmountException e) {
            ErrorResponse errorResponse = new ErrorResponse("Wrong amount of students. It must be fewer than " + maxLessons);
            log.error("Exception " + e.getClass().getSimpleName() + " was thrown. Error - " + errorResponse.getErrorId());
            out.println(errorResponse);
            out.flush();
            resp.setStatus(400);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getClass().getSimpleName());
            log.error("Exception " + e.getClass().getSimpleName() + " was thrown. Error - " + errorResponse.getErrorId());
            out.println(errorResponse);
            out.flush();
            resp.setStatus(400);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        log.debug("doDelete method invoked");
        PrintWriter out = resp.getWriter();

        String date = req.getParameter("date");
        String groupNumber = req.getParameter("groupNumber");
        log.debug("date = {}, groupNumber = {}", date, groupNumber);

        try {
            if (date == null && groupNumber == null)
                timetableService.deleteAllTimetable();
            else {
                out.println(jsonService.createJson(timetableService.deleteTimetableByDateAndGroupNumber(date, groupNumber)));
                resp.setStatus(200);
                out.flush();
            }
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getClass().getSimpleName());
            log.error("Exception " + e.getClass().getSimpleName() + " was thrown. Error - " + errorResponse.getErrorId());
            out.println(errorResponse);
            out.flush();
            resp.setStatus(400);
        }
    }
}
