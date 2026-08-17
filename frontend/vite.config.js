import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

// Aashray frontend – talks to the API Gateway (default http://localhost:8080)
// The gateway URL is configurable via VITE_API_BASE_URL in .env
export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    open: true
  }
});
