<template>
  <div class="edit-profile-container">
    <h2>Edit Profile</h2>

    <form v-if="user" class="edit-profile-form" @submit.prevent="saveChanges">
      <input
          type="email"
          v-model="user.email"
          placeholder="Email:"
          required
          class="rounded-input"
      />
      <input
          type="text"
          v-model="user.firstName"
          placeholder="First Name:"
          required
          class="rounded-input"
      />
      <input
          type="text"
          v-model="user.lastName"
          placeholder="Last Name:"
          required
          class="rounded-input"
      />
      <input
          type="date"
          v-model="user.dateOfBirth"
          class="rounded-input"
      />

      <div class="form-row">
        <label for="privateProfile" class="form-label">Private Profile:</label>
        <input
            type="checkbox"
            id="privateProfile"
            v-model="user.privateAccount"
        />
      </div>

      <label for="gender" class="form-label">Gender:</label>
      <select id="gender" v-model="user.gender" class="rounded-input">
        <option disabled value="">Select gender</option>
        <option value="female">Female</option>
        <option value="male">Male</option>
        <option value="other">Other</option>
      </select>

      <!-- Nova lozinka -->
      <input
          type="password"
          v-model="newPassword"
          placeholder="New Password:"
          class="rounded-input"
      />
      <input
          type="password"
          v-model="confirmPassword"
          placeholder="Confirm New Password:"
          class="rounded-input"
      />
      <p v-if="passwordError" class="error-message">{{ passwordError }}</p>

      <!-- Profilna slika -->
      <label>Profile Picture:</label>
      <input type="file" accept="image/*" @change="handleImageUpload" />
      <img v-if="previewImage" :src="previewImage" alt="Preview" class="preview-img" />

      <button type="submit" class="rounded-button">Save Changes</button>
    </form>

    <p v-else>Loading...</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import User from '@/models/User'

const user = ref(null)
const newPassword = ref('')
const confirmPassword = ref('')
const passwordError = ref('')
const previewImage = ref('')

onMounted(() => {
  const userStr = localStorage.getItem('loggedUser')
  if (userStr) {
    user.value = new User(JSON.parse(userStr))
    previewImage.value = user.value.profilePicturePath
  }
})

function handleImageUpload(event) {
  const file = event.target.files[0]
  if (file) {
    const reader = new FileReader()
    reader.onload = () => {
      previewImage.value = reader.result
      user.value.profilePicturePath = reader.result
    }
    reader.readAsDataURL(file)
  }
}

async function saveChanges() {
  passwordError.value = ''

  if (newPassword.value || confirmPassword.value) {
    if (newPassword.value !== confirmPassword.value) {
      passwordError.value = 'Passwords do not match.'
      return
    }
    user.value.password = newPassword.value
  }

  try {
    const response = await fetch(`http://localhost:8080/api/users/${user.value.id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(user.value)
    })

    if (!response.ok) {
      throw new Error('Failed to update user')
    }

    const updatedUser = await response.json()
    localStorage.setItem('loggedUser', JSON.stringify(updatedUser))
    alert('Changes saved successfully!')
  } catch (error) {
    console.error('Update failed:', error)
    alert('Something went wrong during update.')
  }
}


</script>

<style scoped>
.edit-profile-container {
  text-align: center;
  padding: 20px;
  max-width: 400px;
  margin: 0 auto;
  background-color: #f4f4f4;
  border-radius: 15px;
  box-shadow: 0 0 10px rgba(44, 62, 80, 0.1);
  color: #2c3e50;
}

.edit-profile-form {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.rounded-input {
  border: 1px solid #ccc;
  border-radius: 10px;
  padding: 8px;
  margin: 8px 0;
  width: 100%;
  box-sizing: border-box;
  color: #2c3e50;
  background-color: white;
}

select {
  border: 1px solid #ccc;
  color: #2c3e50;
  background-color: white;
  border-radius: 10px;
  padding: 8px;
  margin: 8px 0;
  width: 100%;
}

button {
  background-color: #2c3e50;
  color: #fff;
  border: none;
  border-radius: 10px;
  padding: 10px;
  cursor: pointer;
  width: 60%;
  margin: 20px auto 0;
  transition: background-color 0.3s ease;
}

button:hover {
  background-color: #f8afb4;
  color: #2c3e50;
}

.form-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin: 10px 0;
  width: 100%;
}

.form-row label {
  flex: 1;
  text-align: left;
}

.form-row input[type='checkbox'] {
  margin-left: 10px;
}

.error-message {
  color: red;
  font-size: 14px;
}

.preview-img {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  margin-top: 10px;
  object-fit: cover;
  border: 2px solid #2c3e50;
}
</style>
