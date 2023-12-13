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
          <h5 class="card-title fw-semibold mb-4">Staff Management</h5>
          <table id="staff-table" class="table table-hover nowrap align-middle" style="width:100%">
            <a href="staff/add" class="btn btn-sm btn-success py-1 my-2 me-2">
              Add Staff
            </a>
            <thead>
            <tr>
              <th>ID</th>
              <th>Role</th>
              <th>Username</th>
              <th>First name</th>
              <th>Last name</th>
              <th>Phone number</th>
              <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${staffs}" var="s">
              <tr>
                <td>${s.userId}</td>
                <td>${s.role}</td>
                <td>${s.username}</td>
                <td>${s.firstName}</td>
                <td>${s.lastName}</td>
                <td>${s.phoneNumber}</td>
                <td>
                  <a href="${pageContext.request.contextPath}/dashboard/staff/update?id=${s.userId}"
                     class="btn btn-sm btn-success py-1 m-1">
                    Update
                  </a>
                  <a href="${pageContext.request.contextPath}/dashboard/staff/delete?id=${s.userId}"
                     class="btn btn-sm btn-danger py-1 m-1" onclick="return confirm('Delete this staff account? This cannot be undone.')">
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
