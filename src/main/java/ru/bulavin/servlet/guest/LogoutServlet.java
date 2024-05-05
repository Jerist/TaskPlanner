package ru.bulavin.servlet.guest;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.bulavin.utils.UrlPathGetter;

import java.io.IOException;

import static ru.bulavin.utils.UrlPathGetter.LOGIN_URL;
import static ru.bulavin.utils.UrlPathGetter.LOGOUT_URL;

@WebServlet(LOGOUT_URL)
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession().invalidate();
        resp.sendRedirect(UrlPathGetter.getFullPath(LOGIN_URL));
    }
}
