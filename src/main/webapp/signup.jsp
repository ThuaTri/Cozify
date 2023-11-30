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
<%@ include file="components/sections/header.jspf" %>

<div class="row justify-content-center">
  <div class="col-md-12 col-lg-10">
    <div class="wrap d-md-flex">
      <div class="login-wrap p-4 p-md-5 w-50">
        <div class="d-flex">
          <div class="w-100">
            <h3 class="mb-4">Sign In</h3>
          </div>
          <div class="w-100">
            <p class="social-media d-flex justify-content-end">
              <a href="#" class="social-icon d-flex align-items-center justify-content-center"><span
                  class="fa fa-facebook"></span></a>
              <a href="#" class="social-icon d-flex align-items-center justify-content-center"><span
                  class="fa fa-twitter"></span></a>
            </p>
          </div>
        </div>
        <form action="#" class="signin-form">
          <div class="form-group mb-3">
            <label class="label" for="username">Username</label>
            <input id="username" type="text" class="form-control" placeholder="username" required="">
          </div>
          <div class="form-group mb-3">
            <label class="label" for="password">Password</label>
            <input id="password" type="password" class="form-control" placeholder="password" required="">
          </div>
          <div class="form-group">
            <button type="submit" class="form-control btn btn-primary rounded submit px-3">Sign In</button>
          </div>
          <div class="form-group d-md-flex">
            <div class="w-50 text-left">
              <label class="checkbox-wrap checkbox-primary mb-0">Remember Me
                <input type="checkbox" checked="">
                <span class="checkmark"></span>
              </label>
            </div>
            <div class="w-50 text-md-right">
              <a href="#">Forgot Password</a>
            </div>
          </div>
        </form>
        <p class="text-center">Not a member? <a data-toggle="tab" href="#signup">Sign Up</a></p>
      </div>
    </div>
  </div>
</div>

<%@ include file="components/sections/footer.jspf" %>
<%@ include file="components/imports/javascript.jspf" %>
<script src="${pageContext.request.contextPath}/components/utils/validation.js"></script>
</body>
</html>
