<template>
  <NavBar/>
  <div class="search-page">
    <!-- 顶部搜索栏 -->
    <SearchBar @search="handleSearch" />

    <!-- 搜索结果展示 -->
    <div class="search-result">
      <p v-if="total > 0">Total match Number :{{ total }} </p>
      <p v-else>No matches</p>

      <div class="book-list">
        <div
          v-for="(book, index) in books"
          :key="book.id"
          class="book-item"
          @click="goToDetail(book.id)"
        >
          <img :src="book.img" alt="Book cover" class="book-cover" />
          <p>{{ book.title }} </p>
        </div>
      </div>
    </div>

    <!-- 分页组件 -->
    <div v-if="total > 0" class="pagination">
      <button
        :disabled="currentPage === 1"
        @click="changePage(currentPage - 1)"
      >
        Previous
      </button>
      <span> Page {{ currentPage }}  / total {{ totalPages }} pages </span>
      <button
        :disabled="currentPage === totalPages"
        @click="changePage(currentPage + 1)"
      >
        Next
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import axios from 'axios';
import SearchBar from '../SearchBar.vue';
import NavBar from "../NavBar.vue";

const route = useRoute();
const router = useRouter();

const books = ref([]);
const total = ref(0);
const currentPage = ref(Number(route.params.pageNo) || 1);

// 每页的数量
const pageSize = 10;
// 计算总页数
const totalPages = computed(() => Math.ceil(total.value / pageSize));

// 监听路由参数变化
watch(
  () => [
    route.params.pageNo,
    route.query.title,
    route.query.author,
    route.query.category,
    route.query.language,
  ],
  () => {
    fetchAndRenderBooks();
  },
  { immediate: true }
);

// 获取书籍数据
async function fetchAndRenderBooks() {
  try {
    const queryParams = {
      title: route.query.title || '',
      author: route.query.author || '',
      category: route.query.category || '',
      language: route.query.language || '',
      pageNo: currentPage.value,
      pageSize,
    };

    const response = await axios.get('/api/bmr/project/v1/bookSearch_page', {
      params: queryParams,
    });
    console.log(response.data.data);
    if (response.data.code) {
        total.value = response.data.data.total;
        books.value = response.data.data.records.map(record => ({
            id: record.id,
            title: record.title,
            img: record.img,
        }));
    } else {
      total.value = 0;
      books.value = [];
    }
  } catch (err) {
    console.error('搜索异常：', err);
    total.value = 0;
    books.value = [];
  }
}

// 处理分页跳转
function changePage(page) {
  if (page < 1 || page > totalPages.value) return;
  currentPage.value = page;
  router.push({
    name: 'Search',
    params: { pageNo: page },
    query: route.query,
  });
}

// 搜索栏触发搜索
function handleSearch(payload) {
  router.push({
    name: 'Search',
    params: { pageNo: 1 },
    query: payload,
  });
}

// 跳转到书籍详情
function goToDetail(bookId) {
  router.push({ name: 'BookDetail', params: { bookId } });
}
</script>

<style scoped>
.search-page {
  padding: 16px;
}

.search-result {
  margin-top: 16px;
}

.book-list {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
}

.book-item {
  width: 120px;
  text-align: center;
  cursor: pointer;
}

.book-cover {
  width: 100px;
  height: 140px;
  object-fit: cover;
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 16px;
  margin-top: 16px;
}

.pagination button {
  padding: 8px 16px;
  cursor: pointer;
}

.pagination button:disabled {
  cursor: not-allowed;
  opacity: 0.5;
}
</style>
