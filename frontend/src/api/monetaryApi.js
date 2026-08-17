import axiosInstance from './axiosInstance';

const monetaryApi = {
  // Donor — Razorpay checkout flow
  createOrder: (payload) => axiosInstance.post('/api/monetary-donations/create-order', payload),
  verifyPayment: (payload) => axiosInstance.post('/api/monetary-donations/verify-payment', payload),
  markPaymentFailed: (payload) => axiosInstance.post('/api/monetary-donations/payment-failed', payload),
  myDonations: () => axiosInstance.get('/api/monetary-donations/my-donations'),
  getById: (id) => axiosInstance.get(`/api/monetary-donations/${id}`),

  // Admin
  allDonations: () => axiosInstance.get('/api/monetary-donations/admin/all'),
  stats: () => axiosInstance.get('/api/monetary-donations/admin/stats'),
  recent: (limit = 5) => axiosInstance.get(`/api/monetary-donations/admin/recent?limit=${limit}`)
};

export default monetaryApi;
