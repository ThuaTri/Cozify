package filters;

import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.User;

import java.io.*;
import java.util.Arrays;
import java.util.Base64;

public class GlobalRequestAttributeFilter implements Filter {
  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    Filter.super.init(filterConfig);
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;
    HttpSession session = ((HttpServletRequest) request).getSession();

    Throwable problem = null;

    try {
      // Change message scope from session to request
      if (session.getAttribute("message") != null) {
        String message = (String) session.getAttribute("message");
        request.setAttribute("message", message);
        session.removeAttribute("message");
      }

      String applicationPath = request.getServletContext().getRealPath("/");
      request.setAttribute("applicationPath", applicationPath);

      chain.doFilter(request, response);

    } catch (Throwable t) {
      // If an exception is thrown somewhere down the filter chain,
      // we still want to execute our after processing, and then
      // rethrow the problem after that.
      problem = t;
      t.printStackTrace();
    }

    // If there was a problem, we want to rethrow it if it is
    // a known type, otherwise log it.
    if (problem
        != null) {
      if (problem instanceof ServletException) {
        throw (ServletException) problem;
      }
      if (problem instanceof IOException) {
        throw (IOException) problem;
      }
      sendProcessingError(problem, response);
    }
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

  @Override
  public void destroy() {
    Filter.super.destroy();
  }
}
