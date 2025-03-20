<template>
  <div class="search-bar">
    <div class="search-field">
      <label>Title :</label>
      <input type="text" v-model="title" @keyup.enter="emitSearch" />
    </div>
    <div class="search-field">
      <label>Author :</label>
      <input type="text" v-model="author" @keyup.enter="emitSearch" />
    </div>
    <div class="search-field">
      <label>Category :</label>
      <input type="text" v-model="category" @keyup.enter="emitSearch" />
    </div>
    <div class="search-field">
      <label>Language :</label>
      <input type="text" v-model="language" @keyup.enter="emitSearch" />
    </div>
    <button @click="emitSearch">Search :</button>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();

const title = ref('');
const author = ref('');
const category = ref('');
const language = ref('');

function emitSearch() {
  const searchParams = {
    title: title.value,
    author: author.value,
    category: category.value,
    language: language.value,
  };

  // 跳转到 search/:pageNo 页面，默认从第一页开始
  router.push({
    name: 'Search',
    params: { pageNo: 1 },
    query: searchParams,
  });
}
</script>

<style scoped>
.search-bar {
  display: flex;
  align-items: flex-start;
  gap: 16px;
  flex-wrap: wrap;
  margin: 16px 0;
}

.search-field {
  display: flex;
  flex-direction: column;
}

button {
  padding: 10px 20px;
  font-size: 14px;
  font-weight: bold;
  background-color: #4caf50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.2s ease;
}

button:hover {
  background-color: #45a049;
}
</style>
