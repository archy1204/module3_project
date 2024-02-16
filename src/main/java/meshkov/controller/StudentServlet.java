package meshkov.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import meshkov.dto.StudentRequest;
import meshkov.dto.StudentResponse;
import meshkov.exception.JsonParseException;
import meshkov.exception.StudentNotFoundException;
import meshkov.mapper.StudentMapper;
import meshkov.repository.Repository;
import meshkov.repository.imp.RepositoryWithJson;
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

    StudentService studentService;
    JsonService jsonService;

    @Override
    public void init() throws ServletException {
        super.init();
        StudentMapper studentMapper = StudentMapper.INSTANCE;
        jsonService = new JsonServiceImp();
        Repository repository = null;
        try {
            repository = new SimpleRepository(jsonService);
        } catch (IOException | JsonParseException e) {
            log.error(e.getMessage());
            throw new RuntimeException();
        }
        studentService = new StudentServiceImp(repository, studentMapper);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        String jsonResponse;

        String pathInfo = req.getPathInfo();
        String queryString = req.getQueryString();

        if (pathInfo == null && queryString == null) {
            List<StudentResponse> responseList = studentService.getAllStudent();
            try {
                jsonResponse = jsonService.createJson(responseList);
                resp.setStatus(200);
            } catch (JsonParseException e) {
                jsonResponse = e.getMessage();
                resp.setStatus(400);
            }
            out.println(jsonResponse);
            out.flush();
        } else if (queryString == null){
            int id = Integer.parseInt(pathInfo.substring(1));
            try {
                StudentResponse studentResponse = studentService.getStudentById(id);
                out.println(jsonService.createJson(studentResponse));
                resp.setStatus(200);
            } catch (Exception e) {
                resp.setStatus(400);
            }
        } else {
            try {
                String name = req.getParameter("name");
                String surname = req.getParameter("surname");
                List<StudentResponse> studentResponse = studentService.getStudentsByNameAndSurname(name, surname);
                out.println(jsonService.createJson(studentResponse));
                resp.setStatus(200);
            } catch (Exception e) {
                resp.setStatus(400);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String reqBody = req.getReader().lines().collect(Collectors.joining());
        PrintWriter out = resp.getWriter();
        try {
            StudentRequest studentRequest = (StudentRequest) jsonService.createObject(reqBody, StudentRequest.class);
            out.println(studentService.createStudent(studentRequest));
            resp.setStatus(201);
        } catch (Exception e) {
            resp.setStatus(400);
            out.println(e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String reqBody = req.getReader().lines().collect(Collectors.joining());
        PrintWriter out = resp.getWriter();

        int id = Integer.parseInt(req.getPathInfo().substring(1));

        try {
            StudentRequest studentRequest = (StudentRequest) jsonService.createObject(reqBody, StudentRequest.class);
            StudentResponse studentResponse = studentService.changeStudentData(id, studentRequest);
            out.println(jsonService.createJson(studentResponse));
            resp.setStatus(204);
        } catch (Exception e) {
            resp.setStatus(400);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();

        int id = Integer.parseInt(req.getPathInfo().substring(1));

        try {
            StudentResponse studentResponse = studentService.deleteStudent(id);
            out.println(jsonService.createJson(studentResponse));
            resp.setStatus(200);
        } catch (Exception e) {
            resp.setStatus(400);
        }
    }
}
