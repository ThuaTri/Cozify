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
            <h5 class="card-title fw-semibold mb-4">Update Order</h5>
            <form method="post" action="update" class="needs-validation" novalidate="">
              <p>Input fields marked with <span class="text-danger">*</span> are required</p>
              <div class="mb-3">
                <input type="hidden" name="id" value="${order.orderId}">
                <label for="status" class="form-label">Status <span class="text-danger">*</span></label>
                <select class="form-select" id="status" name="status" required="">
                  <c:forEach var="s" items="${statuses}">
                    <option
                        value="${s}" ${order.status == s ? "selected" : ""}>${s}</option>
                  </c:forEach>
                </select>
              </div>
              <div class="mb-3">
                <label for="firstName" class="form-label">First name <span class="text-danger">*</span></label>
                <input type="text" maxlength="50" class="form-control" id="firstName" name="firstName" placeholder=""
                       value="${order.firstName}">
                <div class="form-text">Valid first name should not exceed 50 characters.</div>
                <div class="invalid-feedback">
                  Valid first name should not exceed 50 characters.
                </div>
              </div>
              <div class="mb-3">
                <label for="lastName" class="form-label">Last name <span class="text-danger">*</span></label>
                <input type="text" maxlength="50" class="form-control" id="lastName" name="lastName" placeholder=""
                       value="${order.lastName}">
                <div class="form-text">Valid last name should not exceed 50 characters.</div>
                <div class="invalid-feedback">
                  Valid last name should not exceed 50 characters.
                </div>
              </div>
              <div class="mb-3">
                <label for="address" class="form-label">Address <span class="text-danger">*</span></label>
                <input type="text" class="form-control" maxlength="255" id="address" name="address"
                       placeholder="" value="${order.address}">
                <div class="form-text">Valid address should not exceed 255 characters.
                </div>
                <div class="invalid-feedback">
                  Address should not exceed 255 characters.
                </div>
              </div>
              <div class="mb-3">
                <label for="phoneNumber" class="form-label">Phone number <span class="text-danger">*</span></label>
                <input type="number" class="form-control" maxlength="10" pattern="[0]{1}\d{9}" id="phoneNumber" name="phoneNumber"
                       placeholder="" value="${order.phoneNumber}">
                <div class="form-text">Valid phone number starts with a 0 digit, and contains exactly 10 digits.
                </div>
                <div class="invalid-feedback">
                  Phone number is not valid.
                </div>
              </div>
              <div class="mb-3">
                <label for="email" class="form-label">Email <span class="text-danger">*</span></label>
                <input type="email" class="form-control" id="email" name="email" placeholder=""
                       required="" maxlength="255" pattern="[a-z0-9](\.?[a-z0-9]){2,}@g(oogle)?mail\.com"
                       value="${order.email}">
                <div class="form-text">Valid email should not exceed 255 characters.</div>
                <div class="invalid-feedback">
                  Valid email is required.
                </div>
              </div>
              <div class="mb-3">
                <label class="form-label" for="note">Note</label>
                <textarea id="note" name="note" class="form-control">${order.note}</textarea>
                <div class="form-text">Note should not exceed 1000 characters.</div>
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
