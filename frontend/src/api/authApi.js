import axiosInstance from './axiosInstance';

const authApi = {
  register: (payload) => axiosInstance.post('/api/auth/register', payload),
  login: (payload) => axiosInstance.post('/api/auth/login', payload),

  getProfile: () => axiosInstance.get('/api/auth/profile'),
  updateProfile: (payload) => axiosInstance.put('/api/auth/profile', payload),

  // Admin - user management
  getAllUsers: () => axiosInstance.get('/api/auth/admin/users'),
  getUsersByRole: (role) => axiosInstance.get(`/api/auth/admin/users/role/${role}`),
  setUserEnabled: (userId, enabled) =>
    axiosInstance.patch(`/api/auth/admin/users/${userId}/status?enabled=${enabled}`),
  getDashboardCounts: () => axiosInstance.get('/api/auth/admin/dashboard/counts')
};

export default authApi;
