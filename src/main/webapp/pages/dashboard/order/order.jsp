<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../../../components/imports/base.jspf" %>
<!DOCTYPE html>
<html lang="en" dir="ltr">
<head>
  <jsp:include page="../../../components/sections/head.jspf">
    <jsp:param name="titleDescription" value="Dashboard"/>
  </jsp:include>

</head>
<body>
<!--  Body Wrapper -->
<div class="page-wrapper" id="main-wrapper" data-layout="vertical" data-navbarbg="skin6" data-sidebartype="full"
     data-sidebar-position="fixed" data-header-position="fixed">
  <%@ include file="../../../components/sections/dashboard_sidebar.jspf" %>

  <!--  Main wrapper -->
  <div class="body-wrapper">
    <%@ include file="../../../components/sections/dashboard_header.jspf" %>
    <div class="container-fluid">
      <div class="card">
        <div class="card-body">
          <h5 class="card-title fw-semibold mb-4">Order Management</h5>
          <table id="category-table">
            <a href="category/add" class="btn btn-sm btn-success py-1 my-2 me-2">
              Add Order
            </a>
            <thead>
            <tr>
              <th>ID</th>
              <th>Order time</th>
              <th>Status</th>
              <th>Payment method</th>
              <th>First name</th>
              <th>Last name</th>
              <th>Address</th>
              <th>Phone number</th>
              <th>Email</th>
              <th>Total</th>
              <th>Note</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${orders}" var="c">
              <tr>
                <td>${c.orderId}</td>
                <td class="text-wrap">
                  <fmt:formatDate value="${c.orderTime}" pattern="HH:mm MM/dd/yyyy"/>
                </td>
                <td>${c.status}</td>
                <td>${c.paymentMethod}</td>
                <td>${c.firstName}</td>
                <td>${c.lastName}</td>
                <td>${c.address}</td>
                <td>${c.phoneNumber}</td>
                <td>${c.email}</td>
                <td>$<fmt:formatNumber type="number" pattern="#,###.##" value="${c.total}"/></td>
                <td>${c.note}</td>
                <td>
                  <a href="${pageContext.request.contextPath}/category/update?id=${r.categoryId}"
                          class="btn btn-sm btn-success py-1 m-1">
                    Update
                  </a>
                  <a href="${pageContext.request.contextPath}/category/delete?id=${r.categoryId}"
                          class="btn btn-sm btn-danger py-1 m-1">
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
</body>
</html>
