<template>
  <div>
    <nav class="navbar">
      <router-link to="/">Home</router-link>
      <router-link to="/search">Search</router-link>

      <template v-if="!loggedUser">
        <router-link to="/login">Login</router-link>
      </template>

      <template v-else>
        <div class="dropdown">
          <span class="dropdown-trigger">Profile ▾</span>
          <div class="dropdown-menu">
            <router-link to="/profile">View Profile</router-link>
            <router-link to="/edit-profile">Edit Profile</router-link>
          </div>
        </div>

        <div class="dropdown">
          <span class="dropdown-trigger">Content ▾</span>
          <div class="dropdown-menu">
            <router-link to="/add-image">Add Image</router-link>
            <router-link to="/add-post">Add Post</router-link>
          </div>
        </div>

        <a href="#" @click.prevent="logout">Logout</a>
      </template>
    </nav>

    <BackgroundVideo v-if="showVideo" />
  </div>
</template>

<script>
import { mapState } from 'vuex'
import BackgroundVideo from "@/views/BackgroundVideo.vue"

export default {
  name: "NavbarComponent",
  components: {
    BackgroundVideo
  },
  computed: {
    ...mapState(['loggedUser']),
    showVideo() {
      return ['/', '/login', '/signup'].includes(this.$route.path)
    }
  },
  methods: {
    logout() {
      this.$store.commit('clearLoggedUser')
      localStorage.removeItem('loggedUser')
      this.$router.push('/login')
    }
  }
}
</script>

<style scoped>
.navbar {
  display: flex;
  gap: 20px;
  padding: 20px;
  justify-content: center;
  font-weight: 500;
  font-size: 1rem;
}

a, .dropdown-trigger {
  cursor: pointer;
  color: #2c3e50;
  text-decoration: none;
  position: relative;
}

a:hover, .dropdown-trigger:hover {
  color: #F8AFB4;
  text-decoration: underline;
}

.dropdown {
  position: relative;
}

.dropdown-menu {
  display: none;
  position: absolute;
  top: 24px;
  left: 0;
  background-color: white;
  border: 1px solid #eee;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  min-width: 150px;
  z-index: 10;
  padding: 8px 0;
}

.dropdown:hover .dropdown-menu {
  display: flex;
  flex-direction: column;
}

.dropdown-menu router-link {
  padding: 8px 16px;
  color: #2c3e50;
  text-decoration: none;
  white-space: nowrap;
}

.dropdown-menu router-link:hover {
  background-color: #f9f9f9;
  color: #f86b86;
}
</style>
