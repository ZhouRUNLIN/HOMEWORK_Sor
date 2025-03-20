<template>
  <NavBar />
  <div class="book-details-container">
    <!-- 左侧书籍图片和操作按钮 -->
    <div class="left-section">
      <img :src="book.img || '/default-book-cover.jpg'" alt="书籍封面" class="book-image" />
      <div class="buttons">
        <button class="btn-read" @click="readBook">Read Now</button>
        <button class="btn-add" @click="addToFavorites">Add to favorite</button>
      </div>
    </div>

    <!-- 右侧书籍信息 -->
    <div class="right-section">
      <h2>{{ book.title }}</h2>
      <table class="book-info-table">
        <tr>
          <td><strong>Author :</strong></td>
          <td>{{ book.author }}</td>
        </tr>
        <tr>
          <td><strong>Category :</strong></td>
          <td>{{ book.category }}</td>
        </tr>
        <tr>
          <td><strong>Language :</strong></td>
          <td>{{ book.language }}</td>
        </tr>
        <tr>
          <td><strong>Description :</strong></td>
          <td>{{ truncateText(book.description, 100) }}</td>
        </tr>
        <tr>
          <td><strong>ClickCount :</strong></td>
          <td>{{ book.clickCount }}</td>
        </tr>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import axios from 'axios';
import NavBar from '../NavBar.vue';

const route = useRoute();
const router = useRouter();
const bookId = route.params.bookId;

const book = ref({});
const gid = localStorage.getItem('gid');
const username = localStorage.getItem('username');

// 获取书籍详情
async function fetchBookDetails() {
  try {
    const queryParams = { bookId: bookId, };

    const response = await axios.get('/api/bmr/project/v1/bookSearch_by_id', {
          params: queryParams,
    });
    console.log(response.data);
    if (response.data.code === '0') {
        book.value = response.data.data;
    } else {
      console.error('获取书籍详情失败：', data.message);
    }
  } catch (error) {
    console.error('获取书籍详情异常：', error);
  }
}

// 跳转到阅读页面
function readBook() {
  router.push(`/read/${bookId}`);
}

function truncateText(text, maxLength) {
  if (!text) return "";
  return text.length > maxLength ? text.slice(0, maxLength) + "..." : text;
}

// 添加到收藏
async function addToFavorites() {
  try {
    const response = await fetch('/api/bmr/project/v1/bookmark', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        gid,
        username,
        bookId,
      }),
    });

    const data = await response.json();
    console.log("test", data);
    console.log(gid);
    console.log(username);
    if (data.code === '0') {
      alert('Success');
    } else {
      alert(`收藏失败：${data.message}`);
    }
  } catch (error) {
    console.error('添加收藏异常：', error);
    alert('收藏失败，请稍后重试。');
  }
}

// 初始化页面
onMounted(() => {
  fetchBookDetails();
});
</script>

<style scoped>
.book-details-container {
  display: flex;
  justify-content: space-between;
  padding: 20px;
  max-width: 1000px;
  margin: 0 auto;
  background: #ffffff;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  font-family: Arial, sans-serif;
}

.left-section {
  flex: 1;
  text-align: center;
}

.book-image {
  width: 200px;
  height: 300px;
  object-fit: cover;
  margin-bottom: 20px;
  border-radius: 8px;
  border: 1px solid #ddd;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.buttons {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.buttons button {
  padding: 10px 20px;
  font-size: 14px;
  font-weight: bold;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.2s ease, box-shadow 0.2s ease;
}

.btn-read {
  background-color: #4caf50;
  color: white;
}

.btn-read:hover {
  background-color: #45a049;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.btn-add {
  background-color: #2196f3;
  color: white;
}

.btn-add:hover {
  background-color: #1976d2;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.right-section {
  flex: 2;
  padding: 0 20px;
}

h2 {
  font-size: 1.5rem;
  color: #333;
  margin-bottom: 20px;
}

.book-info-table {
  width: 100%;
  border-collapse: collapse;
}

.book-info-table td {
  padding: 8px 10px;
  border-bottom: 1px solid #ddd;
}

.book-info-table tr:last-child td {
  border-bottom: none;
}

.book-info-table td:first-child {
  font-weight: bold;
  color: #555;
}
</style>
