<template>
  <div class="login-container">
    <h2>Prijava</h2>
    <form @submit.prevent="handleLogin">
      <input v-model="username" type="text" placeholder="Korisničko ime" required />
      <input v-model="password" type="password" placeholder="Lozinka" required />
      <button type="submit">Uloguj se</button>
    </form>
    <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { loginUser } from '@/api/auth'
import {useRouter} from "vue-router";

const username = ref('')
const password = ref('')
const errorMessage = ref('')
const router = useRouter()

const handleLogin = async () => {
  try {
    const user = await loginUser({ username: username.value, password: password.value })
    localStorage.setItem('user', JSON.stringify(user))
    router.push('/profile')
  } catch (err) {
    errorMessage.value = 'Pogrešan username ili lozinka'
  }
}
</script>

<style scoped>
.login-container {
  max-width: 400px;
  margin: auto;
  padding: 2rem;
}

input {
  display: block;
  margin-bottom: 1rem;
  width: 100%;
  padding: 0.5rem;
}

button {
  padding: 0.5rem 1rem;
}

.error {
  color: red;
  margin-top: 1rem;
}
</style>
