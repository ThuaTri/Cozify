"use strict";
$(document).ready(function () {
  // Default configuration for all DataTables
  Object.assign(DataTable.defaults, {
    search: {
      return: true,
    },
    pagingType: "full_numbers",
    scrollX: true,
  });

  if (document.querySelector("#category-table")) {
    let categoryTable = new DataTable("#category-table");
  }
  if (document.querySelector("#clothes-table")) {
    let clothesTable = new DataTable("#clothes-table");
  }
  if (document.querySelector("#voucher-table")) {
    let voucherTable = new DataTable("#voucher-table", {
      "order": [[0, "desc"]],
    });
  }
  if (document.querySelector("#order-table")) {
    let orderTable = new DataTable("#order-table", {
      "order": [[0, "desc"]],
    });
  }
  if (document.querySelector("#staff-table")) {
    let staffTable = new DataTable("#staff-table");
  }
  if (document.querySelector("#user-table")) {
    let userTable = new DataTable("#user-table");
  }
});
