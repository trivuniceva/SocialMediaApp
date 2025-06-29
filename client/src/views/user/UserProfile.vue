<template>
  <div v-if="user" class="profile">
    <div class="upper-section">
      <img
          v-if="user.profilePicturePath"
          class="profile-pic"
          :src="user.profilePicturePath"
          alt="Profile Picture"
      />
      <div class="profile-info">
        <h3>
          <span>{{ user.username }}</span>
          <strong class="followers-link" style="margin-left: 24px">
            {{ user.friendListIds?.length || 0 }} followers
          </strong>
        </h3>
        <p>{{ user.firstName }} {{ user.lastName }}</p>
        <p>{{ user.dateOfBirth }}</p>
      </div>
    </div>

    <div class="line-separation"></div>

    <div class="lower-section" v-if="canViewContent">
      <div class="post" v-for="(postId, index) in user.postIds || []" :key="index">
        <img :src="'/images/posts/' + postId + '.jpg'" alt="Post Image" />
      </div>
    </div>

    <p v-else>Ovaj profil je privatan.</p>
  </div>

  <div v-else>
    <p>Učitavanje profila...</p>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useStore } from 'vuex'
import { useRoute } from 'vue-router'

const store = useStore()
const route = useRoute()
const user = ref(null)
const errorMessage = ref('')

const loggedUser = computed(() => store.state.loggedUser)

const canViewContent = computed(() => {
  if (!user.value) return false;

  // Ako profil nije privatan — svi mogu da vide
  if (!user.value.privateAccount) return true;

  // Ako je ulogovani korisnik isti kao prikazani korisnik — može da vidi
  if (loggedUser.value && loggedUser.value.id === user.value.id) return true;

  // Ako je ulogovani korisnik prijatelj prikazanog korisnika — može da vidi
  if (
      loggedUser.value &&
      user.value.friendListIds &&
      user.value.friendListIds.includes(loggedUser.value.id)
  ) {
    return true;
  }

  // U ostalim slučajevima — ne može da vidi sadržaj
  return false;
})

onMounted(async () => {
  const userId = route.params.id || store.state.loggedUser?.id
  if (!userId) {
    errorMessage.value = 'Korisnik nije pronađen'
    return
  }

  try {
    const response = await fetch(`http://localhost:8080/api/users/${userId}`)
    if (!response.ok) {
      errorMessage.value = 'Korisnik nije pronađen'
      return
    }
    user.value = await response.json()
  } catch (error) {
    errorMessage.value = 'Greška prilikom učitavanja korisnika'
  }
})
</script>

<style scoped>
/* Stilovi kao što si već koristila */
.profile {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  padding: 20px;
  max-width: 600px;
  margin: 0 auto;
}
.profile-pic {
  width: 150px;
  height: 150px;
  border-radius: 50%;
  object-fit: cover;
}
.profile-info {
  margin-top: 20px;
}
.line-separation {
  width: 100%;
  height: 1px;
  background-color: #ccc;
  margin: 20px 0;
}
.lower-section {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
}
.post {
  margin: 10px;
  width: 150px;
  height: 150px;
  overflow: hidden;
}
.post img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
</style>
