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
  "error-register-existing-email": "An account with this email already exists.",
  "error-verify-email": "Could not verify your email.",
  "error-send-otp": "Could not send verification code. Please try again.",
  "error-wrong-otp": "Invalid verification code.",
  "error-change-password": "Could not change user password.",
  "error-update-info": "Could not update user information.",
  "error-login": "Could not log in. Please try again.",
  "error-login-credentials": "Please check your email and password.",
  "error-no-email-found": "No valid email was found. Please double-check your email.",
  "error-404": "Could not find the requested page."
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