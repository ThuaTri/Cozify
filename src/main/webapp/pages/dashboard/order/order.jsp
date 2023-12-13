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
          <h5 class="card-title fw-semibold mb-4">Order Management</h5>
          <table id="order-table" class="table table-hover align-middle" style="width:100%">
            <thead>
            <tr>
              <th>ID</th>
              <th>Order time</th>
              <th>Payment method</th>
              <th>First name</th>
              <th>Last name</th>
              <th>Address</th>
              <th>Phone number</th>
              <th>Email</th>
              <th>Total</th>
              <th>Note</th>
              <th>Status</th>
              <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${orders}" var="o">
              <tr>
                <td>${o.orderId}</td>
                <td class="text-wrap">
                  <fmt:formatDate value="${o.orderTime}" pattern="HH:mm MM/dd/yyyy"/>
                </td>
                <td>${o.paymentMethod}</td>
                <td>${o.firstName}</td>
                <td>${o.lastName}</td>
                <td>${o.address}</td>
                <td>${o.phoneNumber}</td>
                <td>${o.email}</td>
                <td>$<fmt:formatNumber type="number" pattern="#,###.##" value="${o.total}"/></td>
                <td>${o.note}</td>
                <c:choose>
                  <c:when test="${o.status == 'delivered'}">
                    <td class="text-success">${o.status}</td>
                  </c:when>
                  <c:when test="${o.status == 'cancelled'}">
                    <td class="text-danger">${o.status}</td>
                  </c:when>
                  <c:otherwise>
                    <td class="text-warning">${o.status}</td>
                  </c:otherwise>
                </c:choose>
                <td>
                  <a href="${pageContext.request.contextPath}/dashboard/order/update?id=${o.orderId}"
                          class="btn btn-sm btn-success py-1 m-1">
                    Update
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
