package umlerr.servicecars.util;

import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CarsUtils {

    public static final List<String> CAR_NAMES = List.of("АВТОМОБИЛЬ", "АВТОМОБИЛЯ", "АВТОМОБИЛЕЙ");
    public static final String CAR_NOT_FOUND = "НЕ НАЙДЕН";
    public static final String CAR_ALREADY_EXIST = "УЖЕ СУЩЕСТВУЕТ";
    public static final String SUCCESS = "УСПЕШНО";
    public static final String ADDED = "ДОБАВЛЕН";
    public static final String DELETED = "УДАЛЕН";
    public static final String EDITED = "ОТРЕДАКТИРОВАН";
    public static final String SPACE = " ";

    public static String getCarNotFound(Object value) {
        return CAR_NAMES.getFirst() + SPACE + value + SPACE + CAR_NOT_FOUND;
    }

    public static String getCarAlreadyExist(Object value) {
        return CAR_NAMES.getFirst() + SPACE + value + SPACE + CAR_ALREADY_EXIST;
    }

    public static String getCarAdded(Object value) {
        return CAR_NAMES.getFirst() + SPACE + value + SPACE + SUCCESS + SPACE + ADDED;
    }

    public static String getCarDeleted(Object value) {
        return CAR_NAMES.getFirst() + SPACE + value + SPACE + SUCCESS + SPACE + DELETED;
    }

    public static String getCarEdited() {
        return CAR_NAMES.getFirst() + SPACE + SUCCESS + SPACE + EDITED;
    }
}
