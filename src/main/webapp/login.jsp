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
         <script src="https://accounts.google.com/gsi/client" async ></script>
         <script async defer src="https://apis.google.com/js/api.js" ></script>
<%@ include file="components/sections/store_components.jspf" %>

<div class="row justify-content-center">
  <div class="col-md-12 col-lg-10">
    <div class="wrap d-md-flex justify-content-center">
      <div class="login-wrap p-4 p-md-5 w-50">
        <div class="d-flex">
          <div class="w-100">
            <h3 class="mb-4">Log in</h3>
          </div>
        </div>
        <form action="${pageContext.request.contextPath}/login" method="post" class="login-form d-flex flex-column needs-validation" novalidate="">
          <div class="form-group mb-3">
            <label class="label" for="email">Email</label>
            <input id="email" name="email" type="email" class="form-control" placeholder="" maxlength="255"
                   pattern="[a-z0-9](\.?[a-z0-9]){2,}@g(oogle)?mail\.com" required="">
            <div class="invalid-feedback">
              Valid email is required.
            </div>
          </div>
          <div class="form-group mb-3">
            <div class="d-flex flex-row justify-content-between">
              <label class="label" for="password">Password</label>
              <a href="/forget-password">Forgot password?</a>
            </div>
            <input id="password" name="password" type="password" class="form-control" placeholder="" minlength="8"
                   required="">
            <div class="invalid-feedback">
              Valid password is required and must have at least 8 characters.
            </div>
          </div>
          <%@ include file="components/sections/google_login_button.jspf" %>
          <button type="submit" class="btn btn-primary rounded submit mt-3 px-3">Log in</button>
        </form>
        <p class="text-center mt-3">No account yet? <a href="${pageContext.request.contextPath}/signup">Sign up here.</a></p>
      </div>
    </div>
  </div>
</div>

<%@ include file="components/sections/footer.jspf" %>
<%@ include file="components/imports/javascript.jspf" %>
<script src="${pageContext.request.contextPath}/components/utils/validation.js"></script>
</body>
</html>
