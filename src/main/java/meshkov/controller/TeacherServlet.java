package meshkov.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import meshkov.dto.ErrorResponse;
import meshkov.dto.TeacherRequest;
import meshkov.exception.InvalidArgumentsException;
import meshkov.exception.InvalidRequestException;
import meshkov.mapper.TeacherMapper;
import meshkov.model.Subject;
import meshkov.model.Teacher;
import meshkov.repository.imp.SimpleRepository;
import meshkov.service.JsonService;
import meshkov.service.TeacherService;
import meshkov.service.imp.JsonServiceImp;
import meshkov.service.imp.TeacherServiceImp;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@WebServlet(name = "TeacherServlet", urlPatterns = "/teachers/*")
public class TeacherServlet extends HttpServlet {

    private JsonService jsonService;
    private TeacherService teacherService;

    @Override
    public void init() throws ServletException {
        log.debug("init method invoked");
        super.init();
        jsonService = new JsonServiceImp();
        teacherService = new TeacherServiceImp(SimpleRepository.getInstance(), TeacherMapper.INSTANCE);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("doGet method invoked");
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        String jsonResponse;

        String pathInfo = req.getPathInfo();
        String queryString = req.getQueryString();
        try {
            if (pathInfo == null && queryString == null) {
                List<Teacher> responseList = teacherService.getAllTeachers();
                jsonResponse = jsonService.createJson(responseList);
                resp.setStatus(200);
                out.println(jsonResponse);
                out.flush();
            } else if (queryString == null) {
                int id = Integer.parseInt(pathInfo.substring(1));
                Teacher teacherResponse = teacherService.getTeacherById(id);
                out.println(jsonService.createJson(teacherResponse));
                out.flush();
                resp.setStatus(200);
            } else {
                String name = req.getParameter("name");
                String surname = req.getParameter("surname");
                log.debug("name = {}, surname = {}", name, surname);
                if (name == null || surname == null)
                    throw new InvalidRequestException();
                List<Teacher> teacherResponse = teacherService.getTeachersByNameAndSurname(name, surname);
                out.println(jsonService.createJson(teacherResponse));
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("doPost method invoked");
        resp.setContentType("application/json");
        String reqBody = req.getReader().lines().collect(Collectors.joining());
        PrintWriter out = resp.getWriter();
        try {
            TeacherRequest teacherRequest = (TeacherRequest) jsonService.createObject(reqBody, TeacherRequest.class);
            out.println(jsonService.createJson(teacherService.createTeacher(teacherRequest)));
            out.flush();
            resp.setStatus(201);
        } catch (InvalidArgumentsException e) {
            ErrorResponse errorResponse = new ErrorResponse("Wrong parameters");
            log.error("Exception " + e.getClass().getSimpleName() + " was thrown. Error - " + errorResponse.getErrorId());
            out.println(errorResponse);
            out.flush();
            resp.setStatus(400);
        }catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getClass().getSimpleName());
            log.error("Exception " + e.getClass().getSimpleName() + " was thrown. Error - " + errorResponse.getErrorId());
            out.println(errorResponse);
            out.flush();
            resp.setStatus(400);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("doPut method invoked");
        String reqBody = req.getReader().lines().collect(Collectors.joining());
        PrintWriter out = resp.getWriter();

        int id = Integer.parseInt(req.getPathInfo().substring(1));
        log.debug("id = {}", id);

        try {
            List<Subject> subject = (new ObjectMapper()).readValue(reqBody, new TypeReference<>() {
            });
            Teacher teacher = teacherService.addSubjects(id, subject);
            out.println(jsonService.createJson(teacher));
            out.flush();
            resp.setStatus(204);
        } catch (InvalidArgumentsException e) {
            ErrorResponse errorResponse = new ErrorResponse("Wrong parameters");
            log.error("Exception " + e.getClass().getSimpleName() + " was thrown. Error - " + errorResponse.getErrorId());
            out.println(errorResponse);
            out.flush();
            resp.setStatus(400);
        }catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getClass().getSimpleName());
            log.error("Exception " + e.getClass().getSimpleName() + " was thrown. Error - " + errorResponse.getErrorId());
            out.println(errorResponse);
            out.flush();
            resp.setStatus(400);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("doDelete method invoked");
        PrintWriter out = resp.getWriter();

        int id = Integer.parseInt(req.getPathInfo().substring(1));
        log.debug("id = {}", id);

        try {
            Teacher teacher = teacherService.deleteTeacher(id);
            out.println(jsonService.createJson(teacher));
            out.flush();
            resp.setStatus(200);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getClass().getSimpleName());
            log.error("Exception " + e.getClass().getSimpleName() + " was thrown. Error - " + errorResponse.getErrorId());
            out.println(errorResponse);
            out.flush();
            resp.setStatus(400);
        }
    }
}
