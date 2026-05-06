package com.ceichhorst.reservation.controller;

import com.ceichhorst.reservation.dao.AdministratorDao;
import com.ceichhorst.reservation.dao.RestaurantDao;
import com.ceichhorst.reservation.entity.Administrator;
import com.ceichhorst.reservation.entity.Restaurant;
import com.ceichhorst.reservation.service.ServiceManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

/**
 * Servlet responsible for managing restaurant data and administrator assignments.
 */
@WebServlet("/admin/restaurants")
public class RestaurantManagementServlet extends HttpServlet {

    private RestaurantDao restaurantDao = new RestaurantDao();
    private AdministratorDao adminDao = new AdministratorDao();
    private ServiceManager serviceManager = new ServiceManager();

    /**
     * Handles GET requests to load restaurant management page
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userEmail") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Long adminId = (Long) session.getAttribute("adminId");

        if (adminId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Load restaurants for this admin
        List<Restaurant> restaurants = adminDao.getRestaurantByAdminId(adminId);
        request.setAttribute("restaurants", restaurants);

        // Handle selected restaurant
        String restaurantIdParam = request.getParameter("restaurantId");

        if (restaurantIdParam != null && !restaurantIdParam.isEmpty()) {
            Long restaurantId = Long.parseLong(restaurantIdParam);

            Restaurant selected = restaurantDao.getById(restaurantId);

            if (selected != null) {
                request.setAttribute("selectedRestaurant", selected);
                request.setAttribute("selectedRestaurantId", restaurantId);
            }
        }

        request.getRequestDispatcher("/WEB-INF/admin/restaurants")
                .forward(request, response);
    }

    /**
     * Handles POST actions (update restaurants, manage admins)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userEmail") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Long adminId = (Long) session.getAttribute("adminId");
        String role = (String) session.getAttribute("role");

        String action = request.getParameter("action");
        Long restaurantId = null;

        try {
            switch (action) {

                case "updateRestaurant": {
                    restaurantId = Long.parseLong(request.getParameter("restaurantId"));

                    serviceManager.authorizeAdminAccess(adminId, restaurantId);

                    Restaurant restaurant = restaurantDao.getById(restaurantId);

                    restaurant.setName(request.getParameter("name"));
                    restaurant.setCity(request.getParameter("city"));
                    restaurant.setState(request.getParameter("state"));
                    restaurant.setDescription(request.getParameter("description"));
                    restaurant.setHowItWorks(request.getParameter("howItWorks"));

                    boolean requireAllergenInfo =
                            request.getParameter("requireAllergenInfo") != null;
                    restaurant.setRequireAllergenInfo(requireAllergenInfo);

                    restaurantDao.update(restaurant);
                    break;
                }

                case "addAdmin": {
                    restaurantId = Long.parseLong(request.getParameter("restaurantId"));

                    serviceManager.authorizeAdminAccess(adminId, restaurantId);

                    String email = request.getParameter("adminEmail");

                    Administrator newAdmin = adminDao.getAdministratorByEmail(email);

                    if (newAdmin == null) {
                        throw new RuntimeException("Admin not found with that email.");
                    }

                    Restaurant restaurant = restaurantDao.getById(restaurantId);

                    restaurant.getAdministrators().add(newAdmin);
                    newAdmin.getRestaurants().add(restaurant);

                    restaurantDao.save(restaurant);
                    break;

                }

                case "removeAdmin": {
                    restaurantId = Long.parseLong(request.getParameter("restaurantId"));

                    serviceManager.authorizeAdminAccess(adminId, restaurantId);

                    Long removeAdminId = Long.parseLong(request.getParameter("adminId"));

                    Administrator adminToRemove = adminDao.getById(removeAdminId);
                    Restaurant restaurant = restaurantDao.getById(restaurantId);

                    restaurant.getAdministrators().remove(adminToRemove);
                    adminToRemove.getRestaurants().remove(restaurant);

                    restaurantDao.delete(restaurant);
                    break;
                }

                case "createRestaurant": {

                    if (!"SUPER_ADMIN".equals(role)) {
                        throw new RuntimeException("Unauthorized");
                    }

                    Restaurant restaurant = new Restaurant();
                    restaurant.setName(request.getParameter("name"));
                    restaurant.setCity(request.getParameter("city"));
                    restaurant.setState(request.getParameter("state"));
                    restaurant.setDescription(request.getParameter("description"));
                    restaurant.setHowItWorks(request.getParameter("howItWorks"));

                    boolean requireAllergenInfo =
                            request.getParameter("requireAllergenInfo") != null;
                    restaurant.setRequireAllergenInfo(requireAllergenInfo);

                    // Assign creator as admin automatically
                    Administrator creator = adminDao.getById(adminId);
                    restaurant.getAdministrators().add(creator);
                    creator.getRestaurants().add(restaurant);

                    restaurantDao.save(restaurant);

                }
            }
        } catch (RuntimeException e) {
            request.getSession().setAttribute("error", e.getMessage());
        }

        // Redirect back to selected restaurant
        if (restaurantId != null) {
            response.sendRedirect(
                    request.getContextPath() + "/admin/restaurants?restaurantId=" + restaurantId
            );
        }

    }

}
