<template>
  <div v-if="user" class="profile">
    <div class="upper-section">
      <img class="profile-pic" v-if="user.profilePicturePath" :src="user.profilePicturePath" alt="Profile Picture">
      <div class="profile-info">
        <h3>
          <span>{{ user.username }}</span>
          <router-link to="#" @click.prevent="togglePopup">
            <strong class="followers-link" style="margin-left: 24px">{{ user.followers?.length || 0 }} followers</strong>
          </router-link>
        </h3>
        <p>{{ user.firstName }} {{ user.lastName }}</p>
        <p>{{ user.dateOfBirth }}</p>
      </div>
    </div>

    <div class="line-separation"></div>

    <div class="lower-section">
      <div class="post" v-for="(post, index) in user.posts || []" :key="index">
        <router-link to="#" @click.prevent="togglePopupPost(post)">
          <img :src="post.picturePath" alt="Post Image" />
        </router-link>
      </div>
    </div>

    <div v-if="isPopupPostOpen" class="popupPost" @click.self="togglePopupPost(null)">
      <div class="popup-window-post">
        <div class="pictureSection">
          <img :src="currentImage" alt="Post Image">
        </div>
        <div class="rightSide">
          <div class="UserInfoSection">
            <button @click="togglePopupPost(null)">
              <img style="width: 16px;" src="img/icons/closeIcon.png">
            </button>
            <p>{{ user.firstName }}</p>
          </div>
          <div class="CommentSection">
            <p>{{ currentPost?.title }}</p>
            <div v-for="(comment, index) in currentPost?.commentsIdList || []" :key="index">
              <p>{{ comment }}</p>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div v-if="isPopupOpen" class="popup-overlay" @click.self="togglePopup">
      <div class="popup-window">
        <button class="closeBtn" @click="togglePopup">
          <img style="width: 16px" src="img/icons/closeIcon.png">
        </button>
        <h3><strong>Followers</strong></h3>
        <div class="follower-list-container">
          <ul>
            <li v-for="(follower, index) in user.followers || []" :key="index">
              <button>Remove</button>
            </li>
          </ul>
        </div>
      </div>
    </div>
  </div>

  <div v-else>
    <p>Učitavanje profila...</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const user = ref(null)
const isPopupOpen = ref(false)
const isPopupPostOpen = ref(false)
const currentImage = ref('')
const currentPost = ref(null)

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
    user.value = JSON.parse(userStr)
  }
})
</script>

<style scoped>
.popup-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.popup-window {
  background-color: #f4f4f4;
  border-radius: 15px;
  padding: 20px;
  width: 350px;
  height: 400px;
  overflow-y: auto;
  z-index: 1;
  text-decoration: none;
  color: #2c3e50;
}

.closeBtn {
  border: 1px solid #2c3e50;
  background-color: #f4f4f4;
  border-radius: 10px;
  margin-left: 92%;
}

.popupPost {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.popup-window-post {
  backdrop-filter: blur(2px);
  background-color: rgba(255, 255, 255, 0.95);
  border-radius: 15px;
  padding: 20px;
  width: 90%;
  height: 80%;
  overflow-y: auto;
  z-index: 1;
  text-decoration: none;
  position: fixed;
  display: flex;
  color: #2c3e50;
}

.popup-window-post button {
  border: 1px solid #2c3e50;
  background-color: #f4f4f4;
  border-radius: 10px;
  margin-left: 92%;
}

.pictureSection {
  flex: 2;
}

.pictureSection img {
  height: 100%;
  border-radius: 25px;
}

.rightSide {
  flex: 1;
}

.UserInfoSection {
  height: 10%;
}

.UserInfoSection p {
  text-align: left;
  padding-left: 22px;
  color: #2c3e50;
}

.CommentSection {
  height: 90%;
}

.CommentSection p {
  color: #2c3e50;
}

.profile {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  text-align: center;
  height: 300px;
  width: 75%;
  margin: 0 auto;
  background-color: #f4f4f4;
  color: #2c3e50;
  padding: 2rem;
  border-radius: 10px;
}

.upper-section {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 20px;
  width: 100%;
  height: 100%;
}

.profile-pic {
  width: 150px;
  height: 150px;
  border-radius: 50%;
  object-fit: cover;
  margin-right: 60px;
  margin-left: 2%;
}

.profile-info {
  text-align: left;
  width: 35%;
  margin-left: 15px;
  color: #2c3e50;
}

.line-separation {
  width: 85%;
  height: 1px;
  background-color: #ccc;
  margin: 0 auto;
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

.followers-link {
  color: #2c3e50;
  text-decoration: none;
}

.follower-list-container button {
  background-color: #f8afc0;
  width: 80px;
  height: 40%;
  border-radius: 5px;
  margin-left: 15%;
  color: #2c3e50;
}
</style>
