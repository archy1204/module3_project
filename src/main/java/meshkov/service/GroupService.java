package meshkov.service;

import meshkov.dto.GroupRequest;
import meshkov.exception.*;
import meshkov.model.Group;

import java.util.List;

public interface GroupService {

    List<Group> getAllGroups();

    Group getGroupByNumber(int id) throws GroupNotFoundException;

    List<Group> getGroupsByNameAndSurname(String name, String surname) throws StudentNotFoundException, GroupNotFoundException;

    Group createGroup(GroupRequest groupRequest) throws StudentNotFoundException, GroupNotFoundException, InvalidArgumentsException, TeacherNotFoundException, TimetableNotFoundException, InvalidAmountException;

    public Group addStudentsToGroup(int number, List<Integer> studentsId) throws StudentNotFoundException, GroupNotFoundException, InvalidArgumentsException, TeacherNotFoundException, TimetableNotFoundException, InvalidAmountException;

    Group deleteGroup(int id) throws GroupNotFoundException;
}
