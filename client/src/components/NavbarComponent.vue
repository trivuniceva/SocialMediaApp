<template>
  <div>
    <nav v-if="!loggedUser">
      <router-link to="/">Home</router-link> |
      <router-link to="/login">Login</router-link>
    </nav>

    <nav v-else>
      <router-link to="/">Home</router-link> |
      <router-link to="/">Search</router-link> |
      <a href="#" @click.prevent="logout">Logout</a>
    </nav>
  </div>
</template>

<script>
import { mapState } from 'vuex'

export default {
  name: "NavbarComponent",

  computed: {
    ...mapState(['loggedUser']),
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
