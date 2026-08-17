import axios from 'axios';

const getBaseUrl = () => {
  const envUrl = import.meta.env.VITE_API_BASE_URL;
  if (envUrl && !envUrl.includes('184.73.25.128')) {
    return envUrl;
  }
  if (typeof window !== 'undefined' && window.location && window.location.hostname) {
    return `http://${window.location.hostname}:8080`;
  }
  return 'http://localhost:8080';
};

const BASE_URL = getBaseUrl();

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
