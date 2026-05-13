package com.ceichhorst.reservation.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class AuthFilterTest {

    private AuthFilter authFilter;

    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private ServletContext servletContext;
    private FilterChain filterChain;

    @BeforeEach
    void setup() {
        authFilter = new AuthFilter();

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        servletContext = mock(ServletContext.class);
        filterChain = mock(FilterChain.class);

        when(request.getServletContext()).thenReturn(servletContext);
    }

    @Test
    void testRedirectToLoginWhenNotAuthenticated()
            throws ServletException, IOException {

        when(request.getSession(false)).thenReturn(null);

        when(servletContext.getAttribute("LOGIN_URL"))
                .thenReturn("https://test-login.com");

        authFilter.doFilter(request, response, filterChain);

        verify(response).sendRedirect("https://test-login.com");

        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void testUnauthorizedWhenLoginUrlMissing()
            throws ServletException, IOException{

        when(request.getSession(false)).thenReturn(null);

        when(servletContext.getAttribute("LOGIN_URL"))
                .thenReturn(null);

        authFilter.doFilter(request, response, filterChain);

        verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED,
                "Login URL not configured"
        );

        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void testRedirectWhenRoleUnauthorized()
            throws ServletException, IOException {

        when(request.getSession(false)).thenReturn(session);

        when(session.getAttribute("userEmail"))
                .thenReturn("test@example.com");

        when(session.getAttribute("role"))
                .thenReturn("USER");

        when(request.getContextPath())
                .thenReturn("/reservation");

        authFilter.doFilter(request, response, filterChain);

        verify(response).sendRedirect("/reservation/unauthorized");

        verify(filterChain, never()).doFilter(request, response);

    }

    @Test
    void testAdminAllowed()
            throws ServletException, IOException {

        when(request.getSession(false)).thenReturn(session);

        when(servletContext.getAttribute("userEmail"))
                .thenReturn("test@example.com");

        when(servletContext.getAttribute("role"))
                .thenReturn("ADMIN");

        authFilter.doFilter(request, response, filterChain);

        verify(filterChain, never()).doFilter(request, response);

        verify(response, never()).sendRedirect(anyString());

    }

    @Test
    void testSuperAdminAllowed()
            throws ServletException, IOException {

        when(request.getSession(false)).thenReturn(session);

        when(servletContext.getAttribute("userEmail"))
                .thenReturn("test@example.com");

        when(servletContext.getAttribute("role"))
                .thenReturn("SUPER_ADMIN");

        authFilter.doFilter(request, response, filterChain);

        verify(filterChain, never()).doFilter(request, response);

        verify(response, never()).sendRedirect(anyString());

    }


}
