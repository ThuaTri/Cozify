package controllers;

import daos.UserDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.User;

import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SignupController extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    request.getRequestDispatcher("/signup.jsp").forward(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    HttpSession session = request.getSession();
    String username = request.getParameter("username");
    String email = request.getParameter("email");
    String password = getMd5(request.getParameter("password"));

    UserDao userDao = new UserDao();
    try {
      // Check if the user with the same email already exists
      if (userDao.hasExistingEmail(email) || userDao.hasExistingUsername(username)) {
        session.setAttribute("message", "error-register-existing-email");
        response.sendRedirect("/signup");
      } else {
        User user = new User(username, password, email, (byte) 3);
        userDao.add(user);
        response.sendRedirect("/");
      }
    } catch (SQLException ex) {
      Logger.getLogger(SignupController.class.getName()).log(Level.SEVERE, null, ex);
      session.setAttribute("message", "error-register");
      response.sendRedirect("/signup");
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
