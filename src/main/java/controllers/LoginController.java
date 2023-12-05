package controllers;

import daos.UserDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import models.User;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginController extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    request.getRequestDispatcher("/login.jsp").forward(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    HttpSession session = request.getSession();
    String email = request.getParameter("email");
    String password = getMd5(request.getParameter("password"));

    UserDao userDao = new UserDao();
    try {
      // Check if the user with the same email already exists
      if (userDao.login(email, password)) {
        User user = userDao.getUserByEmail(email);

        // Serialize the user so that it can be stored in a cookie
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(bos)) {
          oos.writeObject(user);
          oos.flush();
        }
        String serializedUser = Base64.getEncoder().encodeToString(bos.toByteArray());

        Cookie cookie = new Cookie("user", serializedUser);
        cookie.setPath("/");
        // 7 days
        int cookieAge = 24 * 60 * 60 * 7;
        cookie.setMaxAge(cookieAge);
        response.addCookie(cookie);

        redirectToPage(request, response, user.getRole());
      } else {
        session.setAttribute("message", "error-login-credentials");
      }
    } catch (SQLException ex) {
      Logger.getLogger(SignupController.class.getName()).log(Level.SEVERE, null, ex);
      session.setAttribute("message", "error-login");
      redirectToPage(request, response);
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


  private void redirectToPage(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    response.sendRedirect("/login");
  }

  private void redirectToPage(HttpServletRequest request, HttpServletResponse response, String role)
      throws ServletException, IOException {
    if (role.equals("admin")) {
      response.sendRedirect("/dashboard");
    } else if (role.equals("user")) {
      response.sendRedirect("/");
    } else {
      response.sendRedirect("/login");
    }
  }
}
