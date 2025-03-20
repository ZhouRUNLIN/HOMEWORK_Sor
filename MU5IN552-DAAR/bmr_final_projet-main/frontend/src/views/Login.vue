<template>
  <div>
    <h2>Login</h2>
    <form @submit.prevent="handleLogin">
      <div>
        <label>Username :</label>
        <input v-model="username" />
      </div>
      <div>
        <label>Password :</label>
        <input v-model="password" type="password" />
      </div>
      <button type="submit">Login</button>
    </form>
    <p v-if="errorMessage" style="color: red;">{{ errorMessage }}</p>
  </div>
</template>

<script>
import { ref } from 'vue';
import { useRouter } from 'vue-router';

export default {
  name: 'Login',
  setup() {
    const username = ref('');
    const password = ref('');
    const errorMessage = ref('');
    const router = useRouter();

    const handleLogin = async () => {
      try {
        // 用户登录请求
        const loginRes = await fetch('/api/bmr/user/v1/user/login', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            username: username.value,
            password: password.value,
          }),
        });

        if (!loginRes.ok) {
          throw new Error(`登录请求失败，状态码：${loginRes.status}`);
        }

        const loginData = await loginRes.json();
        console.log('登录成功后端返回：', loginData);

        if (!loginData || !loginData.data || !loginData.data.token) {
          throw new Error('登录响应格式不正确，缺少 token');
        }

        // 存储 token 和用户名
        localStorage.setItem('token', loginData.data.token);
        localStorage.setItem('username', username.value);

        // 获取用户组信息
        const groupRes = await fetch(`/api/bmr/user/v1/group?username=${username.value}`, {
          method: 'GET',
          headers: { 'Content-Type': 'application/json' },
        });

        if (!groupRes.ok) {
          throw new Error(`用户组请求失败，状态码：${groupRes.status}`);
        }

        const groupData = await groupRes.json();
        console.log('用户组信息返回：', groupData);

        if (!groupData || !groupData.data || !groupData.data.length) {
          throw new Error('未获取到有效的用户组信息');
        }

        // 存储第一个组的 gid
        localStorage.setItem('gid', groupData.data[0].gid);

        // 跳转到用户页面
        router.push(`/${username.value}`);
      } catch (err) {
        errorMessage.value = err.message;
      }
    };

    return {
      username,
      password,
      errorMessage,
      handleLogin,
    };
  },
};
</script>
