<template>
  <div class="signup-container">
    <h2>SignUp</h2>
    <form @submit.prevent="register" class="signup-form">
      <input type="text" v-model="username" placeholder="Username:" required class="rounded-input" />
      <input type="email" v-model="email" placeholder="Email:" required class="rounded-input" />
      <input type="text" v-model="firstName" placeholder="Name:" required class="rounded-input" />
      <input type="text" v-model="lastName" placeholder="Lastname:" required class="rounded-input" />

      <div class="gender-radio">
        <label for="female">Female</label>
        <input type="radio" id="female" value="female" v-model="gender" required />

        <label for="male">Male</label>
        <input type="radio" id="male" value="male" v-model="gender" required />
      </div>

      <br><br>
      <input type="password" v-model="password" placeholder="Password:" required class="rounded-input" />
      <input type="password" v-model="confirmPassword" placeholder="Confirm password:" required class="rounded-input" />

      <br><br>
      <button type="submit" class="rounded-button">SignUp</button>
    </form>
  </div>
</template>

<script>
export default {
  name: "SignupView"
}
</script>

<style scoped>
.signup-container {
  text-align: center;
  padding: 20px;
}

.signup-form {
  max-width: 300px;
  margin: 0 auto;
}

.gender-radio {
  display: flex;
  justify-content: space-around;
  margin: 10px 0;
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
</style>