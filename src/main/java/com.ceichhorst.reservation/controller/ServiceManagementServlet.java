package com.ceichhorst.reservation.controller;

import com.ceichhorst.reservation.dao.ServiceInstanceDao;
import com.ceichhorst.reservation.dao.AdministratorDao;
import com.ceichhorst.reservation.dao.RestaurantDao;
import com.ceichhorst.reservation.entity.Restaurant;
import com.ceichhorst.reservation.entity.ServiceInstance;
import com.ceichhorst.reservation.service.ServiceManager;
import com.ceichhorst.reservation.service.ServiceTimeFormatter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servlet responsible for managing {@link ServiceInstance} records
 * in the administrative interface.
 *
 * <p>This servlet allows administrators to view, filter, create, update,
 * and delete service instances (i.e., bookable time slots) for restaurants
 * they are authorized to manage.</p>
 *
 * <p><strong>Responsibilities:</strong></p>
 * <ul>
 *   <li>Authenticate and validate administrator session</li>
 *   <li>Load restaurants assigned to the administrator</li>
 *   <li>Display and filter service instances for a selected restaurant</li>
 *   <li>Handle administrative actions such as adding, deleting, and updating services</li>
 *   <li>Delegate business logic to {@link ServiceManager}</li>
 * </ul>
 *
 * <p>This servlet delegates business logic to {@link ServiceManager} and
 * data access to {@link ServiceInstanceDao} and {@link AdministratorDao}.</p>
 *
 * @author ceichhorst
 */
@WebServlet("/admin/services")
public class ServiceManagementServlet extends HttpServlet {

    /**
     * DAO for retrieving {@link ServiceInstance} data.
     */
    private ServiceInstanceDao serviceDao = new ServiceInstanceDao();

    /**
     * DAO for retrieving administrator and restaurant associations.
     */
    private AdministratorDao adminDao = new AdministratorDao();

    /**
     * Service layer handling business logic for service management.
     */
    private ServiceManager serviceManager = new ServiceManager();

    /**
     * Utility for formatting service times for display.
     */
    private final ServiceTimeFormatter formatter = new ServiceTimeFormatter();

    private static final Logger logger = LogManager.getLogger(ServiceManagementServlet.class);

    /**
     * Handles HTTP GET requests to display and filter service instances.
     *
     * <p>This method performs the following steps:</p>
     * <ol>
     *   <li>Validates administrator session</li>
     *   <li>Retrieves restaurants assigned to the administrator</li>
     *   <li>If a restaurant is selected:
     *     <ul>
     *       <li>Loads associated service instances</li>
     *       <li>Applies optional filtering (by date or month)</li>
     *       <li>Formats service times for display</li>
     *       <li>Determines scheduling type for UI rendering</li>
     *     </ul>
     *   </li>
     *   <li>Forwards data to the services management JSP</li>
     * </ol>
     *
     * <p><strong>Optional request parameters:</strong></p>
     * <ul>
     *   <li>{@code restaurantId} → selected restaurant</li>
     *   <li>{@code filterType} → filtering mode ({@code DATE}, {@code MONTH}, {@code ALL})</li>
     *   <li>{@code filterDate} → specific date (ISO-8601 format)</li>
     *   <li>{@code filterMonth} → year-month format (e.g., {@code yyyy-MM})</li>
     * </ul>
     *
     * <p><strong>Request attributes set:</strong></p>
     * <ul>
     *   <li>{@code restaurants} → list of accessible {@link Restaurant}</li>
     *   <li>{@code services} → filtered list of {@link ServiceInstance}</li>
     *   <li>{@code selectedRestaurantId}</li>
     *   <li>{@code scheduleType} → scheduling type of selected restaurant</li>
     * </ul>
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @throws ServletException if forwarding fails
     * @throws IOException if an I/O error occurs
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

        // Get restaurants assigned to admin
        List<Restaurant> restaurants = adminDao.getRestaurantByAdminId(adminId);
        request.setAttribute("restaurants", restaurants);

        // Selected restaurant
        String restaurantIdParam = request.getParameter("restaurantId");

        if (restaurantIdParam != null && !restaurantIdParam.isEmpty()) {
            final Long restaurantId = Long.parseLong(restaurantIdParam);

            // Filtering variables for Existing Services table
            String filterType = request.getParameter("filterType");
            String filterDate = request.getParameter("date");
            String filterMonth = request.getParameter("month");

            List<ServiceInstance> services = serviceDao.getByRestaurantId(restaurantId);

            // Filtering
            if (filterType != null) {
                switch (filterType) {
                    case "DATE":
                        if (filterDate != null && !filterDate.isEmpty()) {
                            LocalDate date = LocalDate.parse(filterDate);
                            services = services.stream()
                                    .filter(s -> s.getServiceDate().equals(date))
                                    .collect(Collectors.toList());
                        }
                        break;

                    case "MONTH":
                        if (filterDate != null && !filterDate.isEmpty()) {
                            String[] parts = filterMonth.split("-");
                            int year = Integer.parseInt(parts[0]);
                            int month = Integer.parseInt(parts[1]);
                            services = services.stream()
                                    .filter(s ->
                                            s.getServiceDate().getYear() == year &&
                                            s.getServiceDate().getMonthValue() == month
                                    )
                                    .collect(Collectors.toList());
                        }
                        break;

                    case "ALL":
                    default:
                        break;
                }
            }

            services = formatter.formatTimes(services);

            request.setAttribute("services", services);
            request.setAttribute("selectedRestaurantId", restaurantId);

            Restaurant selected = new RestaurantDao().getById(restaurantId);

            if (selected != null) {
                request.setAttribute("scheduleType", selected.getSchedulingType());
            }
        }

        request.getRequestDispatcher("/WEB-INF/admin/services.jsp")
                .forward(request, response);
    }

    /**
     * Handles HTTP POST requests to perform administrative actions on services.
     *
     * <p>Supported actions (via {@code action} parameter):</p>
     * <ul>
     *   <li>{@code addService} → create a new service instance</li>
     *   <li>{@code bulkDelete} → delete multiple service instances</li>
     *   <li>{@code updateService} → update restaurant scheduling type</li>
     *   <li>{@code bulkToggleVisibility} → toggle visibility of multiple services</li>
     * </ul>
     *
     * <p>All actions are validated through {@link ServiceManager}, which enforces
     * authorization and business rules.</p>
     *
     * @param request the HTTP request containing action and form data
     * @param response the HTTP response
     * @throws ServletException if processing fails
     * @throws IOException if an I/O error occurs
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
                case "addService": {
                    restaurantId = Long.parseLong(request.getParameter("restaurantId"));
                    LocalDate date = LocalDate.parse(request.getParameter("date"));
                    LocalTime time = LocalTime.parse(request.getParameter("time"));
                    LocalTime endTime = LocalTime.parse(request.getParameter("endTime"));
                    int capacity = Integer.parseInt(request.getParameter("capacity"));

                    serviceManager.addService(adminId, restaurantId, date, time, endTime, capacity);
                    logger.info("Service date successfully added");
                    break;
                }

                case "bulkDelete": {
                    String[] serviceIds = request.getParameterValues("serviceIds");

                    if (serviceIds != null) {
                        for (String id : serviceIds) {
                            serviceManager.deleteService(adminId, Long.parseLong(id));
                        }
                    }

                    restaurantId = Long.parseLong(request.getParameter("restaurantId"));
                    logger.info("Service date(s) successfully deleted");
                    break;
                }

                case "updateService": {
                    restaurantId = Long.parseLong(request.getParameter("restaurantId"));
                    String schedulingType = request.getParameter("scheduleType");
                    serviceManager.updateSchedulingType(adminId, restaurantId, schedulingType);
                    logger.info("Service type fo restaurant successfully updated");
                    break;
                }

                case "bulkToggleVisibility": {
                    String[] serviceIds = request.getParameterValues("serviceIds");

                    if (serviceIds != null) {
                        for (String id : serviceIds) {
                            serviceManager.toggleVisibility(adminId, Long.parseLong(id));
                        }
                    }

                    restaurantId = Long.parseLong(request.getParameter("restaurantId"));
                    logger.info("Service date visibility successfully toggled");
                    break;
                }
            }

        } catch (RuntimeException e) {
            logger.error("Error occurred during action");
            request.getSession().setAttribute("error", e.getMessage());
        }

        if (restaurantId != null) {
            response.sendRedirect(
                    request.getContextPath() + "/admin/services?restaurantId=" + restaurantId
            );
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/services");
        }

    }
}
