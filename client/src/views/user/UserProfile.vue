<template>
  <div v-if="user" class="profile">
    <h2>👤 Profil korisnika</h2>
    <p><strong>ID:</strong> {{ user.id }}</p>
    <p><strong>Korisničko ime:</strong> {{ user.username }}</p>
    <p><strong>Email:</strong> {{ user.emailAddress }}</p>
    <p><strong>Ime:</strong> {{ user.firstName }}</p>
    <p><strong>Prezime:</strong> {{ user.lastName }}</p>
    <p><strong>Datum rođenja:</strong> {{ user.dateOfBirth }}</p>
    <p><strong>Pol:</strong> {{ user.gender }}</p>
    <p><strong>Uloga:</strong> {{ user.role }}</p>
    <p><strong>Privatni nalog:</strong> {{ user.privateAccount ? 'Da' : 'Ne' }}</p>
    <p><strong>Logički obrisan:</strong> {{ user.logicallyDeleted ? 'Da' : 'Ne' }}</p>
    <p><strong>Blokiran:</strong> {{ user.blocked ? 'Da' : 'Ne' }}</p>

    <div v-if="user.profilePicturePath">
      <h4>🖼️ Profilna slika:</h4>
      <img :src="user.profilePicturePath" alt="Profilna slika" width="150" />
    </div>

    <div v-if="user.friendListIds && user.friendListIds.length">
      <h4>👥 Lista prijatelja:</h4>
      <ul>
        <li v-for="id in user.friendListIds" :key="id">{{ id }}</li>
      </ul>
    </div>
  </div>

  <div v-else>
    <p>Učitavanje profila...</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const user = ref(null)

onMounted(() => {
  const userStr = localStorage.getItem('loggedUser')
  if (userStr) {
    user.value = JSON.parse(userStr)
  }
})
</script>

<style scoped>
.profile {
  max-width: 600px;
  margin: auto;
  padding: 1rem;
  background: #f4f4f4;
  border-radius: 10px;
}
img {
  border-radius: 10px;
  margin-top: 0.5rem;
}
</style>
