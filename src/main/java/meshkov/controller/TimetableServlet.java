package meshkov.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import meshkov.exception.InvalidRequestException;
import meshkov.exception.JsonParseException;
import meshkov.model.Timetable;
import meshkov.repository.Repository;
import meshkov.repository.imp.SimpleRepository;
import meshkov.service.JsonService;
import meshkov.service.imp.JsonServiceImp;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@WebServlet(name = "TimetableServlet", urlPatterns = "/timetable/*")
public class TimetableServlet extends HttpServlet {
    private JsonService jsonService;
    private Repository repository;

    @Override
    public void init() throws ServletException {
        super.init();
        jsonService = new JsonServiceImp();
        repository = SimpleRepository.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        String jsonResponse;

        String pathInfo = req.getPathInfo();
        String queryString = req.getQueryString();

        if (pathInfo == null && queryString == null) {
            List<Timetable> responseList = repository.getAllTimetable();
            try {
                jsonResponse = jsonService.createJson(responseList);
                resp.setStatus(200);
            } catch (JsonParseException e) {
                jsonResponse = e.getMessage();
                resp.setStatus(400);
            }
            out.println(jsonResponse);
            out.flush();
        } else {
            try {
                String groupNumber = req.getParameter("groupNumber");
                String date = req.getParameter("date");
                String studentName = req.getParameter("studentName");
                String studentSurname = req.getParameter("studentSurname");
                String teachedName = req.getParameter("teachedName");
                String teachedSurname = req.getParameter("teachedSurname");

                List<Timetable> requiredTimetable;
                if (groupNumber != null) {
                    requiredTimetable = repository.getTimetableByGroupNumber(Integer.parseInt(groupNumber));
                } else if (date != null) {
                    requiredTimetable = repository.getTimetableByDate(date);
                }else if (studentName != null && studentSurname != null) {
                    requiredTimetable = repository.getTimetableByStudent(studentName, studentSurname);
                } else if (teachedName != null && teachedSurname != null){
                    requiredTimetable = repository.getTimetableByTeacher(teachedName, teachedSurname);
                } else {
                    throw new InvalidRequestException();
                }

                out.println(jsonService.createJson(requiredTimetable));
                resp.setStatus(200);
            } catch (Exception e) {
                resp.setStatus(400);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        String reqBody = req.getReader().lines().collect(Collectors.joining());
        PrintWriter out = resp.getWriter();
        try {
            Timetable timetable = (Timetable) jsonService.createObject(reqBody, Timetable.class);
            out.println(jsonService.createJson(repository.createTimetable(timetable)));
            resp.setStatus(201);
        } catch (Exception e) {
            resp.setStatus(400);
            out.println(e.getMessage());
        }
    }

    @SneakyThrows
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        String reqBody = req.getReader().lines().collect(Collectors.joining());
        PrintWriter out = resp.getWriter();

        String date = req.getParameter("date");


        try {
            List<Timetable> newTimetable = (List<Timetable>) jsonService.createObject(reqBody, List.class);
            List<Timetable> timetable = repository.updateTimetable(date, newTimetable); // убрать ненужную переменную
            out.println(jsonService.createJson(timetable));
            resp.setStatus(204);
        } catch (Exception e) {
            resp.setStatus(400);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();

        String date = req.getParameter("date");
        String groupNumber = req.getParameter("groupNumber");

        try {
            if (date == null && groupNumber == null)
                repository.deleteAllTimetable();
            else {
                int number = Integer.parseInt(groupNumber);
                out.println(jsonService.createJson(repository.deleteTimetableByDateAndGroupNumber(date, number)));
                resp.setStatus(200);
            }
        } catch (Exception e) {
            resp.setStatus(400);
        }
    }
}
