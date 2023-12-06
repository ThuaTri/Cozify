package filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ImageProcessingFilter implements Filter {

  private static final boolean debug = true;

  // The filter configuration object we are associated with.  If
  // this value is null, this filter instance is not currently
  // configured. 
  private FilterConfig filterConfig = null;

  public ImageProcessingFilter() {
  }

  /**
   * @param request  The servlet request we are processing
   * @param response The servlet response we are creating
   * @param chain    The filter chain we are processing
   * @throws IOException      if an input/output error occurs
   * @throws ServletException if a servlet error occurs
   */
  public void doFilter(ServletRequest request, ServletResponse response,
                       FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;

    Throwable problem = null;
    try {
      // Get the request's method, only POST is allowed to process the image
      if (httpRequest.getMethod().equals("POST")) {
        // If request comes from /clothes/add, add the image
        // Otherwise, update the image if there is one
        if (httpRequest.getRequestURI().contains("/clothes/add")) {
          // Add the image
          addImage(httpRequest, httpResponse);
        } else if (httpRequest.getRequestURI().contains("/clothes/update")) {
          // Otherwise, update the image if there is one
          updateImage(httpRequest, httpResponse);
        }
      }
    } catch (Throwable t) {
      // If an exception is thrown somewhere down the filter chain,
      // we still want to execute our after processing, and then
      // rethrow the problem after that.
      problem = t;
      t.printStackTrace();
    }

    // If there was a problem, we want to rethrow it if it is
    // a known type, otherwise log it.
    if (problem != null) {
      if (problem instanceof ServletException) {
        throw (ServletException) problem;
      }
      if (problem instanceof IOException) {
        throw (IOException) problem;
      }
      sendProcessingError(problem, response);
    }
    chain.doFilter(request, response);
  }

  /**
   * Return the filter configuration object for this filter.
   */
  public FilterConfig getFilterConfig() {
    return (this.filterConfig);
  }

  /**
   * Set the filter configuration object for this filter.
   *
   * @param filterConfig The filter configuration object
   */
  public void setFilterConfig(FilterConfig filterConfig) {
    this.filterConfig = filterConfig;
  }

  /**
   * Destroy method for this filter
   */
  public void destroy() {
  }

  /**
   * Init method for this filter
   */
  public void init(FilterConfig filterConfig) {
    this.filterConfig = filterConfig;
    if (filterConfig != null) {
      if (debug) {
        log("ImageProcessing:Initializing filter");
      }
    }
  }

  /**
   * Return a String representation of this object.
   */
  @Override
  public String toString() {
    if (filterConfig == null) {
      return ("ImageProcessing()");
    }
    StringBuffer sb = new StringBuffer("ImageProcessing(");
    sb.append(filterConfig);
    sb.append(")");
    return (sb.toString());
  }

  private void sendProcessingError(Throwable t, ServletResponse response) {
    String stackTrace = getStackTrace(t);

    if (stackTrace != null && !stackTrace.equals("")) {
      try {
        response.setContentType("text/html");
        PrintStream ps = new PrintStream(response.getOutputStream());
        PrintWriter pw = new PrintWriter(ps);
        pw.print("<html>\n<head>\n<title>Error</title>\n</head>\n<body>\n"); //NOI18N

        // PENDING! Localize this for next official release
        pw.print("<h1>The resource did not process correctly</h1>\n<pre>\n");
        pw.print(stackTrace);
        pw.print("</pre></body>\n</html>"); //NOI18N
        pw.close();
        ps.close();
        response.getOutputStream().close();
      } catch (Exception ex) {
      }
    } else {
      try {
        PrintStream ps = new PrintStream(response.getOutputStream());
        t.printStackTrace(ps);
        ps.close();
        response.getOutputStream().close();
      } catch (Exception ex) {
      }
    }
  }

  public static String getStackTrace(Throwable t) {
    String stackTrace = null;
    try {
      StringWriter sw = new StringWriter();
      PrintWriter pw = new PrintWriter(sw);
      t.printStackTrace(pw);
      pw.close();
      sw.close();
      stackTrace = sw.getBuffer().toString();
    } catch (Exception ex) {
    }
    return stackTrace;
  }

  public void log(String msg) {
    filterConfig.getServletContext().log(msg);
  }

  public void addImage(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {

    String clothesName = request.getParameter("clothesName");

    // NOTE: Some implementations of HttpServletRequest defines that request.getPart()
    // returns null if there is no submitted file.
    // However, the Jakarta implementation returns Part object with content-type
    // "application/octet-stream" with size = -1 when there is no file.
    // Only process the image uploading if there is one
    // Skips the process if the update request does not change the image
    // Retrieves <input type="file" name="fileImage">
    Part filePart = request.getPart("image");
    if ((filePart != null)
        && ((!filePart.getContentType().equals("application/octet-stream")
        || ((filePart.getSize() != -1) && (filePart.getSize() != 0))))) {
      String imgFolderPath = "resources/media/img/clothes";
      // Absolute path of the web application
      String applicationPath = request.getServletContext().getRealPath("/");
      // Constructs path of the directory to save uploaded file
      String uploadFilePath = applicationPath + File.separator + imgFolderPath;

      // creates the save directory if it does not exists
      File fileSaveDir = new File(uploadFilePath);
      if (!fileSaveDir.exists()) {
        fileSaveDir.mkdirs();
      }

      // Gets image file extension
      String extension = filePart.getContentType();
      extension = extension.substring(extension.lastIndexOf("/") + 1);

      // Append "." to extension if it doesn't have one
      extension = (extension != null && !extension.equals("")) ? "." + extension : extension;
      if (extension.equals(".jpeg")) {
        extension = ".jpg";
      }
      String imgFileName = clothesName.replace(" ", "-").toLowerCase() + extension;

      // Validate image type
      String fileType = filePart.getContentType();
      fileType = fileType.substring(fileType.lastIndexOf("/") + 1);
      String mimeType = filePart.getContentType();
      ArrayList<String> allowedExtensions = new ArrayList<>();
      allowedExtensions.add("jpeg");
      allowedExtensions.add("jpg");

      ArrayList<String> allowedMimeTypes = new ArrayList<>();
      allowedMimeTypes.add("image/jpeg");
      allowedMimeTypes.add("image/pjpeg");

      // Uploaded file does not meet the validation
      if (!allowedExtensions.contains(fileType)
          || !allowedMimeTypes.contains(mimeType.toLowerCase())) {

        // Delete the uploaded file
        File file = new File(uploadFilePath, imgFileName);
        if (file.exists()) {
          file.delete();
        }
        // Prepare response information
        HttpSession session = request.getSession();
        session.setAttribute("message", "error-upload-image");
      }

      InputStream fileContent = filePart.getInputStream();
      // Create path components to save the file.
      File file = new File(uploadFilePath, imgFileName);
      final PrintWriter writer = response.getWriter();
      try {
        if (!file.exists()) {
          Files.copy(fileContent, file.toPath());
        }
      } catch (Exception e) {
        writer.println("<br/> ERROR: " + e);
      }
    }

    String requestURIContentType = request.getRequestURI().substring(request.getRequestURI().lastIndexOf(".") + 1);
    switch (requestURIContentType) {
      case "png" -> response.setContentType("image/png");
      case "jpg" -> response.setContentType("image/jpg");
      case "jpeg" -> response.setContentType("image/jpeg");
      case "gif" -> response.setContentType("image/gif");
      default -> response.setContentType("text/html");
    }
  }

  private void updateImage(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    String clothesName = request.getParameter("clothesName");

    // NOTE: Some implementations of HttpServletRequest defines that request.getPart()
    // returns null if there is no submitted file.
    // However, the Jakarta implementation returns Part object with content-type
    // "application/octet-stream" with size = -1 when there is no file.
    // Only process the image uploading if there is one
    // Skips the process if the update request does not change the image
    // Retrieves <input type="file" name="fileImage">
    Part filePart = request.getPart("image");
    if ((filePart != null)
        && ((!filePart.getContentType().equals("application/octet-stream")
        || ((filePart.getSize() != -1) && (filePart.getSize() != 0))))) {
      String imgFolderPath = "resources/media/img/clothes";
      // Absolute path of the web application
      String applicationPath = request.getServletContext().getRealPath("/");
      // Constructs path of the directory to save uploaded file
      String uploadFilePath = applicationPath + File.separator + imgFolderPath;

      deleteImage(uploadFilePath, clothesName.replace(" ", "-").toLowerCase() + ".jpg");
      addImage(request, response);
    }
  }

  private void deleteImage(String uploadPath, String imgName) {
    Path imagesPath = Paths.get(uploadPath + File.separator + imgName);

    try {
      Files.delete(imagesPath);
      System.out.println("File "
          + imagesPath.toAbsolutePath().toString()
          + " successfully removed");
    } catch (IOException e) {
      System.err.println("Unable to delete "
          + imagesPath.toAbsolutePath().toString()
          + " due to...");
      e.printStackTrace();
    }
  }

}
