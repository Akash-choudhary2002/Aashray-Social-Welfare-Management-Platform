import axiosInstance from './axiosInstance';

const volunteerApi = {
  // NGO
  assignTask: (payload) => axiosInstance.post('/api/volunteers/tasks', payload),
  cancelTask: (id) => axiosInstance.patch(`/api/volunteers/tasks/${id}/cancel`),
  ngoTasks: () => axiosInstance.get('/api/volunteers/tasks/ngo/history'),

  // Volunteer
  myTasks: () => axiosInstance.get('/api/volunteers/tasks/my-tasks'),
  completedTasks: () => axiosInstance.get('/api/volunteers/tasks/completed'),
  startTask: (id) => axiosInstance.patch(`/api/volunteers/tasks/${id}/start`),
  completeTask: (id) => axiosInstance.patch(`/api/volunteers/tasks/${id}/complete`),

  // Admin
  allTasks: () => axiosInstance.get('/api/volunteers/admin/all'),
  stats: () => axiosInstance.get('/api/volunteers/admin/stats'),
  recent: (limit = 5) => axiosInstance.get(`/api/volunteers/admin/recent?limit=${limit}`)
};

export default volunteerApi;
