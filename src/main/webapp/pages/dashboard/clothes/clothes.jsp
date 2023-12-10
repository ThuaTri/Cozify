<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../../../components/imports/base.jspf" %>
<!DOCTYPE html>
<html lang="en" dir="ltr">
<head>
  <jsp:include page="../../../components/sections/head.jspf">
    <jsp:param name="titleDescription" value="Dashboard"/>
  </jsp:include>
  <%@ include file="../../../components/imports/dataTablesCss.jspf" %>
</head>
<body>
<!--  Body Wrapper -->
<div class="page-wrapper" id="main-wrapper" data-layout="vertical" data-navbarbg="skin6" data-sidebartype="full"
     data-sidebar-position="fixed" data-header-position="fixed">
  <%@ include file="../../../components/sections/dashboard_sidebar.jspf" %>
  <%@ include file="../../../components/elements/toast.jspf" %>

  <!--  Main wrapper -->
  <div class="body-wrapper">
    <%@ include file="../../../components/sections/dashboard_header.jspf" %>
    <div class="container-fluid">
      <div class="card">
        <div class="card-body">
          <h5 class="card-title fw-semibold mb-4">Clothes Management</h5>
          <table id="clothes-table" class="table table-hover align-middle" style="width:100%">
            <a href="clothes/add" class="btn btn-sm btn-success py-1 my-2 me-2">
              Add Clothes
            </a>
            <thead>
            <tr>
              <th>ID</th>
              <th>Image</th>
              <th>Clothes name</th>
              <th>Price</th>
              <th>Discount %</th>
              <th>Rating</th>
              <th>Quantity</th>
              <th>Size</th>
              <th>State</th>
              <th>Category</th>
              <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${clothes}" var="c">
              <tr>
                <td>${c.clothesId}</td>
                <td class="table-image-cell">
                  <img src="${pageContext.request.contextPath}/resources/media/img/clothes/${c.imgUrl}"
                       alt="${c.clothesName}" style="height:80px;"/>
                </td>
                <td>${c.clothesName}</td>
                <td>$<fmt:formatNumber type="number" pattern="#,###.##" value="${c.price}"/></td>
                <td>${c.discount}</td>
                <td>${c.rating}</td>
                <td>${c.stockQuantity}</td>
                <td>${c.size != "null" ? c.size : "N/A"}</td>
                <td>${c.isHidden ? "Disabled" : "Available"}</td>
                <td>${c.categoryName}</td>
                <td>
                  <a href="${pageContext.request.contextPath}/dashboard/clothes/update?id=${c.clothesId}"
                     class="btn btn-sm btn-success py-1 m-1">
                    Update
                  </a>
                  <a href="${pageContext.request.contextPath}/dashboard/clothes/delete?id=${c.clothesId}"
                     class="btn btn-sm btn-danger py-1 m-1"
                     onclick="return confirm('Disable this clothes item? This cannot be undone.')">
                    Disable
                  </a>
                </td>
              </tr>
            </c:forEach>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</div>
<%@ include file="../../../components/imports/javascript.jspf" %>
<%@ include file="../../../components/imports/dataTablesJs.jspf" %>
</body>
</html>
