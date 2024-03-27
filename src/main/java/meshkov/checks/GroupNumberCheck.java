package meshkov.checks;

import lombok.extern.slf4j.Slf4j;
import meshkov.exception.GroupNotFoundException;
import meshkov.model.Group;
import meshkov.repository.Repository;
import meshkov.repository.imp.SimpleRepository;

@Slf4j
public class GroupNumberCheck extends Middleware {

    public GroupNumberCheck(Repository repository) {
        super(repository);
    }

    @Override
    public boolean check(Checkable model) {
        log.debug("GroupInTimeTable check is processing");
        Group group = (Group) model;

        try {
            repository.getGroupByNumber(group.getNumber());
        } catch (GroupNotFoundException e) {
            if (group.getId() == -2) {
                log.debug("GroupInTimeTable check FAIL");
                return false;
            }
            log.debug("GroupInTimeTable check SUCCESS");
            return checkNext(model);
        }

        if (group.getId() == -2) {
            log.debug("GroupInTimeTable check SUCCESS");
            return checkNext(model);
        }

        log.debug("GroupInTimeTable check FAIL");
        return false;
    }
}
