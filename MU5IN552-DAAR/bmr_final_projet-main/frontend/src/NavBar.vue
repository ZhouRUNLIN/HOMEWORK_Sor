<template>
  <header class="header">
    <!-- 左边按钮 -->
    <div class="left-buttons">
      <button @click="goBack">Back</button>
      <button @click="goToWelcome">Welcome</button>
    </div>

    <!-- 右边按钮 -->
    <div class="right-buttons">
      <template v-if="isLoggedIn">
        <button @click="goToUser">{{ username }}</button>
        <button @click="logout">Logout</button>
      </template>
      <template v-else>
        <button @click="goToLogin">Login</button>
        <button @click="goToRegister">Register</button>
      </template>
    </div>
  </header>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';

const router = useRouter();
const username = ref(localStorage.getItem('username') || ''); // 从本地存储中获取用户名
const isLoggedIn = computed(() => !!localStorage.getItem('token')); // 判断是否已登录

const goBack = () => router.back();
const goToWelcome = () => router.push({ name: 'Welcome' });
const goToLogin = () => router.push({ name: 'Login' });
const goToRegister = () => router.push({ name: 'Register' });
const goToUser = () => router.push({ name: 'User', params: { username: username.value } });

async function logout() {
  const token = localStorage.getItem('token') || '';
  console.log('token:', token);
  try {
    const response = await axios.delete('/api/bmr/user/v1/user/logout', {
      params: {
        username: username.value,
        token: token,
      },
    });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    const result = await response.json();

    if (result.code === '200') {
      // 注销成功
      console.log('注销成功:', result.message);
      localStorage.removeItem('token');
      localStorage.removeItem('username');
      router.push({ name: 'Login' });
    } else {
      console.error('注销失败:', result.message);
    }
  } catch (error) {
    console.error('注销异常:', error);
  }
}

</script>

<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  padding: 1rem;
  background-color: #f4f4f4;
  border-bottom: 1px solid #ddd;
}

.left-buttons,
.right-buttons {
  display: flex;
  gap: 1rem;
  align-items: center;
}

button {
  background-color: #007bff;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  cursor: pointer;
}

button:hover {
  background-color: #0056b3;
}
</style>
