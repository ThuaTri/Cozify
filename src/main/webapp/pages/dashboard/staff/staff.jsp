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
          <h5 class="card-title fw-semibold mb-4">Staff Management</h5>
          <table id="category-table">
            <a href="category/add" class="btn btn-sm btn-success py-1 my-2 me-2">
              Add Staff
            </a>
            <thead>
            <tr>
              <th>ID</th>
              <th>Username</th>
              <th>Email</th>
              <th>First name</th>
              <th>Last name</th>
              <th>Phone number</th>
              <th>Address</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${staffs}" var="s">
              <tr>
                <td>${s.userId}</td>
                <td>${s.username}</td>
                <td>${s.email}</td>
                <td>${s.firstName}</td>
                <td>${s.lastName}</td>
                <td>${s.phoneNumber}</td>
                <td>${s.address}</td>
                <td>
                  <a href="${pageContext.request.contextPath}/user/update?id=${r.categoryId}"
                     class="btn btn-sm btn-success py-1 m-1">
                    Update
                  </a>
                  <a href="${pageContext.request.contextPath}/user/delete?id=${r.categoryId}"
                     class="btn btn-sm btn-danger py-1 m-1">
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
