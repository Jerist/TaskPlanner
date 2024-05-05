package ru.bulavin.servlet.guest;

import ru.bulavin.dto.user.controller.UserRegistrationControllerDto;
import ru.bulavin.dto.user.view.UserRegistrationViewDto;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.bulavin.manager.MapperManager;
import ru.bulavin.manager.ServiceManager;
import ru.bulavin.manager.ValidationManager;
import ru.bulavin.mapper.UserMapper;
import ru.bulavin.processing.validator.RegistrationUserValidator;
import ru.bulavin.processing.validator.load.LoadValidationResult;
import ru.bulavin.service.UserService;
import ru.bulavin.utils.JspPathCreator;

import java.io.IOException;
import java.util.List;

import static ru.bulavin.utils.AttributeGetter.NAME_ATTRIBUTE_ERRORS;
import static ru.bulavin.utils.JspPathGetter.REGISTRATION_JSP;
import static ru.bulavin.utils.UrlPathGetter.LOGIN_URL;
import static ru.bulavin.utils.UrlPathGetter.REGISTRATION_URL;


@WebServlet(REGISTRATION_URL)
public class RegistrationServlet extends HttpServlet {

    private static UserService userService;
    private RegistrationUserValidator registrationUserValidator;
    private static UserMapper userMapper;


    @Override
    public void init(ServletConfig config) {
        userService = ServiceManager.getUserService();
        registrationUserValidator = ValidationManager.getRegistrationUserValidator();
        userMapper = MapperManager.getUserMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(JspPathCreator.getDefaultPath(REGISTRATION_JSP)).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        final UserRegistrationViewDto userRegistrationViewDto = getUserRegistrationViewDto(req);
        final LoadValidationResult result = registrationUserValidator.isValid(userRegistrationViewDto);

        if (result.isEmpty()) {
            UserRegistrationControllerDto userRegistrationControllerDto = userMapper.map(userRegistrationViewDto);
            userService.insertUser(userRegistrationControllerDto);
            resp.sendRedirect(LOGIN_URL);
        } else {
            req.setAttribute(NAME_ATTRIBUTE_ERRORS, result.getLoadErrors());
            doGet(req, resp);
        }
    }

    private static UserRegistrationViewDto getUserRegistrationViewDto(HttpServletRequest req) {
        return UserRegistrationViewDto.builder()
                .name(req.getParameter("name"))
                .phone(req.getParameter("phone"))
                .email(req.getParameter("email"))
                .password(req.getParameter("password"))
                .build();
    }
}
