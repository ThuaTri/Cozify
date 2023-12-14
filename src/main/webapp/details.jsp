<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="components/imports/base.jspf" %>
<!DOCTYPE html>
<html lang="en" dir="ltr">
<head>
  <jsp:include page="components/sections/head.jspf">
    <jsp:param name="titleDescription" value="${clothes.clothesName}"/>
  </jsp:include>
</head>
<body>
<%@ include file="components/sections/store_components.jspf" %>

<!-- Main Content -->
<main class="main" id="top">
  <!-- Product section-->
  <section class="py-5">
    <div class="container px-4 px-lg-5 my-5">
      <div class="row gx-4 gx-lg-5 align-items-center">
        <div class="col-md-6">
          <img class="card-img-top mb-5 mb-md-0"
               src="${pageContext.request.contextPath}/resources/media/img/clothes/${clothes.imgUrl}"
               alt="..."/>
        </div>
        <div class="col-md-6">
          <h1 class="display-5 fw-bolder">${clothes.clothesName}</h1>
          <div class="d-flex text-warning mb-2">
            <c:forEach begin="1" end="5" varStatus="loop">
              <c:choose>
                <c:when test="${loop.index <= clothes.rating}">
                  <div class="ph-fill ph-star"></div>
                </c:when>
                <c:otherwise>
                  <div class="ph-bold ph-star"></div>
                </c:otherwise>
              </c:choose>
            </c:forEach>
          </div>
          <div class="fs-5 mb-5">
            <!-- Product price-->
            <c:if test="${clothes.discount > 0}">
              <span class="text-muted text-decoration-line-through">$${clothes.price}</span>
            </c:if>
            <span class="fw-bolder fs-5">$<fmt:formatNumber
                pattern="#,###.##">${clothes.price * (100 - clothes.discount) / 100}</fmt:formatNumber></span>
          </div>
          <div class="d-flex flex-column">
            <!-- Quantity input -->
            <div class="col-md-3 col-lg-3 col-xl-2 d-flex align-items-center">
              <label for="quantity" class="me-3">Quantity: </label>
              <button class="btn px-2"
                      onclick="decrement(this)">
                <i class="ph-bold ph-minus"></i>
              </button>

              <input id="quantity" min="1" max="${clothes.stockQuantity < 5 ? clothes.stockQuantity : 5}"
                     name="quantity-${clothes.clothesId}" value="1" type="number"
                     class="form-control form-control-sm" style="min-width: 3rem;"/>

              <button class="btn px-2"
                      onclick="increment(this)">
                <i class="ph-bold ph-plus"></i>
              </button>
            </div>
            <c:if test="${!(clothes.size == 'null')}">
              <label for="size">Size: </label>
              <select id="size" class="form-select" aria-label="Default select example"
                      name="size-${clothes.clothesId}" onchange="changeSize(this)">
                <c:forEach items="${availableSizes}" var="size">
                  <option value="${size}" ${clothes.size == size ? "selected" : ""}>${size}</option>
                </c:forEach>
              </select>
            </c:if>
            <a id="addToCart" class="btn btn-outline-dark flex-shrink-0 mt-4"
               href="${pageContext.request.contextPath}/cart/add?id=${clothes.clothesId}&quantity=1&size=M">
              Add to cart
            </a>
          </div>
        </div>
      </div>
    </div>
  </section>
  <!-- Related items section-->
  <section class="py-5 bg-light">
    <div class="container px-4 px-lg-5 mt-5">
      <h2 class="fw-bolder mb-4">Related products</h2>
      <div class="row gx-4 gx-lg-5 row-cols-2 row-cols-md-3 row-cols-xl-4 justify-content-center">
        <c:forEach items="${clothesList}" begin="0" end="3" var="c">
          <div class="col mb-5">
            <div class="card h-100">
              <!-- Sale badge-->
              <c:if test="${c.discount > 0}">
                <div class="badge bg-dark text-white position-absolute" style="top: 0.5rem; right: 0.5rem">${c.discount}%
                  off
                </div>
              </c:if>
              <!-- Product image-->
              <img class="card-img-top object-fit-cover" style="height: 240px;"
                   src="${pageContext.request.contextPath}/resources/media/img/clothes/${c.imgUrl}" alt="..."/>
              <!-- Product details-->
              <div class="card-body p-4">
                <div class="text-center">
                  <!-- Product name-->
                  <h5 class="fw-bolder">${c.clothesName}</h5>
                  <!-- Product reviews-->
                  <div class="d-flex justify-content-center small text-warning mb-2">
                    <c:forEach begin="1" end="5" varStatus="loop">
                      <c:choose>
                        <c:when test="${loop.index <= c.rating}">
                          <div class="ph-fill ph-star"></div>
                        </c:when>
                        <c:otherwise>
                          <div class="ph-bold ph-star"></div>
                        </c:otherwise>
                      </c:choose>
                    </c:forEach>
                  </div>
                  <!-- Product price-->
                  <c:if test="${c.discount > 0}">
                    <span class="text-muted text-decoration-line-through">$${c.price}</span>
                  </c:if>
                  <span class="fw-bolder fs-5">$<fmt:formatNumber
                      pattern="#,###.##">${c.price * (100 - c.discount) / 100}</fmt:formatNumber></span>
                </div>
              </div>
              <!-- Product actions-->
              <a href="/clothes/${c.clothesId}" class="stretched-link"></a>
              <div class="card-footer p-4 pt-0 border-top-0 bg-transparent">
                <div class="text-center"><a class="btn btn-outline-dark mt-auto position-relative z-2"
                                            href="${pageContext.request.contextPath}/cart/add?id=${c.clothesId}&quantity=1&size=M">Add to cart</a></div>
              </div>
            </div>
          </div>
        </c:forEach>
      </div>
    </div>
  </section>
</main>

<%@ include file="components/sections/footer.jspf" %>
<%@ include file="components/imports/javascript.jspf" %>
</body>

<script>
  function increment(event) {
    let element = event.parentNode.querySelector('input[type=number]');
    let max = Number.parseInt(element.getAttribute("max"));
    if (element.value < max) {
      let quantity = Number.parseInt(element.value) + 1;
      let id = element.getAttribute("name").split("-")[1];
      let size = event.parentNode.parentNode.querySelector('select').value;
      let url = "/cart/add?id=" + id + "&quantity=" + (quantity) + "&size=" + size;
      element.setAttribute("value", quantity);
      let button = document.getElementById("addToCart");
      button.setAttribute("href", url);
    }
  }

  function decrement(event) {
    let element = event.parentNode.querySelector('input[type=number]');
    let min = Number.parseInt(element.getAttribute("min"));
    if (element.value > min) {
      let quantity = Number.parseInt(element.value) - 1;
      let id = element.getAttribute("name").split("-")[1];
      let size = event.parentNode.parentNode.querySelector('select').value;
      let url = "/cart/add?id=" + id + "&quantity=" + (quantity) + "&size=" + size;
      element.setAttribute("value", quantity);
      let button = document.getElementById("addToCart");
      button.setAttribute("href", url);
    }
  }

  function changeSize(event) {
    let element = event.parentNode.querySelector('select');
    let id = element.getAttribute("name").split("-")[1];
    let size = element.value;
    let quantity = Number.parseInt(event.parentNode.parentNode.querySelector('input[type=number]').value);

    let url = "/cart/add?id=" + id + "&size=" + size + "&quantity=" + quantity;
    let button = document.getElementById("addToCart");
    button.setAttribute("href", url);
  }
</script>
</html>
