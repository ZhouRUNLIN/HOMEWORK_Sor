import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';

export default defineConfig({
  plugins: [vue()],
  server: {
    port: 3000, // Port for the frontend
    proxy: {
        '/api/bmr/user': {
            target: 'http://localhost:8002',
            changeOrigin: true,
        },
        '/api/bmr/project': {
            target: 'http://localhost:8001',
            changeOrigin: true,
        },
    },
  },
});
