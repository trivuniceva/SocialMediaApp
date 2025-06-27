<template>
  <div class="modal-overlay" @click.self="$emit('close')">
    <div class="modal-content">
      <h2>{{ user.firstName }} {{ user.lastName }}</h2>
      <img :src="user.profilePicture || defaultImage" alt="Profile Picture" class="profile-pic" />
      <p><strong>Date of Birth:</strong> {{ user.dateOfBirth || 'Unknown' }}</p>

      <div v-if="currentUser">
        <p v-if="canSendMessage">✅ You can send a message to this user.</p>
        <p v-if="canSeeMutualFriends">👥 Mutual friends will be shown here...</p>
        <button v-if="canSendMessage" @click="sendMessage">Send Message</button>
      </div>

      <div v-else>
        <p>🔒 You are not logged in. Only public information is visible.</p>
      </div>

      <button class="close-btn" @click="$emit('close')">Close</button>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
const props = defineProps({
  user: Object,
  currentUser: Object
})

const defaultImage = '/default-avatar.png'

// Pretpostavljamo da user.role i currentUser.role postoje
const canSendMessage = computed(() => {
  return props.currentUser && (props.currentUser.role === 'User' || props.currentUser.role === 'Administrator')
})

const canSeeMutualFriends = computed(() => {
  return props.currentUser?.role === 'User'
})

const sendMessage = () => {
  alert(`Sending message to ${props.user.firstName}...`)
}
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0; left: 0; right: 0; bottom: 0;
  background: rgba(0,0,0,0.5);
  display: flex; align-items: center; justify-content: center;
  z-index: 9999;
}
.modal-content {
  background: white;
  padding: 20px;
  border-radius: 10px;
  max-width: 400px;
  text-align: center;
}
.profile-pic {
  width: 150px;
  height: 150px;
  border-radius: 50%;
  object-fit: cover;
  margin-bottom: 10px;
}
.close-btn {
  background: #2c3e50;
  color: white;
  border: none;
  padding: 10px;
  border-radius: 8px;
  margin-top: 15px;
  cursor: pointer;
}
.close-btn:hover {
  background: #F8AFB4;
  color: #2c3e50;
}
</style>
