package controllers;

import daos.CategoryDao;
import daos.ClothesDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class HomeController extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
        CategoryDao categoryDao = new CategoryDao();
    request.setAttribute("categories", categoryDao.getAllAvailable());

    ClothesDao clothesDao = new ClothesDao();
    if (request.getParameter("filter") != null) {
      int categoryId = Integer.parseInt(request.getParameter("filter"));
      request.setAttribute("clothes", clothesDao.getAllDistinctByCategory(categoryId));
    } else {
      request.setAttribute("clothes", clothesDao.getAllDistinct());
    }

    request.getRequestDispatcher("index.jsp").forward(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    request.getRequestDispatcher("index.jsp").forward(request, response);
  }
}
