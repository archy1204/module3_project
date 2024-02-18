package meshkov.service;

import meshkov.exception.GroupNotFoundException;
import meshkov.exception.StudentNotFoundException;
import meshkov.model.Group;
import meshkov.model.Student;

import java.util.ArrayList;
import java.util.List;

public interface GroupService {

    public List<Group> getAllGroups();

    public Group getGroupById(int id);

    public List<Group> getGroupsByNameAndSurname(String name, String surname);

    public Group createGroup(Group group);

    public Group addStudentsToGroup(int id, List<Student> students);

    public Group deleteGroup(int id);
}
