<template>
  <div class="user-posts">
    <div v-if="postsWithImages.length > 0" class="post-list">
      <div
          v-for="post in postsWithImages"
          :key="post.id"
          class="post-card"
          @click="openPostPopup(post)"
      >
        <img
            v-if="isValidImagePath(post.imagePath)"
            :src="post.imagePath"
            class="post-image"
        />

        <div class="post-content">
          <p class="post-text">{{ post.text }}</p>
          <p class="post-date">{{ formatDate(post.creationDate) }}</p>
        </div>
      </div>
    </div>
    <div v-else>
      <p>No posts to display.</p>
    </div>

    <div
        v-if="isPopupOpen"
        class="post-popup-overlay"
        @click.self="closePopup"
    >
      <div class="post-popup-content">
        <button class="close-btn" @click="closePopup">X</button>
        <img
            v-if="isValidImagePath(currentPost?.imagePath)"
            :src="currentPost.imagePath"
            :alt="currentPost.text"
        />
        <p class="popup-text">{{ currentPost.text }}</p>
        <p class="popup-date">
          Posted: {{ formatDate(currentPost.creationDate) }}
        </p>
        <p class="popup-comments">
          Comments: {{ currentPost.commentIds?.length || 0 }}
        </p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, defineProps, computed } from 'vue'
import axios from 'axios'

const props = defineProps({
  userId: String,
})

const posts = ref([])
const isPopupOpen = ref(false)
const currentPost = ref(null)

const fetchPosts = async () => {
  if (!props.userId) return
  try {
    const response = await axios.get(`http://localhost:8080/api/posts/user/${props.userId}`)
    posts.value = response.data
  } catch (err) {
    console.error('Greška prilikom dohvatanja postova:', err)
  }
}

watch(() => props.userId, fetchPosts, { immediate: true })

const openPostPopup = (post) => {
  currentPost.value = post
  isPopupOpen.value = true
}

const closePopup = () => {
  isPopupOpen.value = false
  currentPost.value = null
}

const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleDateString('en-GB', {
    year: 'numeric', month: 'long', day: 'numeric',
    hour: '2-digit', minute: '2-digit',
  })
}

// Nova funkcija za proveru validnosti putanje slike
const isValidImagePath = (path) => {
  return path && path.trim() !== '' && !path.endsWith('null') && !path.includes('undefined');
}

const postsWithImages = computed(() =>
    // Filtriramo postove u computed propertiju samo za prikaz,
    // ali za uslov u template-u koristimo isValidImagePath
    posts.value
)
</script>

<style scoped>
/* Vaš postojeći CSS kod */
.user-posts {
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 24px;
  padding: 20px;
  box-sizing: border-box;
}

.post-list {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.post-card {
  background-color: #ffffff;
  border-radius: 12px;
  padding: 16px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  transition: transform 0.2s ease;
}

.post-card:hover {
  transform: scale(1.01);
}

.post-image {
  width: 100%;
  max-height: 400px;
  object-fit: cover;
  border-radius: 10px;
  margin-bottom: 10px;
}

.post-content {
  text-align: left;
}

.post-text {
  font-size: 1.1rem;
  color: #2c3e50;
  margin-bottom: 8px;
}

.post-date {
  font-size: 0.85rem;
  color: #777;
}

.post-popup-overlay {
  position: fixed;
  top: 0; left: 0;
  width: 100%; height: 100%;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  justify-content: center;
  align-items: center;
}

.post-popup-content {
  background-color: #fff;
  padding: 20px;
  border-radius: 15px;
  width: 60%;
  max-height: 80%;
  overflow-y: auto;
  text-align: center;
  position: relative;
}

.post-popup-content img {
  max-width: 100%;
  border-radius: 10px;
  margin-bottom: 15px;
}

.popup-text {
  font-size: 1.2rem;
  margin-bottom: 5px;
}

.popup-date {
  color: #777;
  font-size: 0.9rem;
}

.popup-comments {
  color: #2c3e50;
  margin-top: 10px;
  font-size: 0.95rem;
}

.close-btn {
  position: absolute;
  top: 10px; right: 10px;
  background-color: #f86b86;
  border: none;
  color: white;
  border-radius: 50%;
  width: 30px; height: 30px;
  font-size: 1rem;
  cursor: pointer;
}
</style>