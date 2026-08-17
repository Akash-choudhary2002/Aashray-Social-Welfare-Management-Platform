import { toast } from 'react-toastify';

// Thin wrapper so pages don't import react-toastify directly everywhere,
// and so default options (position, autoClose) stay consistent app-wide.
const defaultOptions = {
  position: 'top-right',
  autoClose: 3500,
  hideProgressBar: false,
  closeOnClick: true,
  pauseOnHover: true
};

export const notifySuccess = (message) => toast.success(message, defaultOptions);
export const notifyError = (message) => toast.error(message || 'Something went wrong', defaultOptions);
export const notifyInfo = (message) => toast.info(message, defaultOptions);
