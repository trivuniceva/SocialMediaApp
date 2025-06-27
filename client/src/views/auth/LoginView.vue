<template>
  <div class="login-container">
    <h2>Login Page</h2>
    <form @submit.prevent="handleLogin" class="login-form">
      <input
          type="text"
          v-model="username"
          placeholder="Username:"
          required
          class="rounded-input"
      />
      <input
          type="password"
          v-model="password"
          placeholder="Password:"
          required
          class="rounded-input"
      />
      <button type="submit" class="rounded-button">Login</button>
    </form>

    <p>
      Don’t have an account?
      <strong><router-link to="/signup" style="color: #F86B86;">Sign up</router-link></strong>
    </p>
    <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { loginUser } from '@/api/auth'
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'

const username = ref('')
const password = ref('')
const errorMessage = ref('')
const router = useRouter()
const store = useStore()

const handleLogin = async () => {
  try {
    const user = await loginUser({ username: username.value, password: password.value })
    store.commit('setLoggedUser', user)
    localStorage.setItem('loggedUser', JSON.stringify(user))
    router.push('/profile')
  } catch (err) {
    errorMessage.value = 'Pogrešan username ili lozinka'
  }
}
</script>


<style scoped>
.login-container {
  text-align: center;
  padding: 20px;
}

.login-form {
  max-width: 300px;
  margin: 0 auto;
}

.rounded-input {
  border: 1px solid #ccc;
  border-radius: 10px;
  padding: 8px;
  margin: 6px 0;
  width: 100%;
}

.rounded-button {
  background-color: #2c3e50;
  color: #fff;
  border: none;
  border-radius: 10px;
  padding: 10px;
  cursor: pointer;
  width: 40%;
  font-size: 16px;
}

.rounded-button:hover {
  background-color: #F8AFB4;
  color: #2c3e50;
}

.error {
  color: red;
  margin-top: 1rem;
}
</style>
