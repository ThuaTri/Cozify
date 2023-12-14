<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="components/imports/base.jspf" %>
<!DOCTYPE html>
<html lang="en" dir="ltr">
<head>
  <jsp:include page="components/sections/head.jspf">
    <jsp:param name="titleDescription" value="Cool yet Cozy"/>
  </jsp:include>
</head>
<body>
<%@ include file="components/sections/store_components.jspf" %>

<!-- Hero section -->
<header class="hero bg-dark py-5">
  <div class="container px-4 px-lg-5 my-5">
    <div class="text-center text-white">
      <h1 class="display-4 fw-bolder text-white">Shop in style</h1>
      <p class="lead fw-normal text-white mb-0">With Cozify's stylish options</p>
    </div>
  </div>
</header>

<!-- Main Content -->
<main class="main" id="top">
  <!-- Filters -->
  <section class="py-5">
    <div class="container px-4 px-lg-5">
      <div class="d-flex flex-row gap-5 justify-content-center text-center fs-6 fw-bolder">
        <a href="${pageContext.request.contextPath}/">All categories</a>
        <c:forEach items="${categories}" var="c">
          <a href="${pageContext.request.contextPath}/?filter=${c.categoryId}">${c.categoryName}</a>
        </c:forEach>
      </div>
    </div>
  </section>

  <!-- Listings -->
  <section class="pb-5">
    <div class="container px-4 px-lg-5">
      <div class="row gx-4 gx-lg-5 row-cols-2 row-cols-md-3 row-cols-xl-4 justify-content-center">
        <c:forEach items="${clothes}" var="c">
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
                                            href="cart/add?id=${c.clothesId}&quantity=1&size=M">Add to cart</a></div>
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

<style>
    .hero {
        position: relative;
    }

    .hero::before {
        content: "";
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background-image: url('${pageContext.request.contextPath}/resources/media/img/hero.jpg');
        background-size: contain;
        filter: brightness(0.4);
    }

    .hero .container {
        position: relative;
    }
</style>
</html>
