<template>
  <NavBar />
  <div class="user-container">

    <div class="search-wrapper">
      <SearchBar @search="handleSearch" />
    </div>

    <div class="recommendations-wrapper">
      <h2>Book Recommendations</h2>
      <div v-if="recommendations.length === 0" class="no-recommendations">
        No Recommendation yet
      </div>
      <div v-else class="books-grid">
        <div
          v-for="book in recommendations"
          :key="book.id"
          class="book-card"
          @click="goToDetail(book.id)"
      >
        <img :src="book.img" alt="Book cover" class="book-cover" />
        <h3>{{ truncateText(book.title, 20) }}</h3>
        <p class="description">{{ truncateText(book.description, 50) }}</p>
        </div>
      </div>
    </div>

    <div class="bookmarks-wrapper">
      <h2>My Collections</h2>
      <div v-if="bookmarks.length === 0" class="no-bookmarks">
        NO Collections yet
      </div>
      <div v-else class="books-grid">
        <div
            v-for="book in bookmarks"
            :key="book.id"
            class="book-card"
            @click="goToDetail(book.id)"
        >
          <img :src="book.img" alt="Book cover" class="book-cover" />
          <h3>{{ truncateText(book.title, 20) }}</h3>
          <p class="description">{{ truncateText(book.description, 50) }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import SearchBar from '../SearchBar.vue';
import NavBar from '../NavBar.vue';

const username = ref(localStorage.getItem('username') || '');
const gid = ref(localStorage.getItem('gid') || '');
const recommendations = ref([]);
const bookmarks = ref([]);

const router = useRouter();

// 获取书籍推荐
async function fetchRecommendations() {
  try {
    const res = await fetch(`/api/bmr/project/v1/recommend?username=${username.value}&pageNo=1&pageSize=10`);
    const data = await res.json();
    console.log(data);
    recommendations.value = data.data || [];
  } catch (err) {
    console.error('获取推荐书籍失败:', err);
  }
}

// 获取收藏书籍
async function fetchBookmarks() {
  try {
    const res = await fetch(`/api/bmr/user/v1/bookmark/search?gid=${gid.value}&username=${username.value}&pageNo=1&pageSize=10`);
    const data = await res.json();
    bookmarks.value = data.data.records || [];
  } catch (err) {
    console.error('获取收藏书籍失败:', err);
  }
}

// 注销
async function logout() {
  localStorage.clear();
  router.push('/login');
}

// 页面加载时初始化数据
onMounted(() => {
  fetchRecommendations();
  fetchBookmarks();
});

// 描述截取
function truncateText(text, maxLength) {
  if (!text) return "";
  return text.length > maxLength ? text.slice(0, maxLength) + "..." : text;
}

function goToDetail(bookId) {
  router.push({ name: 'BookDetail', params: { bookId } });
}
</script>

<style scoped>
.user-container {
  display: flex;
  flex-direction: column;
  height: 100vh;
  font-family: 'Arial', sans-serif;
  background: #f7f7f7;
}

.user-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  background: #4caf50;
  color: white;
  font-size: 1.2em;
}

.header-left .username {
  font-weight: bold;
}

.search-wrapper {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

.recommendations-wrapper,
.bookmarks-wrapper {
  margin: 20px auto;
  width: 90%;
  background: white;
  padding: 16px;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.recommendations-wrapper h2,
.bookmarks-wrapper h2 {
  font-size: 1.5em;
  color: #333;
  text-align: center;
  margin-bottom: 16px;
}

.books-grid {
  display: grid;
  grid-template-columns: repeat(5, 1fr); /* 每行 5 个书籍 */
  gap: 16px;
  margin-top: 16px;
}

.book-card {
  background: #f9f9f9;
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 12px;
  text-align: center;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
  cursor: pointer;
}

.book-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.2);
}

.book-card img {
  width: 100%;
  height: 120px; /* 固定高度使布局整齐 */
  object-fit: cover;
  border-radius: 4px;
  margin-bottom: 8px;
}

.book-card h3 {
  font-size: 1em;
  font-weight: bold;
  margin-bottom: 8px;
  color: #333;
}

.book-card .description {
  font-size: 0.85em;
  color: #666;
  line-height: 1.4;
}

.no-recommendations,
.no-bookmarks {
  text-align: center;
  color: #666;
  margin-top: 16px;
}
</style>

