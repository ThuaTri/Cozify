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
          <h5 class="card-title fw-semibold mb-4">User Management</h5>
          <table id="user-table" class="table table-hover nowrap align-middle" style="width:100%">
            <thead>
            <tr>
              <th>ID</th>
              <th>Username</th>
              <th>Email</th>
              <th>First name</th>
              <th>Last name</th>
              <th>Phone number</th>
              <th>Address</th>
              <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${users}" var="u">
              <tr>
                <td>${u.userId}</td>
                <td>${u.username}</td>
                <td>${u.email}</td>
                <td>${u.firstName}</td>
                <td>${u.lastName}</td>
                <td>${u.phoneNumber}</td>
                <td>${u.address}</td>
                <td>
                  <a href="${pageContext.request.contextPath}/dashboard/user/update?id=${u.userId}"
                     class="btn btn-sm btn-success py-1 m-1">
                    Update
                  </a>
                  <a href="${pageContext.request.contextPath}/dashboard/user/delete?id=${u.userId}"
                     class="btn btn-sm btn-danger py-1 m-1" onclick="return confirm('Delete this user account? This cannot be undone.')">
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
