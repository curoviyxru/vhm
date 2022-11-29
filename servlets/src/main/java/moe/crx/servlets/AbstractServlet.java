package moe.crx.servlets;

import jakarta.servlet.http.HttpServlet;

public abstract class AbstractServlet extends HttpServlet {
    public abstract String getServletPath();
}
