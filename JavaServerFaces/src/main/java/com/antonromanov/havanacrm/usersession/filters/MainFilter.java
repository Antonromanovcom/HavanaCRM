package com.antonromanov.havanacrm.usersession.filters;

import java.io.IOException;
import javax.faces.application.FacesMessage;
import javax.faces.application.ResourceHandler;
import javax.faces.context.FacesContext;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;



@WebFilter(filterName = "AuthFilter", urlPatterns = { "*.xhtml" })
public class MainFilter implements Filter{


    public MainFilter() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        try {
            HttpServletRequest reqt = (HttpServletRequest) request;
            HttpServletResponse resp = (HttpServletResponse) response;
            HttpSession ses = reqt.getSession(false);

            String reqURI = reqt.getRequestURI();
            if (reqURI.indexOf("/login.xhtml") >= 0
                    || reqURI.indexOf("/signin.xhtml") >= 0
                    || (ses != null && ses.getAttribute("username") != null)
                    || reqURI.indexOf("/public/") >= 0
                    || reqURI.contains("javax.faces.resource")) {
                chain.doFilter(request, response);
            }else {
                // logger.info("Попытка пролезть не залогиневшись");
                resp.sendRedirect(reqt.getContextPath() + "/login.xhtml");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


    }

    @Override
    public void destroy() {
    }
}
