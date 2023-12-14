package controllers;

import com.microsoft.sqlserver.jdbc.StringUtils;
import daos.ClothesDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import models.Cart;
import models.Clothes;
import models.OrderItem;
import models.User;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

public class CartController extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String path = request.getRequestURI();
    if (path.endsWith("/cart")) {
      request.getRequestDispatcher("cart.jsp").forward(request, response);
    } else if (path.startsWith("/cart/add")) {
      addItemToCart(request, response);
    } else if (path.startsWith("/cart/update")) {
      updateItemInCart(request, response);
    } else if (path.startsWith("/cart/delete")) {
      deleteItemFromCart(request, response);
    }
  }

  private void addItemToCart(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    int clothesId = Integer.parseInt(request.getParameter("id"));
    int quantity = Integer.parseInt(request.getParameter("quantity"));
    String size = request.getParameter("size") != null && !request.getParameter("size").isEmpty()
        ? request.getParameter("size")
        : "M";

    ClothesDao clothesDao = new ClothesDao();
    Clothes clothes = clothesDao.getById(clothesId);
    clothes = clothesDao.getOtherClothesBySize(clothes, size);
    // Update the clothesId as the clothes have been updated to new size
    clothesId = clothes.getClothesId();

    // price * (100 - discount) / 100
    BigDecimal subtotal = clothes.getPrice()
        .multiply(BigDecimal.valueOf(100).subtract(BigDecimal.valueOf(clothes.getDiscount())))
        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)
        .multiply(BigDecimal.valueOf(quantity));

    HttpSession session = request.getSession();
    if (session.getAttribute("cart") == null) {
      Cart cart = new Cart();
      List<OrderItem> orderItems = new ArrayList<>();
      orderItems.add(new OrderItem(clothesId, quantity, subtotal));
      cart.setOrderItems(orderItems);
      cart.setTotal(subtotal);
      session.setAttribute("cart", cart);
    } else {
      Cart cart = (Cart) session.getAttribute("cart");
      List<OrderItem> orderItems = cart.getOrderItems();

      // Check if the item already exists in the orderItems list
      boolean itemExists = false;
      for (OrderItem item : orderItems) {
        if (item.getClothesId() == clothesId
            && item.getClothes().getSize().equals(size)) {
          item.setQuantity(item.getQuantity() + quantity);
          item.setSubtotal(item.getSubtotal().add(subtotal));
          cart.setTotal(cart.getTotal().add(subtotal));

          itemExists = true;
          break;
        }
      }

      if (!itemExists) {
        orderItems.add(new OrderItem(clothesId, quantity, subtotal));
        cart.setTotal(cart.getTotal().add(subtotal));
      }

      cart.setOrderItems(cart.getOrderItems());
    }

    saveCartToCookie(request, response);
    session.setAttribute("message", "success-cart");
    redirectToPreviousPage(request, response);
  }

  private void updateItemInCart(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    int clothesId = Integer.parseInt(request.getParameter("id"));
    ClothesDao clothesDao = new ClothesDao();
    Clothes clothes = clothesDao.getById(clothesId);
    int quantity = request.getParameter("quantity") != null
        ? Integer.parseInt(request.getParameter("quantity"))
        : -1;
    int minQuantity = 1;
    int maxQuantity = Math.min(5, clothes.getStockQuantity());
    String size = request.getParameter("size");

    // price * (100 - discount) / 100
    BigDecimal subtotal = clothes.getPrice()
        .multiply(BigDecimal.valueOf(100).subtract(BigDecimal.valueOf(clothes.getDiscount())))
        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)
        .multiply(BigDecimal.valueOf(quantity));

    HttpSession session = request.getSession();
    if (session.getAttribute("cart") == null) {
      Cart cart = new Cart();
      List<OrderItem> orderItems = new ArrayList<>();
      orderItems.add(new OrderItem(clothesId, quantity, subtotal));
      cart.setOrderItems(orderItems);
      cart.setTotal(subtotal);
      session.setAttribute("cart", cart);
    } else {
      Cart cart = (Cart) session.getAttribute("cart");
      List<OrderItem> orderItems = cart.getOrderItems();

      // Check if the item already exists in the orderItems list
      boolean itemExists = false;
      for (OrderItem item : orderItems) {
        if (item.getClothesId() == clothesId) {
          if (quantity >= minQuantity && quantity <= maxQuantity) {
            item.setQuantity(quantity);

            // Disgustingly verbose calculation of new subtotal because of Java's stupid BigDecimal

            // item.price * quantity * (100 - item.discount) / 100
            BigDecimal newSubtotal = item.getClothes().getPrice()
                .multiply(BigDecimal.valueOf(quantity))
                .multiply(BigDecimal.valueOf(100).subtract(BigDecimal.valueOf(item.getClothes().getDiscount())))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

            // cart.total - item.subtotal + newSubtotal
            BigDecimal newTotal = cart.getTotal()
                .subtract(item.getSubtotal())
                .add(newSubtotal);

            item.setSubtotal(newSubtotal);
            cart.setTotal(newTotal);
          }

          // If clothes have a size, update the clothes' size
          if (!Objects.equals(item.getClothes().getSize(), "null") && !item.getClothes().getSize().isEmpty()) {
            Clothes newClothes = clothesDao.getOtherClothesBySize(clothes, size);
            item.setClothes(newClothes); // new clothes have a different size, and therefore different id
          } else {
            item.setClothes(clothes);
          }
          orderItems.set(orderItems.indexOf(item), item);
          itemExists = true;
          break;
        }
      }

      if (!itemExists) {
        orderItems.add(new OrderItem(clothesId, quantity, subtotal));
        cart.setTotal(cart.getTotal().add(subtotal));
      }

      cart.setOrderItems(orderItems);
    }

    saveCartToCookie(request, response);
    session.setAttribute("message", "success-cart");
    redirectToPreviousPage(request, response);
  }

  private void deleteItemFromCart(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    int clothesId = Integer.parseInt(request.getParameter("id"));
    HttpSession session = request.getSession();
    if (session.getAttribute("cart") != null) {
      Cart cart = (Cart) session.getAttribute("cart");
      List<OrderItem> orderItems = cart.getOrderItems();

      // Remove the deleted item from the orderItems list, and update the total
      for (OrderItem orderItem : orderItems) {
        if (orderItem.getClothesId() == clothesId) {
          cart.setTotal(cart.getTotal().subtract(orderItem.getSubtotal()));
          orderItems.remove(orderItem);
          break;
        }
      }

      // Update the orderItems list in the session after removing the deleted item
      cart.setOrderItems(orderItems);
      session.setAttribute("cart", cart);
    }

    saveCartToCookie(request, response);
    redirectToPreviousPage(request, response);
  }

  private void saveCartToCookie(HttpServletRequest request, HttpServletResponse response) {
    // This method is used to save a copy of the current cart
    // as a Cookie so that it can be retrieved later when the user logs back in
    HttpSession session = request.getSession();

    boolean hasCart = session.getAttribute("cart") != null;
    boolean hasUserSession = session.getAttribute("user") != null;

    if (hasCart && hasUserSession) {
      // Get the current cart
      Cart cart = (Cart) session.getAttribute("cart");
      User user = (User) session.getAttribute("user");

      // Serialize the cart so that it can be stored in a cookie
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      try (ObjectOutputStream oos = new ObjectOutputStream(bos)) {
        oos.writeObject(cart);
        oos.flush();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      String serializedCart = Base64.getEncoder().encodeToString(bos.toByteArray());

      // Save a copy of the current cart as a Cookie
      // Each copy can be identified by the user's ID
      Cookie cookie = new Cookie(String.valueOf(user.getUserId()), serializedCart);
      cookie.setPath("/");
      cookie.setMaxAge(60 * 60 * 24 * 30); // 30 days
      response.addCookie(cookie);
    }
  }

  private void redirectToPreviousPage(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    // Get the URL of the page before user triggered this servlet
    HttpSession session = request.getSession();
    String previousUrl = (String) session.getAttribute("previousUrl");
    if (StringUtils.isEmpty(previousUrl)) {
      response.sendRedirect("/");
    } else {
      response.sendRedirect(previousUrl);
      session.removeAttribute("previousUrl");
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    request.getRequestDispatcher("/index.jsp").forward(request, response);
  }
}
