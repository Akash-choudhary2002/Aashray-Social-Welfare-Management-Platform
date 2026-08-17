import axiosInstance from './axiosInstance';

const chatbotApi = {
  // Public endpoint — works logged out or logged in. When a token is
  // present, axiosInstance attaches it automatically for a tailored reply.
  chat: (message, sessionId) => axiosInstance.post('/api/chatbot/chat', { message, sessionId })
};

export default chatbotApi;
