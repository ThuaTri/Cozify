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
            <h5 class="card-title fw-semibold mb-4">Update Voucher</h5>
            <form method="post" action="update" class="needs-validation" novalidate="">
              <p>Input fields marked with <span class="text-danger">*</span> are required</p>
              <div class="mb-3">
                <input type="hidden" name="id" value="${voucher.voucherId}">
                <label for="voucherName" class="form-label">Voucher name <span class="text-danger">*</span></label>
                <input type="text" maxlength="200" class="form-control" id="voucherName" name="voucherName" placeholder=""
                       value="${voucher.voucherName}">
                <div class="form-text">Valid voucher name should not exceed 200 characters.</div>
                <div class="invalid-feedback">
                  Voucher name is invalid.
                </div>
              </div>
              <div class="mb-3">
                <label for="voucherCode" class="form-label">Voucher code <span class="text-danger">*</span></label>
                <input type="text" maxlength="16" pattern="[a-zA-Z0-9]{16}" class="form-control" id="voucherCode" name="voucherCode" placeholder=""
                       value="${voucher.voucherCode}">
                <div class="form-text">Valid voucher code contains exactly 16 alphanumeric characters.</div>
                <div class="invalid-feedback">
                  Voucher code is invalid.
                </div>
              </div>
              <div class="mb-3">
                <label for="voucherPercent" class="form-label">Value (percentage) <span class="text-danger">*</span></label>
                <input type="number" step="1" min="0" max="100" pattern="[0-9]{1,}" class="form-control" id="voucherPercent" name="voucherPercent"
                       placeholder="" value="${voucher.voucherPercent}">
                <div class="form-text">Valid value should be an integer between 0 to 100.
                </div>
                <div class="invalid-feedback">
                  Value is not valid.
                </div>
              </div>
              <div class="mb-3">
                <label for="voucherQuantity" class="form-label">Quantity <span class="text-danger">*</span></label>
                <input type="number" step="1" min="0" pattern="[0-9]{1,}" class="form-control" id="voucherQuantity" name="voucherQuantity"
                       placeholder="" value="${voucher.voucherQuantity}">
                <div class="form-text">Valid quantity should be a non-negative integer.
                </div>
                <div class="invalid-feedback">
                  Quantity is not valid.
                </div>
              </div>
              <div class="row">
                <div class="col-6 mb-3">
                  <label for="startDate" class="form-label">Starting time <span class="text-danger">*</span></label>
                  <input type="datetime-local" class="form-control" id="startDate" name="startDate" placeholder=""
                         required="" min="" onchange="setMinEndDate()"
                         value="<fmt:formatDate value="${voucher.startDate}" pattern="yyyy-MM-dd hh:mm"/>">
                  <div class="form-text">Valid starting date must be set in or after today.</div>
                  <div class="invalid-feedback">
                    Valid starting date is required.
                  </div>
                </div>
                <div class="col-6 mb-3">
                  <label for="endDate" class="form-label">Ending time <span class="text-danger">*</span></label>
                  <input type="datetime-local" class="form-control" id="endDate" name="endDate" placeholder=""
                         required="" min=""
                         value="<fmt:formatDate value="${voucher.endDate}" pattern="yyyy-MM-dd hh:mm"/>">
                  <div class="form-text">Valid ending date must be set after starting date.</div>
                  <div class="invalid-feedback">
                    Valid ending date is required.
                  </div>
                </div>
              </div>
              <div class="mb-3 form-check">
                <input type="checkbox" class="form-check-input" id="check-disable" name="isHidden" value="true" ${voucher.isHidden ? "checked" : ""}>
                <label class="form-check-label" for="check-disable">Disable this voucher</label>
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
<script>
  document.addEventListener("DOMContentLoaded", () => {
    let startDate = document.getElementById("startDate");
    let startDateValue = startDate.value;
    if (startDateValue === "") {
      startDate.setAttribute("min", new Date().toJSON().substring(0,16));
    } else {
      startDate.setAttribute("min", startDateValue);
    }

    setMinEndDate();
  });

  function setMinEndDate() {
    let startDate = document.getElementById("startDate");
    let endDate = document.getElementById("endDate");
    endDate.setAttribute("min", startDate.value);
  }
</script>
</body>
</html>
