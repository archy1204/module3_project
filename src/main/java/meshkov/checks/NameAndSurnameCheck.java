package meshkov.checks;

import lombok.extern.slf4j.Slf4j;
import meshkov.consts.RegexConsts;
import meshkov.model.Teacher;
import meshkov.repository.Repository;

@Slf4j
public class NameAndSurnameCheck extends Middleware {

    @Override
    public boolean check(Checkable model) {
        log.debug("NameAndSurname check is processing");
        NameCheckable nameCheckable = (NameCheckable) model;

        if (model instanceof Teacher && ((Teacher) model).getExperience() == -1) {
            log.debug("NameAndSurname check SUCCESS");
            return checkNext(model);
        }


        if (nameCheckable.getName().matches(RegexConsts.NAME_AND_SURNAME) && nameCheckable.getSurname().matches(RegexConsts.NAME_AND_SURNAME)) {
            log.debug("NameAndSurname check SUCCESS");
            return checkNext(model);
        }
        else {
            log.debug("NameAndSurname check FAIL");
            return false;
        }
    }
}
