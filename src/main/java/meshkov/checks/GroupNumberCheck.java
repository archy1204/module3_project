package meshkov.checks;

import lombok.extern.slf4j.Slf4j;
import meshkov.model.Group;
import meshkov.repository.Repository;
import meshkov.repository.imp.SimpleRepository;

@Slf4j
public class GroupNumberCheck extends Middleware {

    private final Repository repository = SimpleRepository.getInstance();

    @Override
    public boolean check(Checkable model) {
        log.debug("GroupInTimeTable check is processing");
        Group group = (Group) model;
        if (group.getNumber().equals("0")) {
            log.debug("GroupInTimeTable check SUCCESS");
            return checkNext(model);
        }

        try {
            repository.getGroupByNumber(Integer.parseInt(group.getNumber()));
        } catch (Exception e) {
            log.debug("GroupInTimeTable check SUCCESS");
            return checkNext(model);
        }
        log.debug("GroupInTimeTable check FAIL");
        return false;
    }
}
