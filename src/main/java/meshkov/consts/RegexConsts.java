package meshkov.consts;

import lombok.experimental.UtilityClass;

@UtilityClass
public class RegexConsts {
    public static final String NAME_AND_SURNAME = "^[a-zA-Z'-]{0,10}";
    public static final String PHONENUMBER = "^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$";
}
