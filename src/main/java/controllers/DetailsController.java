package controllers;

import daos.ClothesDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Clothes;

import java.io.IOException;
import java.util.List;

public class DetailsController extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    HttpSession session = request.getSession();
    String path = request.getRequestURI();
    int id = -1;
    try {
      // There are 2 ways to retrieve the clothes' ID:
      // 1st method is directly from the URL (used to display the details page):
      // 2nd method is used to add/update/delete an item in the cart. This method uses parameters:
      id = request.getParameter("id") != null && !request.getParameter("id").isEmpty()
          ? Integer.parseInt(request.getParameter("id"))
          : Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));

    } catch (NumberFormatException e) {
      System.out.println("Error: " + e.getMessage());
      System.out.println("This might be due to a malformed URL.");
      response.sendRedirect("/");
    }

    ClothesDao clothesDao = new ClothesDao();
    Clothes clothes = id != -1
        ? clothesDao.getById(id)
        : null;
    List<Clothes> clothesList = clothesDao.getRandom4();

    List<String> availableSizes = clothesDao.getAvailableSizes(clothes);

    request.setAttribute("clothes", clothes);
    request.setAttribute("clothesList", clothesList);
    request.setAttribute("availableSizes", availableSizes);
    session.setAttribute("previousUrl", path);
    request.getRequestDispatcher("/details.jsp").forward(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

  }
}
