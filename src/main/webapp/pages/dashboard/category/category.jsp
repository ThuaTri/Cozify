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
  <%@ include file="../../../components/elements/toast.jspf" %>

  <!--  Main wrapper -->
  <div class="body-wrapper">
    <%@ include file="../../../components/sections/dashboard_header.jspf" %>
    <div class="container-fluid">
      <div class="card">
        <div class="card-body">
          <h5 class="card-title fw-semibold mb-4">Category Management</h5>
          <table id="category-table">
            <a href="category/add" class="btn btn-sm btn-success py-1 my-2 me-2">
              Add Category
            </a>
            <thead>
            <tr>
              <th>ID</th>
              <th>Category name</th>
              <th>Item count</th>
              <th>Order count</th>
              <th>Total revenue</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${categoryReports}" var="r">
              <tr>
                <td>${r.categoryId}</td>
                <td>${r.categoryName}</td>
                <td>${r.itemCount}</td>
                <td>${r.orderCount}</td>
                <td>${r.isHidden ? "Disabled" : "Available"}</td>
                <td>$<fmt:formatNumber type="number" pattern="#,###.##" value="${r.totalProfit}"/></td>
                <td>
                  <a href="${pageContext.request.contextPath}/dashboard/category/update?id=${r.categoryId}"
                     class="btn btn-sm btn-success py-1 m-1">
                    Update
                  </a>
                  <a href="${pageContext.request.contextPath}/dashboard/category/delete?id=${r.categoryId}"
                     class="btn btn-sm btn-danger py-1 m-1"
                     onclick="confirm('Delete this category? This cannot be undone.')">
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
</body>
</html>
