// Requires importing toast.jspf

const successToast = document.getElementById('success');
const errorToast = document.getElementById('error');

// Initialize toasts
const toastElList = [successToast, errorToast];
const toastList = [...toastElList].map(toastEl => new bootstrap.Toast(toastEl, {
  delay: 3000
}))

const toastMessages = {
  "success-cart": "Successfully added to cart.",
  "success-order": "Successfully placed order.",
  "success-cancel-order": "Successfully canceled order.",
  "success-register": "Successfully registered order.",
  "success-change-password": "Successfully changed user password.",
  "success-update-info": "Successfully updated user information.",
  "error-cart": "Could not add to cart. Please try again.",
  "error-order": "Could not place order. Please try again.",
  "error-cancel-order": "Could not cancel order because it is being delivered.",
  "error-register": "Could not register an account.",
  "error-register-existing-email": "An account with this email or username already exists.",
  "error-verify-email": "Could not verify your email.",
  "error-send-otp": "Could not send verification code. Please try again.",
  "error-wrong-otp": "Invalid verification code.",
  "error-change-password": "Could not change user password.",
  "error-update-info": "Could not update user information.",
  "error-login": "Could not log in. Please try again.",
  "error-login-credentials": "Please check your email and password.",
  "error-no-email-found": "No valid email was found. Please double-check your email.",
  "error-404": "Could not find the requested page.",

  "success-add-category": "Successfully added category.",
  "success-update-category": "Successfully updated category.",
  "success-delete-category": "Successfully disabled category.",
  "success-add-clothes": "Successfully added clothes.",
  "success-update-clothes": "Successfully updated clothes.",
  "success-delete-clothes": "Successfully disabled clothes.",
  "success-add-voucher": "Successfully added voucher.",
  "success-update-voucher": "Successfully updated voucher.",
  "success-delete-voucher": "Successfully deleted voucher.",
  "success-add-order": "Successfully added order.",
  "success-update-order": "Successfully updated order.",
  "success-delete-order": "Successfully deleted order.",
  "success-add-staff": "Successfully added staff.",
  "success-update-staff": "Successfully updated staff.",
  "success-delete-staff": "Successfully deleted staff.",
  "success-add-user": "Successfully added user.",
  "success-update-user": "Successfully updated user.",
  "success-delete-user": "Successfully deleted user.",
  "error-add-category": "Could not add category. Please try again.",
  "error-update-category": "Could not update category. Please try again.",
  "error-delete-category": "Could not disable category. Please try again.",
  "error-add-clothes": "Could not add clothes. Please try again.",
  "error-upload-image": "The uploaded file is either not an image or an unsupported image format. Please upload another image.",
  "error-update-clothes": "Could not update clothes. Please try again.",
  "error-delete-clothes": "Could not disable clothes. Please try again.",
  "error-add-voucher": "Could not add voucher. Please try again.",
  "error-add-voucher-existing-voucher": "A voucher with this name or code already exists.",
  "error-update-voucher": "Could not update voucher. Please try again.",
  "error-delete-voucher": "Could not delete voucher. Please try again.",
  "error-add-order": "Could not add order. Please try again.",
  "error-update-order": "Could not update order. Please try again.",
  "error-delete-order": "Could not delete order. Please try again.",
  "error-empty-cart": "Could not create order because the cart is empty.",
  "error-add-staff": "Could not add staff. Please try again.",
  "error-update-staff": "Could not update staff. Please try again.",
  "error-delete-staff": "Could not delete staff. Please try again.",
  "error-add-user": "Could not add user. Please try again.",
  "error-update-user": "Could not update user. Please try again.",
  "error-delete-user": "Could not delete user. Please try again.",
}

// Get the message from the session scope
let toastContent = successToast.getAttribute('data-message');

function initModal(id) {
  const modal = document.getElementById(`${id}`);
  // Construct the attribute name using the id (e.g. trigger-otp-modal)
  const attributeName = "data-" + id.replace("-modal", "");
  if (modal.getAttribute(attributeName) && modal.getAttribute(attributeName) != "0") {
    const modalInstance = new bootstrap.Modal(`#${id}`, {});
    modal.setAttribute(attributeName, 'true');
    modalInstance.show();
  }
}

function showToast(toast, toastContent) {
  const toastElement = bootstrap.Toast.getOrCreateInstance(toast);
  const toastMessage = toast.getElementsByClassName('toast-message')[0];

  toastMessage.innerHTML = toastMessages[toastContent];

  toastElement.show();
}
function hideToast(toast) {
  bootstrap.Toast.getInstance(toast).hide();
}

// Initialize either of these modals
document.addEventListener('DOMContentLoaded', () => {
  toastContent = successToast.getAttribute('data-message');
  // Show success or error toast based on the message
  if (toastContent) {
    if (toastContent.includes('success')) {
      hideToast(errorToast);
      showToast(successToast, toastContent);
    } else if (toastContent.includes('error')) {
      hideToast(successToast);
      showToast(errorToast, toastContent);
    }
  }
});