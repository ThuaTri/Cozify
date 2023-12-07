<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../../components/imports/base.jspf" %>
<!DOCTYPE html>
<html lang="en" dir="ltr">
<head>
  <jsp:include page="../../components/sections/head.jspf">
    <jsp:param name="titleDescription" value="Dashboard"/>
  </jsp:include>
  <%@ include file="../../components/imports/dataTablesCss.jspf" %>
</head>
<body>
<!--  Body Wrapper -->
<div class="page-wrapper" id="main-wrapper" data-layout="vertical" data-navbarbg="skin6" data-sidebartype="full"
     data-sidebar-position="fixed" data-header-position="fixed">
  <%@ include file="../../components/sections/dashboard_sidebar.jspf" %>
  <%@ include file="../../components/elements/toast.jspf" %>

  <!--  Main wrapper -->
  <div class="body-wrapper">
    <%@ include file="../../components/sections/dashboard_header.jspf" %>
    <div class="container-fluid">
      <div class="card">
        <div class="card-body">
          <h5 class="card-title fw-semibold mb-4">Order History</h5>
          <table id="order-table" class="table table-hover align-middle" style="width:100%">
            <thead>
            <tr>
              <th>Order time</th>
              <th>Status</th>
              <th>Payment method</th>
              <th>Address</th>
              <th>Phone number</th>
              <th>Total</th>
              <th>Note</th>
              <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${orders}" var="o">
              <tr>
                <td class="text-wrap">
                  <fmt:formatDate value="${o.orderTime}" pattern="HH:mm MM/dd/yyyy"/>
                </td>
                <td>${o.status}</td>
                <td>${o.paymentMethod}</td>
                <td>${o.address}</td>
                <td>${o.phoneNumber}</td>
                <td>$<fmt:formatNumber type="number" pattern="#,###.##" value="${o.total}"/></td>
                <td>${o.note}</td>
                <td>
                  <c:if test="${(o.status != 'cancelled' && o.status != 'delivering' && o.status != 'delivered')}">
                    <a href="${pageContext.request.contextPath}/dashboard/order/cancel?id=${o.orderId}"
                       class="btn btn-sm btn-danger py-1 m-1"
                       onclick="return confirm('Do you want to cancel this order? This cannot be undone.')">
                      Cancel
                    </a>
                  </c:if>
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
<%@ include file="../../components/imports/javascript.jspf" %>
<%@ include file="../../components/imports/dataTablesJs.jspf" %>
</body>
</html>
