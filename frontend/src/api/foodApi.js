import axiosInstance from './axiosInstance';

const foodApi = {
  // Donor
  create: (payload) => axiosInstance.post('/api/food-donations', payload),
  update: (id, payload) => axiosInstance.put(`/api/food-donations/${id}`, payload),
  remove: (id) => axiosInstance.delete(`/api/food-donations/${id}`),
  myDonations: () => axiosInstance.get('/api/food-donations/my-donations'),

  // NGO
  available: () => axiosInstance.get('/api/food-donations/available'),
  accept: (id) => axiosInstance.post(`/api/food-donations/${id}/accept`),
  reject: (id) => axiosInstance.post(`/api/food-donations/${id}/reject`),
  markPickedUp: (id) => axiosInstance.patch(`/api/food-donations/${id}/pickup`),
  markDelivered: (id) => axiosInstance.patch(`/api/food-donations/${id}/delivered`),
  ngoHistory: () => axiosInstance.get('/api/food-donations/ngo/history'),

  // Admin
  allDonations: () => axiosInstance.get('/api/food-donations/admin/all'),
  stats: () => axiosInstance.get('/api/food-donations/admin/stats'),
  recent: (limit = 5) => axiosInstance.get(`/api/food-donations/admin/recent?limit=${limit}`)
};

export default foodApi;
