import java.io.IOException;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;

@WebServlet("/autosale")
public class AutoSaleServlet extends HttpServlet {

    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?\\d{10,15}$");
    private static final Pattern CAR_NUMBER_PATTERN = Pattern.compile("^[А-ЯA-Z]\\d{3}[А-ЯA-Z]{2}\\s?\\d{2,3}$");
    private static final Pattern PRICE_PATTERN = Pattern.compile("^\\d+(\\.\\d{1,2})?$");

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("autosale.jsp");
        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String carNumber = request.getParameter("carNumber");
        String model = request.getParameter("model");
        String price = request.getParameter("price");

        String errorMessage = null;

        if (!PHONE_PATTERN.matcher(phone).matches()) {
            errorMessage = "Ошибка: Неправильный формат телефона!";
        } else if (!CAR_NUMBER_PATTERN.matcher(carNumber).matches()) {
            errorMessage = "Ошибка: Номер машины должен быть в формате A123BC 77!";
        } else if (!PRICE_PATTERN.matcher(price).matches()) {
            errorMessage = "Ошибка: Цена должна быть числом!";
        }

        request.setAttribute("name", name);
        request.setAttribute("phone", phone);
        request.setAttribute("carNumber", carNumber);
        request.setAttribute("model", model);
        request.setAttribute("price", price);

        if (errorMessage == null) {
            request.setAttribute("message", "Продажа успешно зарегистрирована");
        } else {
            request.setAttribute("message", "Ошибка в данных");
            request.setAttribute("error", errorMessage);
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("result.jsp");
        dispatcher.forward(request, response);
    }
}
