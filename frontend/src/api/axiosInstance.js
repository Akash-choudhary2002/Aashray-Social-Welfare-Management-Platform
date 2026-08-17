import axios from 'axios';

// Every request goes through the API Gateway (Phase 1), which routes to
// the right microservice and validates the JWT before forwarding.
const BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080';

const axiosInstance = axios.create({
  baseURL: BASE_URL,
  headers: {
    'Content-Type': 'application/json'
  }
});

// Attach the JWT (if present) to every outgoing request.
axiosInstance.interceptors.request.use((config) => {
  const token = localStorage.getItem('aashray_token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Every backend response is wrapped in { success, message, data, timestamp }.
// Unwrap it here so calling code just gets `data`, and normalize errors so
// every caller can rely on `error.message` and `error.status`.
axiosInstance.interceptors.response.use(
  (response) => response.data,
  (error) => {
    const status = error.response?.status;
    const backendMessage = error.response?.data?.message;

    if (status === 401) {
      // Token missing/expired/invalid — force a clean re-login.
      localStorage.removeItem('aashray_token');
      localStorage.removeItem('aashray_user');
      if (!window.location.pathname.startsWith('/login')) {
        window.location.href = '/login';
      }
    }

    return Promise.reject({
      status,
      message: backendMessage || error.message || 'Something went wrong. Please try again.',
      raw: error
    });
  }
);

export default axiosInstance;
