package ru.bulavin.servlet.guest;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.bulavin.dto.user.controller.UserLoginControllerDto;
import ru.bulavin.dto.user.view.UserLoginViewDto;
import ru.bulavin.manager.MapperManager;
import ru.bulavin.manager.ServiceManager;
import ru.bulavin.mapper.UserMapper;
import ru.bulavin.service.UserService;
import ru.bulavin.service.entity.user.ResultCheckLogin;
import ru.bulavin.utils.JspPathCreator;

import java.io.IOException;

import static ru.bulavin.utils.AttributeGetter.NAME_ATTRIBUTE_ERROR;
import static ru.bulavin.utils.AttributeGetter.NAME_ATTRIBUTE_USER;
import static ru.bulavin.utils.JspPathGetter.LOGIN_JSP;
import static ru.bulavin.utils.UrlPathGetter.*;

@WebServlet(LOGIN_URL)
public class LoginServlet extends HttpServlet {
    private static UserService userService;
    private static UserMapper userMapper;

    @Override
    public void init() {
        userService = ServiceManager.getUserService();
        userMapper = MapperManager.getUserMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(JspPathCreator.getDefaultPath(LOGIN_JSP)).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        final UserLoginViewDto userLoginViewDto = new UserLoginViewDto
                (req.getParameter("phone"), req.getParameter("password"));
        final UserLoginControllerDto userLoginControllerDto = userMapper.map(userLoginViewDto);
        final ResultCheckLogin value = userService.checkLogin(userLoginControllerDto);


        if (value.userDto().isPresent()) {
            req.getSession().setAttribute(NAME_ATTRIBUTE_USER, value.userDto().get());
            resp.sendRedirect(getFullPath(DEFAULT_URL));
        } else {
            req.setAttribute(NAME_ATTRIBUTE_ERROR, value.loginError());
            req.getRequestDispatcher(JspPathCreator.getDefaultPath(LOGIN_JSP)).forward(req, resp);
        }


    }
}
