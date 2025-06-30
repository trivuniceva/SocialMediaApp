import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import LoginView from "@/views/auth/LoginView.vue";
import UserProfile from "@/views/user/UserProfile.vue";
import SignupView from "@/views/auth/SignupView.vue";
import SearchUsersView from "@/views/SearchUsersView.vue";
import EditProfile from "@/views/user/EditProfile.vue";
import AddImage from "@/views/AddImage.vue";
import AddPost from "@/views/AddPost.vue";

const routes = [
  { path: '/', name: 'home', component: HomeView },
  { path: '/login', name: 'login', component: LoginView },
  { path: '/signup', name: 'signup', component: SignupView },
  { path: '/profile', component: UserProfile },
  { path: '/search', component:SearchUsersView },
  { path: '/edit-profile', component:EditProfile },
  { path: '/profile/:id?', name: 'UserProfile', component: () => import('@/views/user/UserProfile.vue')},
  { path: '/add-image', name: 'add-image', component:AddImage, meta: { requiresAuth: true } },
  { path: '/add-post', name: 'add-post', component:AddPost, meta: { requiresAuth: true } },

]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
