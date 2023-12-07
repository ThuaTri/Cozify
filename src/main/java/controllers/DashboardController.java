package controllers;

import com.microsoft.sqlserver.jdbc.StringUtils;
import daos.CategoryDao;
import daos.ClothesDao;
import daos.OrderDao;
import daos.UserDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Category;
import models.Clothes;
import models.Order;
import models.User;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

@MultipartConfig
public class DashboardController extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String path = request.getRequestURI();
    // Redirect to pages depending on path's starting with /dashboard

    // Get user's role
    String role = "";
    HttpSession session = request.getSession();
    if (session != null) {
      role = ((User) session.getAttribute("user")).getRole();
    }
    if (path.equals("/dashboard")) {
      switch (role) {
        case "user" -> response.sendRedirect("/dashboard/order-history");
        case "admin", "staff" -> response.sendRedirect("/dashboard/category");
        default -> response.sendRedirect("/");
      }
    } else if (path.startsWith("/dashboard/order-history")) {
      redirectToOrderHistory(request, response, "get");
    } else if (path.startsWith("/dashboard/category")) {
      redirectToCategory(request, response, "get");
    } else if (path.startsWith("/dashboard/clothes")) {
      redirectToClothes(request, response, "get");
    } else if (path.startsWith("/dashboard/user")) {
      redirectToUser(request, response, "get");
    } else if (path.startsWith("/dashboard/order")) {
      redirectToOrder(request, response, "get");
    } else if (path.startsWith("/dashboard/staff")) {
      redirectToStaff(request, response, "get");
    }
  }


  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String path = request.getRequestURI();
    // Redirect to pages depending on path's starting with /dashboard

    if (path.equals("/dashboard")) {
      response.sendRedirect("/dashboard/category");
    } else if (path.startsWith("/dashboard/category")) {
      redirectToCategory(request, response, "post");
    } else if (path.startsWith("/dashboard/clothes")) {
      redirectToClothes(request, response, "post");
    } else if (path.startsWith("/dashboard/user")) {
      redirectToUser(request, response, "post");
    } else if (path.startsWith("/dashboard/order")) {
      redirectToOrder(request, response, "post");
    } else if (path.startsWith("/dashboard/staff")) {
      redirectToStaff(request, response, "post");
    }
  }

  private void redirectToOrderHistory(HttpServletRequest request, HttpServletResponse response, String method)
      throws ServletException, IOException {
    HttpSession session = request.getSession();

    // Get user's ID
    int userId = 0;
    if (session != null) {
      userId = ((User) session.getAttribute("user")).getUserId();
    }
    String path = request.getRequestURI();
    if (path.equals("/dashboard/order-history")) {
      OrderDao orderDao = new OrderDao();
      List<Order> orders = orderDao.getAllByUserId(userId);
      request.setAttribute("orders", orders);
      request.getRequestDispatcher("/pages/dashboard/order_history.jsp").forward(request, response);
    }
  }

  private void redirectToCategory(HttpServletRequest request, HttpServletResponse response, String method)
      throws ServletException, IOException {
    HttpSession session = request.getSession();
    String path = request.getRequestURI();
    if (path.equals("/dashboard/category")) {
      CategoryDao categoryDao = new CategoryDao();
      List<Category> categoryReports = categoryDao.getReportAll();
      request.setAttribute("categoryReports", categoryReports);
      request.getRequestDispatcher("/pages/dashboard/category/category.jsp").forward(request, response);
    } else if (path.startsWith("/dashboard/category/add")) {
      if (method.equals("post")) {
        String categoryName = request.getParameter("categoryName");
        boolean isHidden = !StringUtils.isEmpty(request.getParameter("isHidden"))
            && request.getParameter("isHidden").equals("true");
        CategoryDao categoryDao = new CategoryDao();
        Category category = new Category(categoryName, isHidden);
        try {
          categoryDao.add(category);
          session.setAttribute("message", "success-add-category");
        } catch (RuntimeException e) {
          session.setAttribute("message", "error-add-category");
          response.sendRedirect("/dashboard/category/add");
        }
        response.sendRedirect("/dashboard/category");
      } else {
        request.getRequestDispatcher("/pages/dashboard/category/category_add.jsp").forward(request, response);
      }
    } else if (path.startsWith("/dashboard/category/update")) {
      if (method.equals("post")) {
        int categoryId = Integer.parseInt(request.getParameter("id"));
        String categoryName = request.getParameter("categoryName");
        boolean isHidden = !StringUtils.isEmpty(request.getParameter("isHidden"))
            && request.getParameter("isHidden").equals("true");
        CategoryDao categoryDao = new CategoryDao();
        Category category = new Category(categoryId, categoryName, isHidden);
        try {
          categoryDao.update(category);
          session.setAttribute("message", "success-update-category");
        } catch (RuntimeException e) {
          session.setAttribute("message", "error-update-category");
          response.sendRedirect("/dashboard/category/update");
        }
        response.sendRedirect("/dashboard/category");
      } else {
        int categoryId = Integer.parseInt(request.getParameter("id"));
        CategoryDao categoryDao = new CategoryDao();
        Category category = categoryDao.getById(categoryId);
        request.setAttribute("category", category);
        request.getRequestDispatcher("/pages/dashboard/category/category_update.jsp").forward(request, response);
      }
    } else if (path.startsWith("/dashboard/category/delete")) {
      int categoryId = Integer.parseInt(request.getParameter("id"));
      CategoryDao categoryDao = new CategoryDao();
      try {
        categoryDao.delete(categoryId);
        session.setAttribute("message", "success-delete-category");
      } catch (RuntimeException e) {
        session.setAttribute("message", "error-delete-category");
      }
      response.sendRedirect("/dashboard/category");
    }
  }

  private void redirectToClothes(HttpServletRequest request, HttpServletResponse response, String method)
      throws ServletException, IOException {
    HttpSession session = request.getSession();
    String path = request.getRequestURI();
    if (path.equals("/dashboard/clothes")) {
      ClothesDao clothesDao = new ClothesDao();
      List<Clothes> clothes = clothesDao.getAll();
      request.setAttribute("clothes", clothes);
      request.getRequestDispatcher("/pages/dashboard/clothes/clothes.jsp").forward(request, response);
    } else if (path.startsWith("/dashboard/clothes/add")) {
      if (method.equals("post")) {
        int categoryId = Integer.parseInt(request.getParameter("categoryId"));
        String clothesName = request.getParameter("clothesName");
        BigDecimal price = BigDecimal.valueOf(Double.parseDouble(request.getParameter("price")));
        int discount = Integer.parseInt(request.getParameter("discount"));
        int rating = Integer.parseInt(request.getParameter("rating"));
        int stockQuantity = Integer.parseInt(request.getParameter("stockQuantity"));
        String size = request.getParameter("size");
        boolean isHidden = !StringUtils.isEmpty(request.getParameter("isHidden"))
            && request.getParameter("isHidden").equals("true");

        ClothesDao clothesDao = new ClothesDao();
        Clothes clothes = new Clothes(clothesName, price, discount, rating, stockQuantity, size, isHidden, categoryId);
        try {
          clothesDao.add(clothes);
          session.setAttribute("message", "success-add-clothes");
        } catch (RuntimeException e) {
          session.setAttribute("message", "error-add-clothes");
          response.sendRedirect("/dashboard/clothes/add");
        }
        response.sendRedirect("/dashboard/clothes");
      } else {
        CategoryDao categoryDao = new CategoryDao();
        List<Category> categories = categoryDao.getAllAvailable();
        request.setAttribute("categories", categories);
        request.getRequestDispatcher("/pages/dashboard/clothes/clothes_add.jsp").forward(request, response);
      }
    } else if (path.startsWith("/dashboard/clothes/update")) {
      if (method.equals("post")) {
        int clothesId = Integer.parseInt(request.getParameter("id"));
        int categoryId = Integer.parseInt(request.getParameter("categoryId"));
        String clothesName = request.getParameter("clothesName");
        BigDecimal price = BigDecimal.valueOf(Double.parseDouble(request.getParameter("price")));
        int discount = Integer.parseInt(request.getParameter("discount"));
        int rating = Integer.parseInt(request.getParameter("rating"));
        int stockQuantity = Integer.parseInt(request.getParameter("stockQuantity"));
        String size = request.getParameter("size");
        boolean isHidden = !StringUtils.isEmpty(request.getParameter("isHidden"))
            && request.getParameter("isHidden").equals("true");

        ClothesDao clothesDao = new ClothesDao();
        Clothes clothes = new Clothes(clothesId, clothesName, price, discount, rating, stockQuantity, size, isHidden, categoryId);
        try {
          clothesDao.update(clothes);
          session.setAttribute("message", "success-update-clothes");
        } catch (RuntimeException e) {
          session.setAttribute("message", "error-update-clothes");
          response.sendRedirect("/dashboard/clothes/update");
        }
        response.sendRedirect("/dashboard/clothes");
      } else {
        int clothesId = Integer.parseInt(request.getParameter("id"));

        ClothesDao clothesDao = new ClothesDao();
        Clothes clothes = clothesDao.getById(clothesId);
        CategoryDao categoryDao = new CategoryDao();
        List<Category> categories = categoryDao.getAllAvailable();

        request.setAttribute("categories", categories);
        request.setAttribute("clothes", clothes);
        request.getRequestDispatcher("/pages/dashboard/clothes/clothes_update.jsp").forward(request, response);
      }
    } else if (path.startsWith("/dashboard/clothes/delete")) {
      int clothesId = Integer.parseInt(request.getParameter("id"));
      ClothesDao clothesDao = new ClothesDao();
      try {
        clothesDao.delete(clothesId);
        session.setAttribute("message", "success-delete-clothes");
      } catch (RuntimeException e) {
        session.setAttribute("message", "error-delete-clothes");
      }
      response.sendRedirect("/dashboard/clothes");
    }
  }

  private void redirectToUser(HttpServletRequest request, HttpServletResponse response, String method)
      throws ServletException, IOException {
    HttpSession session = request.getSession();
    String path = request.getRequestURI();
    if (path.equals("/dashboard/user")) {
      UserDao userDao = new UserDao();
      List<User> users = userDao.getAllByRole((byte) 3);
      request.setAttribute("users", users);
      request.getRequestDispatcher("/pages/dashboard/user/user.jsp").forward(request, response);
    } else if (path.startsWith("/dashboard/user/update")) {
      if (method.equals("post")) {
        int userId = Integer.parseInt(request.getParameter("id"));
        String username = request.getParameter("username");
        String email = request.getParameter("email");

        // Optional fields
        String password = request.getParameter("password") != null
            ? getMd5(request.getParameter("password"))
            : "";
        String firstName = request.getParameter("firstName") != null
            ? request.getParameter("firstName")
            : "";
        String lastName = request.getParameter("lastName") != null
            ? request.getParameter("lastName")
            : "";
        String phoneNumber = request.getParameter("phoneNumber") != null
            ? request.getParameter("phoneNumber")
            : "";
        String address = request.getParameter("address") != null
            ? request.getParameter("address")
            : "";

        UserDao userDao = new UserDao();
        User user = userDao.getById(userId);
        user.setUsername(username);
        user.setEmail(email);
        if (!password.isBlank()) {
          user.setPassword(password);
        }
        if (!firstName.isBlank()) {
          user.setFirstName(firstName);
        }
        if (!lastName.isBlank()) {
          user.setLastName(lastName);
        }
        if (!phoneNumber.isBlank()) {
          user.setPhoneNumber(phoneNumber);
        }
        if (!address.isBlank()) {
          user.setAddress(address);
        }

        try {
          userDao.update(user);
          session.setAttribute("message", "success-update-user");
        } catch (RuntimeException e) {
          session.setAttribute("message", "error-update-user");
          response.sendRedirect("/dashboard/user/update");
        }
        response.sendRedirect("/dashboard/user");
      } else {
        int userId = Integer.parseInt(request.getParameter("id"));

        UserDao userDao = new UserDao();
        User user = userDao.getById(userId);

        request.setAttribute("user", user);
        request.getRequestDispatcher("/pages/dashboard/user/user_update.jsp").forward(request, response);
      }
    } else if (path.startsWith("/dashboard/user/delete")) {
      int userId = Integer.parseInt(request.getParameter("id"));
      UserDao userDao = new UserDao();
      try {
        userDao.delete(userId);
        session.setAttribute("message", "success-delete-user");
      } catch (RuntimeException e) {
        session.setAttribute("message", "error-delete-user");
      }
      response.sendRedirect("/dashboard/user");
    }
  }

  private void redirectToOrder(HttpServletRequest request, HttpServletResponse response, String method)
      throws ServletException, IOException {
    HttpSession session = request.getSession();
    String path = request.getRequestURI();
    if (path.equals("/dashboard/order")) {
      OrderDao orderDao = new OrderDao();
      List<Order> orders = orderDao.getAll();
      request.setAttribute("orders", orders);
      request.getRequestDispatcher("/pages/dashboard/order/order.jsp").forward(request, response);
    } else if (path.startsWith("/dashboard/order/update")) {
      if (method.equals("post")) {
        int orderId = Integer.parseInt(request.getParameter("id"));
        String status = request.getParameter("status");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String address = request.getParameter("address");
        String phoneNumber = request.getParameter("phoneNumber");
        String email = request.getParameter("email");

        // Optional fields
        String note = request.getParameter("note") != null
            ? request.getParameter("note")
            : "";

        OrderDao orderDao = new OrderDao();
        Order order = orderDao.getById(orderId);
        order.setStatus(status);
        order.setFirstName(firstName);
        order.setLastName(lastName);
        order.setAddress(address);
        order.setPhoneNumber(phoneNumber);
        order.setEmail(email);
        if (!note.isBlank()) {
          order.setNote(note);
        }

        try {
          orderDao.update(order);
          session.setAttribute("message", "success-update-order");
        } catch (RuntimeException e) {
          session.setAttribute("message", "error-update-order");
          response.sendRedirect("/dashboard/order/update");
          return;
        }
        response.sendRedirect("/dashboard/order");
      } else {
        int orderId = Integer.parseInt(request.getParameter("id"));

        OrderDao orderDao = new OrderDao();
        Order order = orderDao.getById(orderId);

        List<String> statuses = Arrays.asList("pending", "packaging", "delivering", "delivered", "cancelled");

        request.setAttribute("order", order);
        request.setAttribute("statuses", statuses);
        request.getRequestDispatcher("/pages/dashboard/order/order_update.jsp").forward(request, response);
      }
    } else if (path.startsWith("/dashboard/order/cancel")) {
      int orderId = Integer.parseInt(request.getParameter("id"));
      OrderDao orderDao = new OrderDao();
      try {
        orderDao.cancel(orderId);
        session.setAttribute("message", "success-cancel-order");
      } catch (RuntimeException e) {
        session.setAttribute("message", "error-cancel-order");
      }
      response.sendRedirect("/dashboard/order-history");
    }
  }

  private void redirectToStaff(HttpServletRequest request, HttpServletResponse response, String method)
      throws ServletException, IOException {
    HttpSession session = request.getSession();
    String path = request.getRequestURI();
    if (path.equals("/dashboard/staff")) {
      UserDao userDao = new UserDao();
      List<User> staffs = userDao.getAllByRole((byte) 1);
      staffs.addAll(userDao.getAllByRole((byte) 2));
      request.setAttribute("staffs", staffs);
      request.getRequestDispatcher("/pages/dashboard/staff/staff.jsp").forward(request, response);
    } else if (path.startsWith("/dashboard/staff/add")) {
      if (method.equals("post")) {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        byte roleId = Byte.parseByte(request.getParameter("role"));
        String password = getMd5(request.getParameter("password"));

        // Optional fields
        String firstName = request.getParameter("firstName") != null
            ? request.getParameter("firstName")
            : "";
        String lastName = request.getParameter("lastName") != null
            ? request.getParameter("lastName")
            : "";
        String phoneNumber = request.getParameter("phoneNumber") != null
            ? request.getParameter("phoneNumber")
            : "";
        String address = request.getParameter("address") != null
            ? request.getParameter("address")
            : "";

        UserDao userDao = new UserDao();
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setRoleId(roleId);
        user.setPassword(password);

        if (!firstName.isBlank()) {
          user.setFirstName(firstName);
        }
        if (!lastName.isBlank()) {
          user.setLastName(lastName);
        }
        if (!phoneNumber.isBlank()) {
          user.setPhoneNumber(phoneNumber);
        }
        if (!address.isBlank()) {
          user.setAddress(address);
        }

        try {
          userDao.add(user);
          session.setAttribute("message", "success-add-staff");
        } catch (RuntimeException e) {
          session.setAttribute("message", "error-add-staff");
          response.sendRedirect("/dashboard/staff/update");
        }
        response.sendRedirect("/dashboard/staff");
      } else {
        request.getRequestDispatcher("/pages/dashboard/staff/staff_add.jsp").forward(request, response);
      }
    } else if (path.startsWith("/dashboard/staff/update")) {
      if (method.equals("post")) {
        int userId = Integer.parseInt(request.getParameter("id"));
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        byte roleId = Byte.parseByte(request.getParameter("role"));

        // Optional fields
        String password = request.getParameter("password") != null
            ? getMd5(request.getParameter("password"))
            : "";
        String firstName = request.getParameter("firstName") != null
            ? request.getParameter("firstName")
            : "";
        String lastName = request.getParameter("lastName") != null
            ? request.getParameter("lastName")
            : "";
        String phoneNumber = request.getParameter("phoneNumber") != null
            ? request.getParameter("phoneNumber")
            : "";
        String address = request.getParameter("address") != null
            ? request.getParameter("address")
            : "";

        UserDao userDao = new UserDao();
        User user = userDao.getById(userId);
        user.setUsername(username);
        user.setEmail(email);
        user.setRoleId(roleId);
        if (!password.isBlank()) {
          user.setPassword(password);
        }
        if (!firstName.isBlank()) {
          user.setFirstName(firstName);
        }
        if (!lastName.isBlank()) {
          user.setLastName(lastName);
        }
        if (!phoneNumber.isBlank()) {
          user.setPhoneNumber(phoneNumber);
        }
        if (!address.isBlank()) {
          user.setAddress(address);
        }

        try {
          userDao.update(user);
          session.setAttribute("message", "success-update-staff");
        } catch (RuntimeException e) {
          session.setAttribute("message", "error-update-staff");
          response.sendRedirect("/dashboard/staff/update");
        }
        response.sendRedirect("/dashboard/staff");
      } else {
        int userId = Integer.parseInt(request.getParameter("id"));

        UserDao userDao = new UserDao();
        User staff = userDao.getById(userId);

        request.setAttribute("staff", staff);
        request.getRequestDispatcher("/pages/dashboard/staff/staff_update.jsp").forward(request, response);
      }
    } else if (path.startsWith("/dashboard/staff/delete")) {
      int userId = Integer.parseInt(request.getParameter("id"));
      UserDao userDao = new UserDao();
      try {
        userDao.delete(userId);
        session.setAttribute("message", "success-delete-staff");
      } catch (RuntimeException e) {
        session.setAttribute("message", "error-delete-staff");
      }
      response.sendRedirect("/dashboard/staff");
    }
  }

  private String getMd5(String input) {
    try {
      java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
      byte[] messageDigest = md.digest(input.getBytes());
      BigInteger no = new BigInteger(1, messageDigest);
      String hashtext = no.toString(16);
      while (hashtext.length() < 32) {
        hashtext = "0" + hashtext;
      }
      return hashtext;
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }
}
