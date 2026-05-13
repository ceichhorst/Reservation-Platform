package com.ceichhorst.reservation.controller;

import com.ceichhorst.reservation.dao.AdministratorDao;
import com.ceichhorst.reservation.dao.RestaurantDao;
import com.ceichhorst.reservation.entity.Administrator;
import com.ceichhorst.reservation.entity.Restaurant;
import com.ceichhorst.reservation.service.ServiceManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;

/**
 * Servlet responsible for managing restaurant data and administrator assignments.
 *
 * @author ceichhorst
 */
@WebServlet("/admin/restaurants")
public class RestaurantManagementServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(RestaurantManagementServlet.class);

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

            Restaurant selected = restaurantDao.getByIdWithAdmins(restaurantId);

            if (selected != null) {
                request.setAttribute("selectedRestaurant", selected);
                request.setAttribute("selectedRestaurantId", restaurantId);
            }
        }

        request.getRequestDispatcher("/WEB-INF/admin/restaurants.jsp")
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
                    logger.info("Restaurant info successfully updated");
                    request.getSession().setAttribute("successMessage", "Restaurant updated successfully!");
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

                    adminDao.addRestaurantAssociation(newAdmin.getId(), restaurantId);
                    logger.info("Admin successfully assigned to restaurant");
                    request.getSession().setAttribute("successMessage", "Admin added successfully!");
                    break;

                }

                case "removeAdmin": {
                    restaurantId = Long.parseLong(request.getParameter("restaurantId"));

                    serviceManager.authorizeAdminAccess(adminId, restaurantId);

                    Long removeAdminId = Long.parseLong(request.getParameter("adminId"));

                    Administrator adminToRemove = adminDao.getById(removeAdminId);

                    if (adminToRemove == null) {
                        throw new RuntimeException("Admin not found with that email.");
                    }

                    // Check to protect user from deleting themselves
                    if (adminId.equals(removeAdminId)) {
                        throw new RuntimeException("You cannot remove yourself from a restaurant.");
                    }

                    adminDao.removeRestaurantAssociation(removeAdminId, restaurantId);
                    logger.info("Admin assignment successfully removed from restaurant");
                    request.getSession().setAttribute("successMessage", "Admin removed successfully!");
                    break;
                }

            }
        } catch (RuntimeException e) {
            logger.error("Error while performing action", e);
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
