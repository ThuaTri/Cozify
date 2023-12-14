<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="components/imports/base.jspf" %>
<html>
<head>
  <jsp:include page="components/sections/head.jspf">
    <jsp:param name="titleDescription" value="Checkout"/>
  </jsp:include>
</head>
<body>
<%@ include file="components/sections/store_components.jspf" %>
<div class="container">
  <main>
    <div class="py-3 text-center">
      <h2>Checkout</h2>
    </div>

    <div class="row g-5 pb-3">
      <div class="col-md-5 col-lg-4 order-md-last">
        <c:set value="${sessionScope.cart}" var="cart" scope="page"/>
        <h4 class="d-flex justify-content-between align-items-center mb-3">
          <span class="text-primary">Your cart</span>
          <span class="badge bg-primary rounded-pill">${cart.getNumberOfItems()}</span>
        </h4>
        <ul class="list-group mb-3">
          <c:choose>
            <c:when test="${cart.getNumberOfItems() > 0}">
              <c:forEach items="${cart.orderItems}" var="item">
                <li class="list-group-item d-flex justify-content-between lh-sm">
                  <div>
                    <h6 class="my-0">${item.clothes.clothesName} x ${item.quantity}</h6>
                    <small class="text-body-secondary">Size: ${item.clothes.size}</small>
                  </div>
                  <span class="text-body-secondary">$<fmt:formatNumber
                      pattern="#,###.##">${item.subtotal}</fmt:formatNumber></span>
                </li>
              </c:forEach>
            </c:when>
            <c:otherwise>
              <p>Your cart is empty right now. Why not go back and find some clothes to buy?</p>
            </c:otherwise>
          </c:choose>

          <c:set var="voucher" value="${sessionScope.voucher != null ? sessionScope.voucher : null}"/>
          <c:set var="shipping" value="${cart.getNumberOfItems() > 0 ? 5.00 : 0.00}"/>
          <c:set var="tax" value="${cart.total / 10.00}"/>

          <c:if test="${voucher != null}">
            <li class="list-group-item d-flex justify-content-between bg-body-tertiary">
              <div class="text-black">
                <h6 class="my-0">Voucher: ${voucher.voucherName}</h6>
                <small class="text-body-secondary">Vouchers are applied before shipping fee and tax.</small>
              </div>
              <span class="text-body-secondary">-${voucher.voucherPercent}%</span>
              <a href="/checkout?removeVoucher=true" class="text-body-secondary">
                <i class="ms-2 ph-bold ph-x"></i>
              </a>
            </li>
          </c:if>
          <li class="list-group-item d-flex justify-content-between bg-body-tertiary">
            <div class="text-black">
              <h6 class="my-0">Shipping fee</h6>
            </div>
            <span class="text-black">$<fmt:formatNumber pattern="#,###.##">${shipping}</fmt:formatNumber></span>
          </li>
          <li class="list-group-item d-flex justify-content-between bg-body-tertiary">
            <div class="text-black">
              <h6 class="my-0">Tax</h6>
            </div>
            <span class="text-black">$<fmt:formatNumber pattern="#,###.##">${tax}</fmt:formatNumber></span>
          </li>

          <li class="list-group-item d-flex justify-content-between">
            <span>Grand Total</span>
            <strong>
              $<fmt:formatNumber
                pattern="#,###.##">${cart.total * (100 - voucher.voucherPercent) / 100 + shipping + tax}</fmt:formatNumber>
            </strong>
          </li>
        </ul>

        <c:if test="${voucher == null}">
          <form class="card p-2" method="get" action="/checkout" id="voucher-form">
            <div class="input-group">
              <label for="voucherCode" class="form-label">Voucher/Coupon code</label>
              <div id="voucherCode" class="input-group">
                <input type="text" maxlength="16" pattern="[a-zA-Z0-9]{16}" class="form-control" name="voucherCode"
                       placeholder="Voucher or coupon code" required="">
                <button type="submit" class="btn btn-secondary" onclick="redeemVoucher(this)">Redeem</button>
              </div>
              <div class="form-text">Valid voucher code has exactly 16 characters.</div>
              <div class="invalid-feedback">
                Voucher code is invalid.
              </div>
            </div>
          </form>
        </c:if>

      </div>
      <div class="col-md-7 col-lg-8">
        <h4 class="mb-3">Billing address</h4>
        <form class="needs-validation" novalidate="" action="/checkout" method="post">
          <div class="row g-3">
            <p>Input fields marked with <span class="text-danger">*</span> are required</p>
            <div class="col-sm-6">
              <label for="firstName" class="form-label">First name <span class="text-danger">*</span></label>
              <input type="text" class="form-control" id="firstName" name="firstName" placeholder=""
                     value="${sessionScope.user.firstName}" required="">
              <div class="invalid-feedback">
                Valid first name is required.
              </div>
            </div>

            <div class="col-sm-6">
              <label for="lastName" class="form-label">Last name <span class="text-danger">*</span></label>
              <input type="text" class="form-control" id="lastName" name="lastName" placeholder=""
                     value="${sessionScope.user.lastName}" required="">
              <div class="invalid-feedback">
                Valid last name is required.
              </div>
            </div>

            <div class="col-12">
              <label for="phone" class="form-label">Phone number <span class="text-danger">*</span></label>
              <input type="tel" class="form-control" id="phone" name="phone" placeholder="1234567890"
                     value="${sessionScope.user.phoneNumber}" maxlength="10" pattern="[0]{1}\d{9}" required="">
              <div class="invalid-feedback">
                Please enter a valid phone number for shipping updates.
              </div>
            </div>

            <div class="col-12">
              <label for="email" class="form-label">Email <span class="text-danger">*</span></label>
              <input type="email" class="form-control" id="email" name="email" placeholder="you@example.com"
                     value="${sessionScope.user.email}" maxlength="255"
                     pattern="[a-z0-9](\.?[a-z0-9]){2,}@g(oogle)?mail\.com" required="">
              <div class="invalid-feedback">
                Please enter a valid email address for shipping updates.
              </div>
            </div>

            <div class="col-12">
              <label for="address" class="form-label">Address <span class="text-danger">*</span></label>
              <input type="text" class="form-control" id="address" name="address"
                     placeholder="Apartment 1 (optional), 1234 Main St, Example State"
                     value="${sessionScope.user.address}" required="">
              <div class="invalid-feedback">
                Please enter your shipping address.
              </div>
            </div>
          </div>

          <div class="my-3">
            <label class="form-label" for="note">Note</label>
            <textarea id="note" name="note" class="form-control"></textarea>
          </div>

          <hr class="my-4">

          <div class="form-check">
            <input type="checkbox" class="form-check-input" id="save-info" name="saveInfo" value="true">
            <label class="form-check-label" for="save-info">Save this information for future checkouts</label>
          </div>

          <hr class="my-4">

          <h4 class="mb-3">Payment <span class="text-danger">*</span></h4>

          <div class="my-3">
            <div class="form-check">
              <input id="cod" name="paymentMethod" type="radio" class="form-check-input" value="1" checked=""
                     required="">
              <label class="form-check-label" for="cod">Cash on delivery</label>
            </div>
          </div>

          <hr class="my-4">

          <button class="w-100 btn btn-primary btn-lg ${cart.getNumberOfItems() > 0 ? "" : "disabled"}" type="submit">
            Place order
          </button>
        </form>
      </div>
    </div>
  </main>
</div>

<%@ include file="components/sections/footer.jspf" %>
<%@ include file="components/imports/javascript.jspf" %>
<script src="${pageContext.request.contextPath}/components/utils/validation.js"></script>
<script>
  let voucherForm = document.getElementById("voucher-form");

  function redeemVoucher(event) {
    if (!voucherForm.checkValidity()) {
      event.preventDefault();
      event.stopPropagation();
    }

    voucherForm.classList.add('was-validated');
  }

</script>
</body>
</html>