<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../../components/imports/base.jspf" %>
<!DOCTYPE html>
<html lang="en" dir="ltr">
<head>
  <jsp:include page="../../components/sections/head.jspf">
    <jsp:param name="titleDescription" value="Dashboard"/>
  </jsp:include>

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
      <div class="container-fluid">
        <div class="card">
          <div class="card-body">
            <h5 class="card-title fw-semibold mb-4">Account Details</h5>
            <form method="post" action="${pageContext.request.contextPath}/dashboard/account-details" class="needs-validation" novalidate="">
              <p>Input fields marked with <span class="text-danger">*</span> are required</p>
              <div class="mb-3">
                <input type="hidden" name="id" value="${user.userId}">
                <label for="username" class="form-label">User name <span class="text-danger">*</span></label>
                <input type="text" maxlength="50" class="form-control" id="username" name="username" placeholder=""
                       required=""
                       value="${user.username}">
                <div class="form-text">Valid username should not exceed 50 characters.</div>
                <div class="invalid-feedback">
                  Valid username is required.
                </div>
              </div>
              <div class="mb-3">
                <label for="email" class="form-label">Email <span class="text-danger">*</span></label>
                <input type="email" class="form-control" id="email" name="email" placeholder=""
                       required="" maxlength="255" pattern="[a-z0-9](\.?[a-z0-9]){2,}@g(oogle)?mail\.com"
                       value="${user.email}">
                <div class="form-text">Valid email should not exceed 255 characters.</div>
                <div class="invalid-feedback">
                  Valid email is required.
                </div>
              </div>
              <div class="mb-3">
                <label for="password" class="form-label">Password</label>
                <input type="password" class="form-control" id="password" name="password" minlength="8" placeholder="">
                <div class="form-text">Leave blank to not update password.</div>
                <div class="invalid-feedback">
                  Valid password must have at least 8 characters.
                </div>
              </div>
              <div class="mb-3">
                <label for="firstName" class="form-label">First name <span class="text-danger">*</span></label>
                <input type="text" maxlength="50" class="form-control" id="firstName" name="firstName" placeholder=""
                       value="${user.firstName}">
                <div class="form-text">Valid first name should not exceed 50 characters.</div>
                <div class="invalid-feedback">
                  Valid first name should not exceed 50 characters.
                </div>
              </div>
              <div class="mb-3">
                <label for="lastName" class="form-label">Last name <span class="text-danger">*</span></label>
                <input type="text" maxlength="50" class="form-control" id="lastName" name="lastName" placeholder=""
                       value="${user.lastName}">
                <div class="form-text">Valid last name should not exceed 50 characters.</div>
                <div class="invalid-feedback">
                  Valid last name should not exceed 50 characters.
                </div>
              </div>
              <div class="mb-3">
                <label for="phoneNumber" class="form-label">Phone number</label>
                <input type="tel" class="form-control" maxlength="10" pattern="[0]{1}\d{9}" id="phoneNumber"
                       name="phoneNumber"
                       placeholder="" value="${user.phoneNumber}">
                <div class="form-text">Valid phone number should not exceed 10 characters.
                </div>
                <div class="invalid-feedback">
                  Phone number is not valid.
                </div>
              </div>
              <div class="mb-3">
                <label for="address" class="form-label">Address</label>
                <input type="text" class="form-control" maxlength="255" id="address" name="address"
                       placeholder="" value="${user.address}">
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
<%@ include file="../../components/imports/javascript.jspf" %>
<script src="${pageContext.request.contextPath}/components/utils/validation.js"></script>
</body>
</html>
