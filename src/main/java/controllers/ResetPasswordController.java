package controllers;

import daos.UserDao;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.User;

import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import java.util.Random;

public class ResetPasswordController extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String path = request.getRequestURI();

    if (path.equals("/forget-password")) {
      request.getRequestDispatcher("/forget_password.jsp").forward(request, response);
    } else if (path.startsWith("/verify")) {
      request.getRequestDispatcher("/verify.jsp").forward(request, response);
    } else if (path.startsWith("/reset-password")) {
      request.getRequestDispatcher("/reset_password.jsp").forward(request, response);
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    HttpSession session = request.getSession();
    String path = request.getRequestURI();

    if (path.startsWith("/forget-password")) {
      sendEmailCode(request, response, session);
    } else if (path.startsWith("/verify")) {
      verifyEmail(request, response, session);
    } else if (path.startsWith("/reset-password")) {
      resetPassword(request, response, session);
    }
  }

  private static void verifyEmail(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
    int value = Integer.parseInt(request.getParameter("otp"));

    int otp = 0;
    if (session.getAttribute("otp") != null) {
      otp = (int) session.getAttribute("otp");
    }

    if (otp == 0) {
      session.setAttribute("message", "error-verify-email");
      response.sendRedirect("/verify");
      return;
    }
    UserDao userDAO = new UserDao();

    if (value == otp) {
      session.removeAttribute("otp");

      String email = (String) session.getAttribute("email");
      User user = userDAO.getUserByEmail(email);
      session.setAttribute("user", user);
      session.setAttribute("triggerChangePassword", "true");
      response.sendRedirect("/reset-password");
    } else {
      session.setAttribute("message", "error-wrong-otp");
      response.sendRedirect("/verify");
    }
  }

  private void sendEmailCode(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ServletException, IOException {
    String email = request.getParameter("email");
    UserDao userDao = new UserDao();
    if (userDao.getUserByEmail(email) != null) {
      try {
        int otpvalue = 0;

        if (email != null && !email.equals("")) {
          // Sending OTP
          Random rand = new Random();
          otpvalue = rand.nextInt(1255650);

          String to = email; // Change accordingly

          // Get the session object
          Properties props = new Properties();
          props.put("mail.smtp.host", "smtp.gmail.com");
          props.put("mail.smtp.socketFactory.port", "465");
          props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
          props.put("mail.smtp.auth", "true");
          props.put("mail.smtp.port", "465");
          Session mailSession = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
              return new PasswordAuthentication("tnttfpt1@gmail.com", "wymd dtth xuyn saey"); // Put your company's email and password here
            }
          });

          // Compose message
          try {
            MimeMessage message = new MimeMessage(mailSession);
            message.setFrom(new InternetAddress("your-email@example.com")); // Change accordingly
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("Verify your Cozify user", "utf-8");
            message.setContent("Your verification code is: " + otpvalue + "<br />To prevent user loss, do not share this code.", "text/html; charset=utf-8");

            // Send message
            Transport.send(message);
          } catch (MessagingException e) {


            throw new RuntimeException(e);
          }
          System.out.println("OTP: " + otpvalue);
          request.setAttribute("message", "success-send-otp");
          session.setAttribute("otp", otpvalue);
          session.setAttribute("email", email);
          response.sendRedirect("/verify");
        }

      } catch (IOException e) {
        System.out.println("Could not send user register");
        session.setAttribute("message", "error-send-otp");
        response.sendRedirect("/forget-password");
      }
    } else {
      session.setAttribute("message", "error-no-email-found");
      response.sendRedirect("/forget-password");
    }
  }

  private void resetPassword(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ServletException, IOException {
    User user = (User) session.getAttribute("user");

    String password = request.getParameter("password") != null
        ? getMd5(request.getParameter("password"))
        : "";

    UserDao userDao = new UserDao();
    if (password.equals("")) {
      session.setAttribute("message", "error-change-password");
      response.sendRedirect("/reset-password");
      return;
    }
    user.setPassword(password);
    try {
      userDao.update(user);
      session.setAttribute("message", "success-change-password");
    } catch (RuntimeException e) {
      session.setAttribute("message", "error-change-password");
      response.sendRedirect("reset-password");
    }
    response.sendRedirect("/");
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
