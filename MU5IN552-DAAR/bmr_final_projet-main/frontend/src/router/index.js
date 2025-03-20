import { createRouter, createWebHistory } from 'vue-router';
import Welcome from '../views/Welcome.vue';
import Login from '../views/Login.vue';
import Register from '../views/Register.vue';
import User from '../views/User.vue';
import Search from '../views/Search.vue'
import BookDetail from '../views/BookDetail.vue'
import ReadBook from '../views/ReadBook.vue'

const routes = [
  { path: '/', name: 'Welcome', component: Welcome },
  { path: '/login', name: 'Login', component: Login },
  { path: '/register', name: 'Register', component: Register },
  { path: '/:username', name: 'User', component: User, props: true },
  { path: '/search/:pageNo', name: 'Search', component: Search},
  { path: '/book/:bookId', name: 'BookDetail', component: BookDetail},
  { path: '/read/:bookId', name: 'ReadBook', component: ReadBook},
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
