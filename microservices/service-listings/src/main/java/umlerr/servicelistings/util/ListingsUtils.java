package umlerr.servicelistings.util;

import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ListingsUtils {

    public static final List<String> LISTING_NAMES = List.of("ОБЪЯВЛЕНИЕ", "ОБЪЯВЛЕНИЯ", "ОБЪЯВЛЕНИЙ");
    public static final String LISTING_NOT_FOUND = "НЕ НАЙДЕНО";
    public static final String LISTING_ALREADY_EXIST = "УЖЕ СУЩЕСТВУЕТ";
    public static final String SUCCESS = "УСПЕШНО";
    public static final String ADDED = "ДОБАВЛЕНО";
    public static final String DELETED = "УДАЛЕНО";
    public static final String EDITED = "ОТРЕДАКТИРОВАНО";
    public static final String SPACE = " ";

    public static String getListingNotFound(Object value) {
        return LISTING_NAMES.getFirst() + SPACE + value + SPACE + LISTING_NOT_FOUND;
    }

    public static String getListingAlreadyExist(Object value) {
        return LISTING_NAMES.getFirst() + SPACE + value + SPACE + LISTING_ALREADY_EXIST;
    }

    public static String getListingAdded(Object value) {
        return LISTING_NAMES.getFirst() + SPACE + value + SPACE + SUCCESS + SPACE + ADDED;
    }

    public static String getListingDeleted(Object value) {
        return LISTING_NAMES.getFirst() + SPACE + value + SPACE + SUCCESS + SPACE + DELETED;
    }

    public static String getListingEdited() {
        return LISTING_NAMES.getFirst() + SPACE + SUCCESS + SPACE + EDITED;
    }
}
