<template>
  <div>
    <nav v-if="!loggedUser">
      <router-link to="/">Home</router-link> |
      <router-link to="/search">Search</router-link> |
      <router-link to="/login">Login</router-link>
    </nav>

    <nav v-else>
      <router-link to="/">Home</router-link> |
      <router-link to="/search">Search</router-link> |
      <router-link to="/profile">Profile</router-link> |
      <router-link to="/edit-profile">Edit Profile</router-link> |
      <router-link to="/add-image">Add Image</router-link> |
      <a href="#" @click.prevent="logout">Logout</a>

    </nav>

    <BackgroundVideo v-if="showVideo" />
  </div>
</template>


<script>
import { mapState } from 'vuex'
import BackgroundVideo from "@/views/BackgroundVideo.vue";

export default {
  name: "NavbarComponent",
  components: {
    BackgroundVideo
  },
  computed: {
    ...mapState(['loggedUser']),
    showVideo() {
      return this.$route.path === '/' || this.$route.path === '/login' || this.$route.path === '/signup'
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
a {
  cursor: pointer;
  color: #2c3e50;
  text-decoration: none;
}
a:hover {
  color: #F8AFB4;
  text-decoration: underline;
}
</style>
