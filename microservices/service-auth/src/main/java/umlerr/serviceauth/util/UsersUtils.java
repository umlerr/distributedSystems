package umlerr.serviceauth.util;

import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UsersUtils {

    public static final List<String> USERS_NAMES = List.of("ПОЛЬЗОВАТЕЛЬ", "ПОЛЬЗОВАТЕЛЯ",
        "ПОЛЬЗОВАТЕЛИ", "ПОЛЬЗОВАТЕЛЕЙ");
    public static final String USERS_NOT_FOUND = "НЕ НАЙДЕН";
    public static final String USERS_ALREADY_EXIST = "УЖЕ СУЩЕСТВУЕТ";
    public static final String SUCCESS = "УСПЕШНО";
    public static final String REGISTERED = "ЗАРЕГИСТРИРОВАН";
    public static final String DELETED = "УДАЛЕН";
    public static final String EDITED = "ОТРЕДАКТИРОВАН";
    public static final String SPACE = " ";

    public static String getUsersNotFound(Object value) {
        return USERS_NAMES.getFirst() + SPACE + value + SPACE + USERS_NOT_FOUND;
    }

    public static String getUsersAlreadyExist(Object value) {
        return USERS_NAMES.getFirst() + SPACE + value + SPACE + USERS_ALREADY_EXIST;
    }

    public static String getUsersRegistered(Object value) {
        return USERS_NAMES.getFirst() + SPACE + value + SPACE + SUCCESS + SPACE + REGISTERED;
    }

    public static String getUsersDeleted() {
        return USERS_NAMES.getFirst() + SPACE + SUCCESS + SPACE + DELETED;
    }

    public static String getUsersEdited() {
        return USERS_NAMES.getFirst() + SPACE + SUCCESS + SPACE + EDITED;
    }
}
