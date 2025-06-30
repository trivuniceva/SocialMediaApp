<template>
  <div v-if="user" class="profile-container">
    <div class="profile-header">
      <img class="profile-pic" v-if="user.profilePicturePath" :src="user.profilePicturePath" alt="Profile Picture">
      <div class="profile-info">
        <div class="profile-username">
          <h3>{{ user.username }}</h3>
          <router-link to="#" @click.prevent="togglePopup" class="followers-link">
            <strong>{{ user.friendListIds?.length || 0 }} followers</strong>
          </router-link>
        </div>
        <p class="full-name">{{ user.firstName }} {{ user.lastName }}</p>
        <p class="dob">{{ user.dateOfBirth }}</p>
      </div>
    </div>

    <div class="line-separator"></div>

    <div class="profile-content">
      <div class="button-group">
        <button
            class="tab-button"
            :class="{ active: selectedSection === 'pictures' }"
            @click="selectedSection = 'pictures'"
        >Pictures</button>

        <button
            class="tab-button"
            :class="{ active: selectedSection === 'posts' }"
            @click="selectedSection = 'posts'"
        >Posts</button>
      </div>

      <UserPictures v-if="selectedSection === 'pictures'" :imageIds="user.imageIds" />
      <UserPosts v-else :userId="user.id" />
    </div>

    <div v-if="isPopupPostOpen" class="popup-overlay" @click.self="togglePopupPost(null)">
      <div class="popup-window-post">
        <div class="picture-section">
          <img :src="currentImage" alt="Post Image">
        </div>
        <div class="right-side">
          <div class="user-info-section">
            <button @click="togglePopupPost(null)" class="close-post-button">
              <img src="/img/icons/closeIcon.png" alt="Close">
            </button>
            <p>{{ user.firstName }}</p>
          </div>
          <div class="comment-section">
            <h4>{{ currentPost?.title }}</h4>
            <div v-for="(comment, index) in currentPost?.commentsIdList || []" :key="index" class="comment-item">
              <p>{{ comment }}</p>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div v-if="isPopupOpen" class="popup-overlay" @click.self="togglePopup">
      <div class="popup-window">
        <button class="close-popup-button" @click="togglePopup">
          <p>X</p>
        </button>
        <h3><strong>Followers</strong></h3>
        <div class="follower-list-container">
          <ul>
            <li v-for="(follower, index) in user.friendListIds || []" :key="index" class="follower-item">
              <span>{{ follower }}</span>
              <button class="remove-follower-button">Remove</button>
            </li>
          </ul>
        </div>
      </div>
    </div>
  </div>

  <div v-else class="loading-message">
    <p>Loading profile...</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import User from '@/models/User'
import UserPictures from './UserPictures.vue'
import UserPosts from './UserPosts.vue'

const user = ref(null)
const isPopupOpen = ref(false)
const isPopupPostOpen = ref(false)
const currentImage = ref('')
const currentPost = ref(null)
const selectedSection = ref('pictures')

function togglePopup() {
  isPopupOpen.value = !isPopupOpen.value
}

function togglePopupPost(post) {
  isPopupPostOpen.value = !isPopupPostOpen.value
  currentImage.value = post?.picturePath || ''
  currentPost.value = post || null
}

onMounted(() => {
  const userStr = localStorage.getItem('loggedUser')
  if (userStr) {
    user.value = new User(JSON.parse(userStr))
  }
})
</script>

<style scoped>
.profile-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
  text-align: center;
  min-height: calc(100vh - 100px); /* Originalna visina */
  width: 75%; /* Originalna širina */
  margin: 0 auto; /* Originalna margina */
  background-color: #ffffff; /* Promenjeno iz #f4f4f4 u belu */
  color: #2c3e50;
  padding: 2rem; /* Originalni padding */
  border-radius: 10px; /* Originalni border-radius */
  box-sizing: border-box;
  box-shadow: 0 8px 25px rgba(44, 62, 80, 0.12); /* Usklađeno sa Edit Profile */
}

h2 {
  color: #2c3e50;
  margin-bottom: 25px;
  font-size: 1.8em; /* Usklađeno sa Edit Profile */
  font-weight: 600; /* Usklađeno sa Edit Profile */
}

h3 {
  color: #2c3e50;
  font-size: 1.6em; /* Nije dirano, bilo je ok */
  font-weight: 600; /* Usklađeno sa Edit Profile */
  margin-bottom: 5px;
}

.profile-header {
  display: flex;
  align-items: center;
  justify-content: center; /* Dodato za centriranje unutra */
  margin-bottom: 20px;
  width: 100%;
  flex-wrap: wrap;
  gap: 60px; /* Dodato za razmak, ako nije bilo */
}

.profile-pic {
  width: 150px; /* Originalna veličina */
  height: 150px; /* Originalna veličina */
  border-radius: 50%;
  object-fit: cover;
  margin-right: 0; /* Uklonjena specifična margina */
  margin-left: 0; /* Uklonjena specifična margina */
  border: 3px solid #f86b86; /* Usklađeno sa Edit Profile */
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1); /* Usklađeno sa Edit Profile */
}

.profile-info {
  text-align: left;
  width: 35%; /* Originalna širina */
  margin-left: 0; /* Uklonjena specifična margina */
  color: #2c3e50;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.profile-username {
  display: flex;
  align-items: baseline;
  gap: 24px; /* Razmak između username-a i followers */
  margin-bottom: 10px;
}

.followers-link {
  color: #f86b86; /* Usklađeno sa temom */
  text-decoration: none;
  font-weight: 600; /* Usklađeno sa temom */
}

.followers-link:hover {
  color: #e05a73; /* Hover boja kao za dugme */
  text-decoration: underline;
}

.full-name,
.dob {
  color: #555; /* Tamniji tekst za bolju čitljivost */
  font-size: 1.1em; /* Malo veći font */
  margin-bottom: 5px;
}

.line-separator {
  width: 77%; /* Originalna širina */
  height: 1px;
  background-color: #ddd; /* Svetlija linija */
  margin: 2rem auto 0; /* Originalna margina */
}

.profile-content {
  display: flex;
  flex-direction: column;
  flex-grow: 1;
  width: 100%;
  overflow: hidden;
}

.button-group {
  display: flex;
  justify-content: center;
  margin-bottom: 1rem;
  margin-top: 1rem;
  gap: 16px;
  flex-shrink: 0;
}

.tab-button {
  padding: 10px 20px;
  font-size: 16px;
  background-color: #ecf0f1; /* Usklađeno sa Edit Profile */
  color: #2c3e50;
  border: none;
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.tab-button:hover {
  background-color: #d6e4ec; /* Usklađeno sa Edit Profile */
}

.tab-button.active {
  background-color: #f86b86; /* Usklađeno sa Edit Profile */
  color: white;
  box-shadow: 0 4px 10px rgba(248, 107, 134, 0.3); /* Usklađeno sa Edit Profile */
}

/* --- Pop-up stilovi (samo boje i senke, bez dimenzija) --- */
.popup-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.6); /* Tamnija pozadina kao na Edit Profile */
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.popup-window {
  background-color: #ffffff; /* Bela pozadina */
  border-radius: 15px; /* Originalni */
  padding: 20px; /* Originalni */
  width: 350px; /* Originalni */
  height: 400px; /* Originalni */
  overflow-y: auto;
  z-index: 1;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2); /* Jača senka */
  color: #2c3e50;
}

.close-popup-button {
  border: none; /* Uklonjen border */
  background: none; /* Uklonjena pozadina */
  border-radius: 50%; /* Okruglo dugme */
  font-size: 1.5em; /* Veći X */
  color: #555; /* Tamniji X */
  cursor: pointer;
  padding: 5px 10px;
  position: absolute; /* Pozicioniranje unutar prozora */
  top: 10px;
  right: 10px;
  transition: all 0.2s ease;
}

.close-popup-button:hover {
  background-color: #f0f0f0;
  color: #e74c3c;
}

.popup-window h3 {
  margin-top: 0;
  margin-bottom: 25px; /* Povećan razmak ispod h3 */
  border-bottom: 2px solid #f86b86; /* Linija ispod h3 */
  padding-bottom: 10px;
  display: inline-block;
}

.popup-window-post {
  backdrop-filter: blur(5px); /* Jači blur efekat */
  background-color: rgba(255, 255, 255, 0.98); /* Skoro bela pozadina */
  border-radius: 15px; /* Originalni */
  padding: 20px; /* Originalni */
  width: 90%; /* Originalni */
  height: 80%; /* Originalni */
  overflow-y: auto;
  z-index: 1;
  display: flex;
  color: #2c3e50;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2); /* Jača senka */
}

.close-post-button {
  border: none; /* Uklonjen border */
  background: none; /* Uklonjena pozadina */
  border-radius: 50%; /* Okruglo dugme */
  cursor: pointer;
  padding: 5px;
  margin-left: auto; /* Gurni dugme desno */
  transition: background-color 0.2s ease;
}

.close-post-button img {
  width: 20px; /* Veća ikonica */
  height: 20px;
}

.close-post-button:hover {
  background-color: #f0f0f0;
}

.picture-section {
  flex: 2;
  display: flex;
  justify-content: center;
  align-items: center;
}

.picture-section img {
  height: 100%;
  max-width: 100%; /* Da se ne prelije */
  object-fit: contain; /* Da se cela slika vidi */
  border-radius: 15px;
  box-shadow: 0 5px 20px rgba(0, 0, 0, 0.1);
}

.right-side {
  flex: 1;
  display: flex;
  flex-direction: column;
  padding-left: 20px;
  border-left: 1px solid #eee; /* Suptilna linija */
}

.user-info-section {
  height: auto; /* Prilagođava se sadržaju */
  display: flex;
  align-items: center;
  padding-bottom: 15px;
  border-bottom: 1px solid #eee;
  margin-bottom: 15px;
}

.user-info-section p {
  text-align: left;
  padding-left: 10px; /* Smanjeno na 10px */
  color: #2c3e50;
  margin: 0; /* Uklonjene margine */
  font-weight: 600; /* Deblji font */
}

.comment-section {
  height: 90%; /* Originalna visina */
  flex-grow: 1; /* Dozvoli rast */
  overflow-y: auto; /* Skrolbar za komentare */
  padding-right: 10px; /* Razmak zbog skrolbara */
}

.comment-section h4 { /* Dodat stil za h4 unutar komentara */
  font-size: 1.3em;
  margin-top: 0;
  margin-bottom: 15px;
  color: #2c3e50;
  text-align: left;
}

.comment-item { /* Novi stil za pojedinačni komentar */
  background-color: #f9f9f9; /* Svetlija pozadina za komentare */
  border-radius: 8px;
  padding: 10px 15px;
  margin-bottom: 10px;
  text-align: left;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
}

.comment-item p {
  margin: 0;
  font-size: 0.95em;
  line-height: 1.4;
  color: #444;
}

.loading-message {
  text-align: center;
  padding: 50px;
  color: #555;
  font-size: 1.2em;
}

.follower-list-container {
  max-height: 300px; /* Dovoljno visine za scroll */
  overflow-y: auto;
}

.follower-list-container ul {
  list-style: none;
  padding: 0;
}

.follower-item { /* Novi stil za pojedinačnog followera */
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #f5f8fb; /* Svetlija pozadina */
  padding: 12px 18px;
  margin-bottom: 10px;
  border-radius: 10px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.05);
  font-size: 1.05em;
}

.remove-follower-button {
  background-color: #f8afb4; /* Svetlija pink */
  width: 80px; /* Originalna širina */
  height: auto; /* Prilagođava se sadržaju */
  border-radius: 5px; /* Originalni */
  margin-left: 15%; /* Originalni */
  color: #2c3e50;
  border: none; /* Dodato da bude bez bordera */
  padding: 8px 15px;
  cursor: pointer;
  transition: background-color 0.2s ease, transform 0.2s ease;
}

.remove-follower-button:hover {
  background-color: #f6949a;
  transform: translateY(-1px);
}


/* Skrolbar stilizacija (za Webkit browsere) */
.follower-list-container::-webkit-scrollbar,
.comment-section::-webkit-scrollbar {
  width: 8px;
}

.follower-list-container::-webkit-scrollbar-track,
.comment-section::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 10px;
}

.follower-list-container::-webkit-scrollbar-thumb,
.comment-section::-webkit-scrollbar-thumb {
  background: #ccc;
  border-radius: 10px;
}

.follower-list-container::-webkit-scrollbar-thumb:hover,
.comment-section::-webkit-scrollbar-thumb:hover {
  background: #bbb;
}

/* Media Queries za responsivnost */
@media (max-width: 992px) {
  .profile-header {
    flex-direction: column;
    gap: 30px;
  }

  .profile-info {
    text-align: center;
    width: auto;
    max-width: none;
  }

  .profile-username {
    justify-content: center;
    gap: 15px; /* Smanjen razmak na manjim ekranima */
  }

  .popup-window-post {
    flex-direction: column;
    height: 90%;
    width: 95%;
  }

  .picture-section {
    padding-right: 0;
    padding-bottom: 20px;
  }

  .right-side {
    padding-left: 0;
    padding-top: 20px;
    border-left: none;
    border-top: 1px solid #eee;
  }
}

@media (max-width: 576px) {
  .profile-container {
    width: 95%; /* Šira na malim ekranima */
    padding: 15px;
  }

  h2 {
    font-size: 1.6em;
  }

  h3 {
    font-size: 1.3em;
  }

  .profile-pic {
    width: 100px;
    height: 100px;
  }

  .button-group {
    flex-direction: column;
    gap: 10px;
  }

  .tab-button {
    width: 90%;
    padding: 10px 15px;
  }

  .popup-window {
    width: 90%;
    padding: 15px;
  }

  .close-popup-button {
    font-size: 1.2em;
    top: 5px;
    right: 5px;
  }

  .follower-item {
    flex-direction: column;
    align-items: flex-start;
    gap: 5px;
    padding: 10px 12px;
  }

  .remove-follower-button {
    width: 100%;
    margin-left: 0;
  }

  .user-info-section p {
    font-size: 1em;
  }
}
</style>