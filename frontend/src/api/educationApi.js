import axiosInstance from './axiosInstance';

const educationApi = {
  // NGO - students
  registerStudent: (payload) => axiosInstance.post('/api/education/students', payload),
  updateStudent: (id, payload) => axiosInstance.put(`/api/education/students/${id}`, payload),
  myStudents: () => axiosInstance.get('/api/education/students/my-ngo'),

  // NGO - assignments
  assignEducator: (payload) => axiosInstance.post('/api/education/assignments', payload),
  cancelAssignment: (id) => axiosInstance.patch(`/api/education/assignments/${id}/cancel`),
  ngoAssignments: () => axiosInstance.get('/api/education/assignments/ngo/history'),

  // Educator
  myAssignedStudents: () => axiosInstance.get('/api/education/assignments/my-students'),
  completeAssignment: (id) => axiosInstance.patch(`/api/education/assignments/${id}/complete`),

  // Admin
  allStudents: () => axiosInstance.get('/api/education/admin/students'),
  allAssignments: () => axiosInstance.get('/api/education/admin/assignments'),
  stats: () => axiosInstance.get('/api/education/admin/stats'),
  recent: (limit = 5) => axiosInstance.get(`/api/education/admin/recent?limit=${limit}`)
};

export default educationApi;
