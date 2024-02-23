package meshkov.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import meshkov.dto.GroupRequest;
import meshkov.exception.InvalidRequestException;
import meshkov.exception.JsonParseException;
import meshkov.mapper.GroupMapper;
import meshkov.model.Group;
import meshkov.repository.Repository;
import meshkov.repository.imp.SimpleRepository;
import meshkov.service.JsonService;
import meshkov.service.imp.JsonServiceImp;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@WebServlet(name = "GroupServlet", urlPatterns = "/groups/*")
public class GroupServlet extends HttpServlet {
    private JsonService jsonService;
    private Repository repository;
    private GroupMapper groupMapper;

    @Override
    public void init() throws ServletException {
        super.init();
        groupMapper = GroupMapper.INSTANCE;
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
            List<Group> responseList = repository.getAllGroups();
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
                String number = req.getParameter("number");
                String name = req.getParameter("name");
                String surname = req.getParameter("surname");
                if (name != null && surname != null) {
                    List<Group> groupResponse = repository.getGroupsByNameAndSurname(name, surname);
                    out.println(jsonService.createJson(groupResponse));
                } else if (number != null) {
                    Group groupResponse = repository.getGroupByNumber(Integer.parseInt(number));
                    out.println(jsonService.createJson(groupResponse));
                } else {
                    throw new InvalidRequestException();
                }
                resp.setStatus(200);
            } catch (Exception e) {
                resp.setStatus(400);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String reqBody = req.getReader().lines().collect(Collectors.joining());
        PrintWriter out = resp.getWriter();
        try {
            GroupRequest groupRequest = (GroupRequest) jsonService.createObject(reqBody, GroupRequest.class);
            for (int id :groupRequest.getStudents()) {
                groupRequest.getStudentObjects().add(repository.getStudentById(id));
            }
            out.println(jsonService.createJson(repository.createGroup(groupMapper.mapToModel(groupRequest))));
            resp.setStatus(201);
        } catch (Exception e) {
            resp.setStatus(400);
            out.println(e.getMessage());
        }
    }

    @SneakyThrows
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String reqBody = req.getReader().lines().collect(Collectors.joining());
        PrintWriter out = resp.getWriter();

        int id = Integer.parseInt(req.getPathInfo().substring(1));

        try {
            List<Integer> students = (List<Integer>) jsonService.createObject(reqBody, List.class);
            Group group = repository.addStudentsToGroup(id, students);
            out.println(jsonService.createJson(group));
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
            Group group = repository.deleteGroup(id);
            out.println(jsonService.createJson(group));
            resp.setStatus(200);
        } catch (Exception e) {
            resp.setStatus(400);
        }
    }
}
