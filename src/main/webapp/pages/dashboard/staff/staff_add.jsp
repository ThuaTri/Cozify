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
      <div class="container-fluid">
        <div class="card">
          <div class="card-body">
            <h5 class="card-title fw-semibold mb-4">Add Staff</h5>
            <form method="post" action="add" class="needs-validation" novalidate="">
              <p>Input fields marked with <span class="text-danger">*</span> are required</p>
              <div class="mb-3">
                <label for="role" class="form-label">Role <span class="text-danger">*</span></label>
                <select class="form-select" id="role" name="role" required="">
                  <option value="1">Admin</option>
                  <option value="2">Staff</option>
                </select>
              </div>
              <div class="mb-3">
                <label for="username" class="form-label">Username <span class="text-danger">*</span></label>
                <input type="text" maxlength="50" class="form-control" id="username" name="username" placeholder=""
                       required="">
                <div class="form-text">Valid username should not exceed 50 characters.</div>
                <div class="invalid-feedback">
                  Valid username is required.
                </div>
              </div>
              <div class="mb-3">
                <label for="email" class="form-label">Email <span class="text-danger">*</span></label>
                <input type="email" maxlength="255" class="form-control" id="email" name="email" placeholder=""
                       required="">
                <div class="form-text">Valid email should not exceed 255 characters.</div>
                <div class="invalid-feedback">
                  Valid email is required.
                </div>
              </div>
              <div class="mb-3">
                <label for="password" class="form-label">Password</label>
                <input id="password" name="password" type="password" class="form-control" placeholder="" minlength="8" required="">
                <div class="invalid-feedback">
                  Valid password is required and must have at least 8 characters.
                </div>
              </div>
              <div class="mb-3">
                <label for="firstName" class="form-label">First name <span class="text-danger">*</span></label>
                <input type="text" maxlength="50" class="form-control" id="firstName" name="firstName" placeholder="">
                <div class="form-text">Valid first name should not exceed 50 characters.</div>
                <div class="invalid-feedback">
                  Valid first name should not exceed 50 characters.
                </div>
              </div>
              <div class="mb-3">
                <label for="lastName" class="form-label">Last name <span class="text-danger">*</span></label>
                <input type="text" maxlength="50" class="form-control" id="lastName" name="lastName" placeholder="">
                <div class="form-text">Valid last name should not exceed 50 characters.</div>
                <div class="invalid-feedback">
                  Valid last name should not exceed 50 characters.
                </div>
              </div>
              <div class="mb-3">
                <label for="phoneNumber" class="form-label">Phone number</label>
                <input type="number" class="form-control" maxlength="15" id="phoneNumber" name="phoneNumber"
                       placeholder="">
                <div class="form-text">Valid phone number should not exceed 13 characters.
                </div>
                <div class="invalid-feedback">
                  Phone number should not exceed 10 characters.
                </div>
              </div>
              <div class="mb-3">
                <label for="address" class="form-label">Address</label>
                <input type="text" class="form-control" maxlength="255" id="address" name="address"
                       placeholder="">
                <div class="form-text">Valid address should not exceed 255 characters.
                </div>
                <div class="invalid-feedback">
                  Address should not exceed 255 characters.
                </div>
              </div>
              <button type="submit" class="btn btn-primary">Submit</button>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
<%@ include file="../../../components/imports/javascript.jspf" %>
<script src="${pageContext.request.contextPath}/components/utils/validation.js"></script>
</body>
</html>
