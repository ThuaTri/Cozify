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
            <h3 class="mb-4">Log in</h3>
          </div>
        </div>
        <form action="/login" method="post" class="login-form d-flex flex-column">
          <div class="form-group mb-3">
            <label class="label" for="email">Email</label>
            <input id="email" name="email" type="email" class="form-control" placeholder="" required="">
          </div>
          <div class="form-group mb-3">
            <label class="label" for="password">Password</label>
            <input id="password" name="password" type="password" class="form-control" placeholder="" required="">
          </div>
          <div class="form-group mt-3">
            <button type="submit" class="form-control btn btn-primary rounded submit px-3">Log in</button>
          </div>
        </form>
        <p class="text-center">No account yet? <a data-toggle="tab" href="/signup">Sign up here.</a></p>
      </div>
    </div>
  </div>
</div>

<%@ include file="components/sections/footer.jspf" %>
<%@ include file="components/imports/javascript.jspf" %>
<script src="${pageContext.request.contextPath}/components/utils/validation.js"></script>
</body>
</html>
