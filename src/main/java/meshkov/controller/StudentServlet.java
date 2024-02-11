package meshkov.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import meshkov.dto.StudentResponse;
import meshkov.exception.JsonParseException;
import meshkov.service.JsonService;
import meshkov.service.StudentService;
import meshkov.service.imp.JsonServiceImp;
import meshkov.service.imp.StudentServiceImp;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;


public class StudentServlet extends HttpServlet {

    StudentService studentService;
    JsonService jsonService;

    public StudentServlet(StudentService studentService, JsonService jsonService) {
        this.studentService = studentService;
        this.jsonService = jsonService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        String jsonResponse;

        if (req.getQueryString() == null) {
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
        } else {

        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }
}
