package meshkov.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import meshkov.consts.FileConstants;
import meshkov.dto.ErrorResponse;
import meshkov.dto.GroupRequest;
import meshkov.exception.InvalidAmountException;
import meshkov.exception.InvalidArgumentsException;
import meshkov.exception.InvalidRequestException;
import meshkov.mapper.GroupMapper;
import meshkov.model.Group;
import meshkov.repository.Repository;
import meshkov.repository.imp.SimpleRepository;
import meshkov.service.GroupService;
import meshkov.service.JsonService;
import meshkov.service.imp.GroupServiceImp;
import meshkov.service.imp.JsonServiceImp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;


@Slf4j
@WebServlet(name = "GroupServlet", urlPatterns = "/groups/*")
public class GroupServlet extends HttpServlet {

    private JsonService jsonService;
    private Repository repository;
    private GroupService groupService;

    public int minStudents;
    public int maxStudents;

    @Override
    public void init() throws ServletException {
        log.debug("init method invoked");
        super.init();
        jsonService = new JsonServiceImp();
        repository = SimpleRepository.getInstance();
        groupService = new GroupServiceImp(repository, GroupMapper.INSTANCE);

        Properties properties = new Properties();
        try (BufferedReader propertyReader = Files.newBufferedReader(Path.of(FileConstants.PROPERTY_FILE_NAME))) {
            properties.load(propertyReader);
            minStudents = Integer.parseInt(properties.getProperty("minStudents"));
            maxStudents = Integer.parseInt(properties.getProperty("maxStudents"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
                List<Group> responseList = groupService.getAllGroups();
                jsonResponse = jsonService.createJson(responseList);
                resp.setStatus(200);
                out.println(jsonResponse);
                out.flush();
            } else {
                String number = req.getParameter("number");
                String name = req.getParameter("name");
                String surname = req.getParameter("surname");
                if (name != null && surname != null) {
                    log.debug("name = {}, surname = {}", name, surname);
                    List<Group> groupResponse = groupService.getGroupsByNameAndSurname(name, surname);
                    out.println(jsonService.createJson(groupResponse));
                    out.flush();
                } else if (number != null) {
                    log.debug("number = {}", number);
                    Group groupResponse = groupService.getGroupByNumber(number);
                    out.println(jsonService.createJson(groupResponse));
                    out.flush();
                } else {
                    log.debug("All parameters are null");
                    throw new InvalidRequestException();
                }
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
            GroupRequest groupRequest = (GroupRequest) jsonService.createObject(reqBody, GroupRequest.class);
            for (int id : groupRequest.getStudents()) {
                groupRequest.getStudentObjects().add(repository.getStudentById(id));
            }
            out.println(jsonService.createJson(groupService.createGroup(groupRequest)));
            out.flush();
            resp.setStatus(201);
        } catch (InvalidArgumentsException e) {
            ErrorResponse errorResponse = new ErrorResponse("Wrong parameters");
            log.error("Exception " + e.getClass().getSimpleName() + " was thrown. Error - " + errorResponse.getErrorId());
            out.println(errorResponse);
            out.flush();
            resp.setStatus(400);
        } catch (InvalidAmountException e) {
            ErrorResponse errorResponse = new ErrorResponse("Wrong amount of students. It must be more than " + (minStudents - 1) + " and fewer than "
                    + (maxStudents + 1));
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
        String reqBody = req.getReader().lines().collect(Collectors.joining());
        PrintWriter out = resp.getWriter();

        String number = req.getPathInfo().substring(1);
        log.debug("number = {}", number);

        try {
            List<Integer> students = (List<Integer>) jsonService.createObject(reqBody, List.class);
            Group group = groupService.addStudentsToGroup(number, students);
            out.println(jsonService.createJson(group));
            out.flush();
            resp.setStatus(204);
        } catch (InvalidArgumentsException e) {
            ErrorResponse errorResponse = new ErrorResponse("Wrong parameters");
            log.error("Exception " + e.getClass().getSimpleName() + " was thrown. Error - " + errorResponse.getErrorId());
            out.println(errorResponse);
            out.flush();
            resp.setStatus(400);
        } catch (InvalidAmountException e) {
            ErrorResponse errorResponse = new ErrorResponse("Wrong amount of students. It must be more than " + (minStudents - 1) + " and fewer than "
                    + (maxStudents + 1));
            log.error("Exception " + e.getClass().getSimpleName() + " was thrown. Error - " + errorResponse.getErrorId());
            out.println(errorResponse);
            resp.setStatus(400);
            out.flush();
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

        String number = req.getPathInfo().substring(1);
        log.debug("number = {}", number);

        try {
            Group group = groupService.deleteGroup(number);
            out.println(jsonService.createJson(group));
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
