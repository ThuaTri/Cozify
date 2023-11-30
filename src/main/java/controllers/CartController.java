package controllers;

import daos.ClothesDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Cart;
import models.Clothes;
import models.OrderItem;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class CartController extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String URI = request.getRequestURI();
    if (URI.endsWith("/cart")) {
      request.getRequestDispatcher("cart.jsp").forward(request, response);
    } else if (URI.startsWith("/cart/add")) {
      addItemToCart(request, response);
    } else if (URI.startsWith("/cart/update")) {
      updateItemInCart(request, response);
    } else if (URI.startsWith("/cart/delete")) {
      deleteItemFromCart(request, response);
    }
  }

  private void addItemToCart(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    int clothesId = Integer.parseInt(request.getParameter("id"));
    int quantity = Integer.parseInt(request.getParameter("quantity"));

    ClothesDao clothesDao = new ClothesDao();
    Clothes clothes = clothesDao.getById(clothesId);

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

    session.setAttribute("message", "success-cart");
    response.sendRedirect("/");
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
    int maxQuantity = Math.max(5, clothes.getStockQuantity());
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

          if (size != null && !size.isEmpty()) {
            Clothes newClothes = clothesDao.getOtherClothesBySize(clothes, size);
            item.setClothes(newClothes);
            orderItems.set(orderItems.indexOf(item), item);
            cart.setOrderItems(orderItems);
          }
        }
        itemExists = true;
        break;
      }

      if (!itemExists) {
        orderItems.add(new OrderItem(clothesId, quantity, subtotal));
        cart.setTotal(cart.getTotal().add(subtotal));
      }

      cart.setOrderItems(cart.getOrderItems());
    }

    session.setAttribute("message", "success-cart");
    response.sendRedirect("/");
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

    response.sendRedirect("/cart");
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    request.getRequestDispatcher("/index.jsp").forward(request, response);
  }
}
