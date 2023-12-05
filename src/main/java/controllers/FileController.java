package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;



public class FileController extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    /*String requestURIContentType = request.getRequestURI().substring(request.getRequestURI().lastIndexOf(".") + 1);
    switch (requestURIContentType) {
      case "png" -> response.setContentType("image/png");
      case "jpg" -> response.setContentType("image/jpg");
      case "jpeg" -> response.setContentType("image/jpeg");
      case "gif" -> response.setContentType("image/gif");
    }*/
    String filename = URLDecoder.decode(request.getPathInfo().substring(1), StandardCharsets.UTF_8);
    File file = new File(getServletContext().getRealPath("/resources/media/img/clothes"), filename);
    response.setHeader("Content-Type", getServletContext().getMimeType(filename));
    response.setHeader("Content-Length", String.valueOf(file.length()));
    response.setHeader("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");
    Files.copy(file.toPath(), response.getOutputStream());
  }
}
