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
          <h5 class="card-title fw-semibold mb-4">Voucher Management</h5>
          <table id="voucher-table" class="table table-hover align-middle" style="width:100%">
            <a href="voucher/add" class="btn btn-sm btn-success py-1 my-2 me-2">
              Add Voucher
            </a>
            <thead>
            <tr>
              <th>ID</th>
              <th>Name</th>
              <th>Code</th>
              <th>Value (%)</th>
              <th>Quantity</th>
              <th>Start date</th>
              <th>End date</th>
              <th>Status</th>
              <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${vouchers}" var="v">
              <tr>
                <td>${v.voucherId}</td>
                <td>${v.voucherName}</td>
                <td>${v.voucherCode}</td>
                <td>${v.voucherPercent}</td>
                <td>${v.voucherQuantity}</td>
                <td class="text-wrap">
                  <fmt:formatDate value="${v.startDate}" pattern="HH:mm MM/dd/yyyy"/>
                </td>
                <td class="text-wrap">
                  <fmt:formatDate value="${v.endDate}" pattern="HH:mm MM/dd/yyyy"/>
                </td>
                <td>${v.isHidden ? "Disabled" : "Available"}</td>
                <td>
                  <a href="${pageContext.request.contextPath}/dashboard/voucher/update?id=${v.voucherId}"
                          class="btn btn-sm btn-success py-1 m-1">
                    Update
                  </a>
                  <a href="${pageContext.request.contextPath}/dashboard/voucher/delete?id=${v.voucherId}"
                     class="btn btn-sm btn-danger py-1 m-1"
                     onclick="return confirm('Delete this voucher? This cannot be undone.')">
                    Delete
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
