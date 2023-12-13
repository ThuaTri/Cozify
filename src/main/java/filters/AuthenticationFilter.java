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

public class AuthenticationFilter implements Filter {

  // The filter configuration object we are associated with.  If
  // this value is null, this filter instance is not currently
  // configured. 
  private FilterConfig filterConfig = null;

  private final String[] IGNORED_URLS = {
      "/signup",
      "/login"
  };

  private final String[] ADMIN_ONLY_DESTINATIONS = {
      "/dashboard/category",
      "/dashboard/clothes",
      "/dashboard/voucher",
      "/dashboard/order",
      "/dashboard/user",
      "/dashboard/staff"
  };

  private final String[] USER_ONLY_DESTINATIONS = {
      "/",
      "/cart",
      "/checkout",
      "/dashboard/order-history"
  };

  private final String[] STAFF_ONLY_DESTINATIONS = {
      "/dashboard/category",
      "/dashboard/clothes",
      "/dashboard/voucher",
      "/dashboard/order"
  };

  private final String[] USER_RESTRICTED_DESTINATIONS = {
      "/cart",
      "/checkout",
      "/dashboard/order-history"
  };

  public AuthenticationFilter() {
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
      String path = httpRequest.getRequestURI();
      boolean isIgnored = Arrays.stream(IGNORED_URLS)
          .anyMatch(path::startsWith);
      boolean isUserRestrictedDestination = Arrays.stream(USER_RESTRICTED_DESTINATIONS)
          .anyMatch(path::startsWith);

      if (!isIgnored) {
        HttpSession session = httpRequest.getSession();

        if (path.endsWith("/logout")) {
          logout(httpRequest, httpResponse);
          return;
        }

        boolean hasUserSession = session.getAttribute("user") != null;
        // Set up cookies if this filter is invoked from the login page
        if (hasUserSession) {
          request.setAttribute("isLoggedIn", true);
        } else {
          Cookie[] cookies = httpRequest.getCookies();
          if (cookies != null) {
            for (Cookie cookie : cookies) {
              if (cookie.getName().equals("user")) {
                String cookieValue = cookie.getValue();

                // Decode the cookie value if necessary
                byte[] decodedBytes = Base64.getDecoder().decode(cookieValue);

                // Deserialize the cookie value into an object
                Object user = null;
                try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(decodedBytes))) {
                  user = ois.readObject();
                } catch (ClassNotFoundException | IOException e) {
                  e.printStackTrace();
                }

                session.setAttribute("user", user);
                request.setAttribute("isLoggedIn", true);
              }
            }
          }
        }

        // If the user is not logged in while accessing a restricted destination, redirect to login
        if (isUserRestrictedDestination && session.getAttribute("user") == null) {
          httpResponse.sendRedirect("/login");
          return;
        }

        // Get the destination from the request
        String destination = ((HttpServletRequest) request).getRequestURI();

        // Check the role of the logged in user
        if (session.getAttribute("user") != null) {
          User user = (User) session.getAttribute("user");


          // Get the user's role, then decide where to redirect
          switch (user.getRole()) {
            case "admin" -> {
              if (Arrays.stream(ADMIN_ONLY_DESTINATIONS).noneMatch(destination::startsWith)) {
                if (!destination.equals("/dashboard")) {
                  httpResponse.sendRedirect("/dashboard");
                  return;
                }
              }
            }
            case "staff" -> {
              if (Arrays.stream(STAFF_ONLY_DESTINATIONS).noneMatch(destination::startsWith)) {
                // DashboardController will automatically redirect to staff pages
                // so no need to redirect
                if (!destination.equals("/dashboard")) {
                  httpResponse.sendRedirect("/dashboard");
                  return;
                }
              }
            }
            case "user" -> {
              if (Arrays.stream(USER_ONLY_DESTINATIONS).noneMatch(destination::startsWith)) {
                // Just like the case of staff, users will automatically redirect to home page
                if (!destination.equals("/dashboard")) {
                  httpResponse.sendRedirect("/dashboard");
                  return;
                }
                if (!destination.equals("/")) {
                  httpResponse.sendRedirect("/");
                  return;
                }
              }
            }
          }
        } else { // User is not logged in while attempting to access restricted destination, be it admin or staff
          // In the case of cart or checkout, redirect to login
          if (Arrays.stream(USER_RESTRICTED_DESTINATIONS).anyMatch(destination::startsWith)) {
            httpResponse.sendRedirect("/login");
            return;
          }

          // In the case of admin or staff, redirect to home page
          if (Arrays.stream(ADMIN_ONLY_DESTINATIONS).anyMatch(destination::startsWith)) {
            httpResponse.sendRedirect("/");
            return;
          }
        }
      }

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
  }

  /**
   * Return a String representation of this object.
   */
  @Override
  public String toString() {
    if (filterConfig == null) {
      return ("AuthenticationFilter()");
    }
    String sb = "AuthenticationFilter(" + filterConfig +
        ")";
    return (sb);
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

  public void logout(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        // Delete all cookies except the user's cart cookie (which only contains digits)
        if (!cookie.getName().matches("\\d+")) {
          Cookie newCookie = new Cookie(cookie.getName(), null);
          newCookie.setMaxAge(0);
          newCookie.setPath("/");
          response.addCookie(newCookie);
        }
      }
    }
    HttpSession session = request.getSession();
    session.invalidate(); // destroy session
    String contextPath = request.getContextPath().replace("/logout", "");
    response.sendRedirect("/" + contextPath);
  }
}
