<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="components/imports/base.jspf" %>
<!DOCTYPE html>
<html lang="en" dir="ltr">
<head>
  <jsp:include page="components/sections/head.jspf">
    <jsp:param name="titleDescription" value="Sign up"/>
  </jsp:include>
</head>
<body>
<%@ include file="components/sections/store_components.jspf" %>

<div class="row justify-content-center">
  <div class="col-md-12 col-lg-10">
    <div class="wrap d-md-flex justify-content-center">
      <div class="login-wrap p-4 p-md-5 w-50">
        <div class="d-flex">
          <div class="w-100">
            <h3 class="mb-4">Reset password</h3>
          </div>
        </div>
        <form action="${pageContext.request.contextPath}/reset-password" method="post"
              class="login-form d-flex flex-column needs-validation" novalidate="">
          <div class="form-group mb-3">
            <label class="label" for="password">New password</label>
            <input id="password" name="password" type="password" class="form-control" placeholder="" maxlength="255"
                   minlength="8" required="">
            <div class="form-text">Your new password must have at least 8 characters.</div>
            <div class="invalid-feedback">
              Valid new password is required.
            </div>
          </div>

          <div class="form-group mb-3">
            <label class="label" for="rePassword">Re-enter password</label>
            <input id="rePassword" name="rePassword" type="password" class="form-control" placeholder="" maxlength="255"
                   minlength="8" required="">
            <div class="form-text">Ensure the re-entered password matches the new password.</div>
            <div class="invalid-feedback">
              Re-entered password is either invalid or does not match the new password above.
            </div>
          </div>

          <div class="form-group mb-3">
            <div class="form-text">After successful verification, we have logged you in. You can change your password
              here, or continue shopping by clicking "Return to home page".<br/>
              You can always change your password later if needed by clicking on your username -> Account Details.
            </div>
          </div>

          <button type="submit" class="btn btn-primary rounded submit mt-3 px-3">Change password</button>
        </form>
        <p class="text-center mt-3"><a href="${pageContext.request.contextPath}/">Return to home page</a></p>
      </div>
    </div>
  </div>
</div>

<%@ include file="components/sections/footer.jspf" %>
<%@ include file="components/imports/javascript.jspf" %>
<script src="${pageContext.request.contextPath}/components/utils/validation.js"></script>
</body>
</html>
