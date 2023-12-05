package controllers;

import com.microsoft.sqlserver.jdbc.StringUtils;
import daos.CategoryDao;
import daos.ClothesDao;
import daos.OrderDao;
import daos.UserDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Category;
import models.Clothes;
import models.Order;
import models.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class DashboardController extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String path = request.getRequestURI();
    // Redirect to pages depending on path's starting with /dashboard

    if (path.equals("/dashboard")) {
      request.getRequestDispatcher("/pages/dashboard/dashboard.jsp").forward(request, response);
    } else if (path.startsWith("/dashboard/category")) {
      redirectToCategory(request, response, "get");
    } else if (path.startsWith("/dashboard/clothes")) {
      ClothesDao clothesDao = new ClothesDao();
      List<Clothes> clothes = clothesDao.getAll();
      request.setAttribute("clothes", clothes);
      request.getRequestDispatcher("/pages/dashboard/clothes/clothes.jsp").forward(request, response);
    } else if (path.startsWith("/dashboard/user")) {
      UserDao userDao = new UserDao();
      List<User> users = userDao.getAllByRole("user");
      request.setAttribute("users", users);
      request.getRequestDispatcher("/pages/dashboard/user/user.jsp").forward(request, response);
    } else if (path.startsWith("/dashboard/order")) {
      OrderDao orderDao = new OrderDao();
      List<Order> orders = orderDao.getAll();
      request.setAttribute("orders", orders);
      request.getRequestDispatcher("/pages/dashboard/order/order.jsp").forward(request, response);
    } else if (path.startsWith("/dashboard/staff")) {
      UserDao userDao = new UserDao();
      List<User> staffs = userDao.getAllByRole("admin");
      staffs.addAll(userDao.getAllByRole("staff"));
      request.setAttribute("staffs", staffs);
      request.getRequestDispatcher("/pages/dashboard/staff/staff.jsp").forward(request, response);
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String path = request.getRequestURI();
    // Redirect to pages depending on path's starting with /dashboard

    if (path.equals("/dashboard")) {
      request.getRequestDispatcher("/pages/dashboard/dashboard.jsp").forward(request, response);
    } else if (path.startsWith("/dashboard/category")) {
      redirectToCategory(request, response, "post");
    } else if (path.startsWith("/dashboard/clothes")) {
      ClothesDao clothesDao = new ClothesDao();
      List<Clothes> clothes = clothesDao.getAll();
      request.setAttribute("clothes", clothes);
      request.getRequestDispatcher("/pages/dashboard/clothes/clothes.jsp").forward(request, response);
    } else if (path.startsWith("/dashboard/user")) {
      UserDao userDao = new UserDao();
      List<User> users = userDao.getAllByRole("user");
      request.setAttribute("users", users);
      request.getRequestDispatcher("/pages/dashboard/user/user.jsp").forward(request, response);
    } else if (path.startsWith("/dashboard/order")) {
      OrderDao orderDao = new OrderDao();
      List<Order> orders = orderDao.getAll();
      request.setAttribute("orders", orders);
      request.getRequestDispatcher("/pages/dashboard/order/order.jsp").forward(request, response);
    } else if (path.startsWith("/dashboard/staff")) {
      UserDao userDao = new UserDao();
      List<User> staffs = userDao.getAllByRole("admin");
      staffs.addAll(userDao.getAllByRole("staff"));
      request.setAttribute("staffs", staffs);
      request.getRequestDispatcher("/pages/dashboard/staff/staff.jsp").forward(request, response);
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
}
