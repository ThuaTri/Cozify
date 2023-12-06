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
            <h5 class="card-title fw-semibold mb-4">Add Clothes</h5>
            <form method="post" action="add" class="needs-validation" novalidate="" enctype="multipart/form-data">
              <p>Input fields marked with <span class="text-danger">*</span> are required</p>
              <div class="mb-3">
                <label for="categoryId" class="form-label">Category <span class="text-danger">*</span></label>
                <select class="form-select" id="categoryId" name="categoryId" required="">
                  <c:forEach var="c" items="${categories}">
                    <option value="${c.categoryId}">${c.categoryName}</option>
                  </c:forEach>
                </select>
                <div class="invalid-feedback">
                  Valid category is required.
                </div>
              </div>
              <div class="mb-3">
                <label for="clothesName" class="form-label">Clothes name <span class="text-danger">*</span></label>
                <input type="text" class="form-control" id="clothesName" name="clothesName" placeholder="" required="">
                <div class="form-text">Valid clothes name should not exceed 255 characters.</div>
                <div class="invalid-feedback">
                  Valid clothes name is required.
                </div>
              </div>
              <div class="mb-3">
                <label for="price" class="form-label">Price <span class="text-danger">*</span></label>
                <div class="input-group">
                  <span class="input-group-text">$</span>
                  <input type="number" min="0" max="10000" class="form-control" id="price" name="price" placeholder="" required=""
                         value="0">
                </div>
                <div class="form-text">Valid price should be less than $10,000.</div>
                <div class="invalid-feedback">
                  Valid price is required.
                </div>
              </div>
              <div class="mb-3">
                <label for="discount" class="form-label">Discount <span class="text-danger">*</span></label>
                <input type="number" min="0" max="100" class="form-control" id="discount" name="discount" placeholder="" required=""
                       value="0">
                <div class="form-text">Valid discount percentage should be an integer within 0 - 100.</div>
                <div class="invalid-feedback">
                  Valid discount percentage is required.
                </div>
              </div>
              <div class="mb-3">
                <label for="rating" class="form-label">Rating <span class="text-danger">*</span></label>
                <select class="form-select" id="rating" name="rating" required="">
                  <option value="5" selected>5 stars</option>
                  <option value="4">4 stars</option>
                  <option value="3">3 stars</option>
                  <option value="2">2 stars</option>
                  <option value="1">1 star</option>
                  <option value="0">0 star</option>
                </select>
                <div class="invalid-feedback">
                  Valid rating is required.
                </div>
              </div>
              <div class="mb-3">
                <label for="stockQuantity" class="form-label">Quantity <span class="text-danger">*</span></label>
                <input type="number" min="0" class="form-control" id="stockQuantity" name="stockQuantity" placeholder=""
                       required="" value="0">
                <div class="form-text">Valid quantity should be an non-negative integer.</div>
                <div class="invalid-feedback">
                  Valid quantity is required.
                </div>
              </div>
              <div class="mb-3">
                <label for="size" class="form-label">Size</label>
                <input type="text" class="form-control" maxlength="10" id="size" name="size" placeholder="Leave blank if item does not have a size">
                <div class="form-text">Valid size should not exceed 10 characters, and should not contain Unicode
                  characters.
                </div>
                <div class="invalid-feedback">
                  Size should not exceed 10 characters.
                </div>
              </div>
              <div class="mb-3">
                <label for="image" class="form-label">Image <span class="text-danger">*</span></label>
                <input type="file" class="form-control" id="image" name="image" required="">
                <div class="form-text">Accepts only .jpg image files.
                </div>
                <div class="invalid-feedback">
                  Valid image file is required.
                </div>
              </div>
              <div class="mb-3 form-check">
                <input type="checkbox" class="form-check-input" id="check-disable" name="isHidden" value="true">
                <label class="form-check-label" for="check-disable">Disable this clothes upon creation</label>
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
