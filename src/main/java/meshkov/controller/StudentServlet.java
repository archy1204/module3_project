package meshkov.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import meshkov.dto.ErrorResponse;
import meshkov.dto.StudentRequest;
import meshkov.exception.InvalidArgumentsException;
import meshkov.exception.InvalidRequestException;
import meshkov.mapper.StudentMapper;
import meshkov.model.Student;
import meshkov.repository.Repository;
import meshkov.repository.imp.SimpleRepository;
import meshkov.service.JsonService;
import meshkov.service.StudentService;
import meshkov.service.imp.JsonServiceImp;
import meshkov.service.imp.StudentServiceImp;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@WebServlet(name = "StudentServlet", urlPatterns = "/students/*")
public class StudentServlet extends HttpServlet {

    private StudentService studentService;
    private JsonService jsonService;

    @Override
    public void init() throws ServletException {
        log.debug("init method invoked");
        super.init();
        StudentMapper studentMapper = StudentMapper.INSTANCE;
        jsonService = new JsonServiceImp();
        Repository repository = SimpleRepository.getInstance();
        studentService = new StudentServiceImp(repository, studentMapper);
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
                List<Student> responseList = studentService.getAllStudent();

                jsonResponse = jsonService.createJson(responseList);
                resp.setStatus(200);
                out.println(jsonResponse);
                out.flush();
            } else if (queryString == null) {
                int id = Integer.parseInt(pathInfo.substring(1));
                Student studentResponse = studentService.getStudentById(id);
                out.println(jsonService.createJson(studentResponse));
                resp.setStatus(200);
            } else {
                String name = req.getParameter("name");
                String surname = req.getParameter("surname");
                log.debug("name = {}, surname = {}", name, surname);
                if (name == null || surname == null)
                    throw new InvalidRequestException();
                List<Student> studentResponse = studentService.getStudentsByNameAndSurname(name, surname);
                out.println(jsonService.createJson(studentResponse));
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
        String reqBody = req.getReader().lines().collect(Collectors.joining());
        PrintWriter out = resp.getWriter();
        try {
            StudentRequest studentRequest = (StudentRequest) jsonService.createObject(reqBody, StudentRequest.class);
            out.println(jsonService.createJson(studentService.createStudent(studentRequest)));
            out.flush();
            resp.setStatus(201);
        } catch (InvalidArgumentsException e) {
            ErrorResponse errorResponse = new ErrorResponse("Wrong parameters");
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
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("doPut method invoked");
        resp.setContentType("application/json");
        String reqBody = req.getReader().lines().collect(Collectors.joining());
        PrintWriter out = resp.getWriter();

        int id = Integer.parseInt(req.getPathInfo().substring(1));
        log.debug("id = {}", id);

        try {
            StudentRequest studentRequest = (StudentRequest) jsonService.createObject(reqBody, StudentRequest.class);
            Student studentResponse = studentService.changeStudentData(id, studentRequest);
            out.println(jsonService.createJson(studentResponse));
            out.flush();
            resp.setStatus(204);
        } catch (InvalidArgumentsException e) {
            ErrorResponse errorResponse = new ErrorResponse("Wrong parameters");
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
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("doDelete method invoked");
        PrintWriter out = resp.getWriter();

        int id = Integer.parseInt(req.getPathInfo().substring(1));
        log.debug("id = {}", id);

        try {
            Student studentResponse = studentService.deleteStudent(id);
            out.println(jsonService.createJson(studentResponse));
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