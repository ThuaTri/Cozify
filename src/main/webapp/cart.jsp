<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="components/imports/base.jspf" %>
<html>
<head>
  <jsp:include page="components/sections/head.jspf">
    <jsp:param name="titleDescription" value="Your Shopping Cart"/>
  </jsp:include>
</head>
<body class="d-flex flex-column">
<%@ include file="components/sections/store_components.jspf" %>

<div class="container-fluid">
  <div class="row d-flex justify-content-center align-items-center h-100">
    <div class="card-registration card-registration-2">
      <div class="row g-0">
        <!-- Shopping cart -->
        <div class="col-lg-8 p-5">
          <div class="d-flex justify-content-between align-items-center mb-5">
            <h1 class="fw-bold mb-0 text-black">Shopping Cart</h1>
            <h6 class="mb-0 text-muted">${sessionScope.cart.getNumberOfItems()} items</h6>
          </div>
          <hr class="my-4">
          <c:set value="${sessionScope.cart}" var="cart" scope="page"/>
          <c:choose>
            <c:when test="${cart.getNumberOfItems() > 0}">
              <c:forEach items="${cart.orderItems}" var="item">
                <div class="row mb-4 d-flex justify-content-between align-items-center">
                  <div class="col-md-2 col-lg-2 col-xl-2">
                    <img
                        src="${pageContext.request.contextPath}/resources/media/img/clothes/${item.clothes.imgUrl}"
                        class="img-fluid rounded-3" alt="${item.clothes.clothesName}">
                  </div>

                  <!-- Name and size -->
                  <div class="col-md-3 col-lg-3 col-xl-3">
                    <h6 id="clothes-${item.clothes.clothesId}" class="text-black mb-0">${item.clothes.clothesName}</h6>
                    <c:if test="${!(item.clothes.size == 'null')}">
                      <label for="size">Size: </label>
                      <select id="size" class="form-select" aria-label="Default select example"
                              name="size-${item.clothesId}" onchange="changeSize(this)">
                        <c:forEach items="${item.availableSizes}" var="size">
                          <option value="${size}" ${item.clothes.size == size ? "selected" : ""}>${size}</option>
                        </c:forEach>
                      </select>
                    </c:if>
                  </div>

                  <!-- Quantity input -->
                  <div class="col-md-3 col-lg-3 col-xl-2 d-flex">
                    <button class="btn px-2"
                            onclick="decrement(this)">
                      <i class="ph-bold ph-minus"></i>
                    </button>

                    <input min="1" max="${item.clothes.stockQuantity < 5 ? item.clothes.stockQuantity : 5}"
                           name="quantity-${item.clothesId}" value="${item.quantity}" type="number"
                           class="form-control form-control-sm"/>

                    <button class="btn px-2"
                            onclick="increment(this)">
                      <i class="ph-bold ph-plus"></i>
                    </button>
                  </div>

                  <!-- Price -->
                  <div class="col-md-3 col-lg-2 col-xl-2 offset-lg-1">
                    <c:if test="${item.clothes.discount > 0}">
                      <span class="text-muted text-decoration-line-through">$${item.clothes.price}</span>
                    </c:if>
                    <span class="fw-bolder fs-5">$<fmt:formatNumber
                        pattern="#,###.##">${item.clothes.price * (100 - item.clothes.discount) / 100}</fmt:formatNumber></span>
                  </div>

                  <!-- Delete button -->
                  <div class="col-md-1 col-lg-1 col-xl-1 text-end">
                    <a href="cart/delete?id=${item.clothes.clothesId}" class="text-muted text-decoration-none"><i
                        class="ph-bold ph-x"></i></a>
                  </div>
                </div>

                <hr class="my-4">
              </c:forEach>
            </c:when>
            <c:otherwise>
              <p>Your cart is empty right now. Why not go back and find some clothes to buy?</p>
            </c:otherwise>
          </c:choose>
          <div class="pt-5">
            <h6 class="mb-0"><a href="/" class="text-body text-decoration-none"><i
                class="ph-bold ph-arrow-left me-2"></i>Back to shop</a></h6>
          </div>
        </div>

        <!-- Summary -->
        <div class="col-lg-4 bg-grey p-5">
          <h3 class="fw-bold mb-5 mt-2 pt-1">Summary</h3>
          <hr class="my-4">

          <c:forEach items="${cart.orderItems}" var="item">
            <div class="d-flex justify-content-between mb-4">
              <h5 class="">
                ${item.quantity} x
                ${item.clothes.clothesName}
                <c:if test="${!(item.clothes.size == 'null')}">- ${item.clothes.size}</c:if>
              </h5>
              <h5>$<fmt:formatNumber pattern="#,###.##">${item.subtotal}</fmt:formatNumber></h5>
            </div>
          </c:forEach>

          <c:set var="shipping" value="${cart.getNumberOfItems() > 0 ? 5.00 : 0.00}"/>
          <div class="d-flex justify-content-between mb-4">
            <h5 class="mb-3">Shipping</h5>
            <p>$<fmt:formatNumber pattern="#,###.##">${shipping}</fmt:formatNumber></p>
          </div>

          <c:set var="tax" value="${cart.total / 10.00}"/>
          <div class="d-flex justify-content-between mb-4">
            <h5 class="mb-3">Tax</h5>
            <p>$<fmt:formatNumber pattern="#,###.##">${tax}</fmt:formatNumber></p>
          </div>

          <hr class="my-4">

          <div class="d-flex justify-content-between mb-5">
            <h5 class="">Total price</h5>
            <h5>$<fmt:formatNumber pattern="#,###.##">${cart.total + shipping + tax}</fmt:formatNumber></h5>
          </div>

          <a href="/checkout" class="btn btn-dark btn-block btn-lg ${(empty cart || cart.getNumberOfItems() <= 0) ? "disabled" : ""}">
            Proceed to Checkout
          </a>

        </div>
      </div>
    </div>
  </div>
</div>

<!-- Footer -->
<%@ include file="components/sections/footer.jspf" %>
<%@ include file="components/imports/javascript.jspf" %>
</body>
<style>

    .card-registration {
        font-size: 1rem;
        line-height: 2.15;
    }

    .card-registration .select-arrow {
        top: 13px;
    }

    .bg-grey {
        background-color: #eae8e8;
    }
</style>
</html>

<script>
  function increment(event) {
    let element = event.parentNode.querySelector('input[type=number]');
    let max = Number.parseInt(element.getAttribute("max"));
    if (element.value < max) {
      let xhr = new XMLHttpRequest();

      let quantity = Number.parseInt(element.value);
      let id = element.getAttribute("name").split("-")[1];
      let size = event.parentNode.parentNode.querySelector('select')
          ? event.parentNode.parentNode.querySelector('select').value
          : "";

      let url = "/cart/update?id=" + id + "&quantity=" + (quantity + 1) + "&size=" + size;
      xhr.open('GET', url, true);
      xhr.send();
      xhr.onreadystatechange = () => {
        if (xhr.readyState === 4 && xhr.status === 200) {
          location.reload();
        }
      }
    }
  }

  function decrement(event) {
    let element = event.parentNode.querySelector('input[type=number]');
    let min = Number.parseInt(element.getAttribute("min"));
    if (element.value > min) {
      let xhr = new XMLHttpRequest();
      let quantity = Number.parseInt(element.value);
      let id = element.getAttribute("name").split("-")[1];
      let size = event.parentNode.parentNode.querySelector('select').value;
      let url = "/cart/update?id=" + id + "&quantity=" + (quantity - 1) + "&size=" + size;
      xhr.open('GET', url, true);
      xhr.send();
      xhr.onreadystatechange = () => {
        if (xhr.readyState === 4 && xhr.status === 200) {
          location.reload();
        }
      }
    }
  }

  function changeSize(event) {
    let element = event.parentNode.querySelector('select');
    let xhr = new XMLHttpRequest();

    let id = element.getAttribute("name").split("-")[1];
    let size = element.value;
    let quantity = Number.parseInt(event.parentNode.parentNode.querySelector('input[type=number]').value);

    let url = "/cart/update?id=" + id + "&size=" + size + "&quantity=" + quantity;
    xhr.open('GET', url, true);
    xhr.send();
    xhr.onreadystatechange = () => {
      if (xhr.readyState === 4 && xhr.status === 200) {
        location.reload();
      }
    }
  }
</script>