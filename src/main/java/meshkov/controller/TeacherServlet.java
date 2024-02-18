package meshkov.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import meshkov.dto.StudentRequest;
import meshkov.dto.StudentResponse;
import meshkov.dto.TeacherRequest;
import meshkov.exception.JsonParseException;
import meshkov.mapper.StudentMapper;
import meshkov.mapper.TeacherMapper;
import meshkov.model.Subject;
import meshkov.model.Teacher;
import meshkov.repository.Repository;
import meshkov.repository.imp.SimpleRepository;
import meshkov.service.JsonService;
import meshkov.service.StudentService;
import meshkov.service.TeacherService;
import meshkov.service.imp.JsonServiceImp;
import meshkov.service.imp.StudentServiceImp;
import meshkov.service.imp.TeacherServiceImp;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@WebServlet(name = "TeacherServlet", urlPatterns = "/teachers/*")
public class TeacherServlet extends HttpServlet {

    private JsonService jsonService;
    private Repository repository;
    private TeacherMapper teacherMapper;

    @Override
    public void init() throws ServletException {
        super.init();
        teacherMapper = TeacherMapper.INSTANCE;
        jsonService = new JsonServiceImp();
        repository = SimpleRepository.getInstance();

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        String jsonResponse;

        String pathInfo = req.getPathInfo();
        String queryString = req.getQueryString();

        if (pathInfo == null && queryString == null) {
            List<Teacher> responseList = repository.getAllTeachers();
            try {
                jsonResponse = jsonService.createJson(responseList);
                resp.setStatus(200);
            } catch (JsonParseException e) {
                jsonResponse = e.getMessage();
                resp.setStatus(400);
            }
            out.println(jsonResponse);
            out.flush();
        } else if (queryString == null) {
            int id = Integer.parseInt(pathInfo.substring(1));
            try {
                Teacher teacherResponse = repository.getTeacherById(id);
                out.println(jsonService.createJson(teacherResponse));
                resp.setStatus(200);
            } catch (Exception e) {
                resp.setStatus(400);
            }
        } else {
            try {
                String name = req.getParameter("name");
                String surname = req.getParameter("surname");
                List<Teacher> teacherResponse = repository.getTeachersByNameAndSurname(name, surname);
                out.println(jsonService.createJson(teacherResponse));
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
            TeacherRequest teacherRequest = (TeacherRequest) jsonService.createObject(reqBody, TeacherRequest.class);
            out.println(repository.createTeacher(teacherMapper.mapToModel(teacherRequest)));
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
            List<Subject> subject = (List<Subject>) jsonService.createObject(reqBody, List.class);
            Teacher teacher = repository.addSubjects(id, subject);
            out.println(jsonService.createJson(teacher));
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
            Teacher teacher = repository.deleteTeacher(id);
            out.println(jsonService.createJson(teacher));
            resp.setStatus(200);
        } catch (Exception e) {
            resp.setStatus(400);
        }
    }
}
