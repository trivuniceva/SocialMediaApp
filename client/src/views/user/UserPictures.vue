<template>
  <div class="user-pictures">
    <div v-if="images.length > 0" class="image-grid">
      <div v-for="image in images" :key="image.id" class="image-item">
        <img :src="image.path" :alt="image.text" @click="openImagePopup(image)">
      </div>
    </div>
    <div v-else>
      <p>No pictures to display.</p>
    </div>

    <div v-if="isImagePopupOpen" class="image-popup-overlay" @click.self="closeImagePopup">
      <div class="image-popup-content">
        <button class="close-popup-btn" @click="closeImagePopup">X</button>
        <img :src="currentImage.path" :alt="currentImage.text">
        <p class="image-text">{{ currentImage.text }}</p>
        <p class="image-upload-date">Uploaded: {{ formatUploadDate(currentImage.uploadDate) }}</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, defineProps } from 'vue';
import axios from 'axios';

const props = defineProps({
  imageIds: {
    type: Array,
    default: () => [],
  },
});

const images = ref([]);
const isImagePopupOpen = ref(false);
const currentImage = ref(null);

const fetchImages = async () => {
  const userStr = localStorage.getItem('loggedUser');
  if (userStr) {
    const loggedUser = JSON.parse(userStr);
    if (loggedUser && loggedUser.id) {
      try {
        const response = await axios.get(`http://localhost:8080/api/images/user/${loggedUser.id}`);
        images.value = response.data;
      } catch (error) {
        console.error('Error fetching images:', error);
        images.value = [];
      }
    } else {
      images.value = [];
    }
  } else {
    images.value = [];
  }
};

watch(() => props.imageIds, fetchImages, { immediate: true });

const openImagePopup = (image) => {
  currentImage.value = image;
  isImagePopupOpen.value = true;
};

const closeImagePopup = () => {
  isImagePopupOpen.value = false;
  currentImage.value = null;
};

const formatUploadDate = (dateString) => {
  if (!dateString) return '';
  const date = new Date(dateString);
  return date.toLocaleDateString('en-US', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  });
};
</script>

<style scoped>
.user-pictures {
  width: 80%;
  flex-grow: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  margin: 0 auto;
}

.image-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 3px;
  padding: 20px;
  justify-items: center;
  overflow-y: auto;

}

.image-item {
  width: 266px;
  height: 366px;
  overflow: hidden;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  position: relative;
}

.image-item::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0);
  transition: background-color 0.3s ease;
  pointer-events: none;
  border-radius: 8px;
}

.image-item:hover::before {
  background-color: rgba(0, 0, 0, 0.3);
}

.image-item img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.image-popup-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.7);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.image-popup-content {
  background-color: #f4f4f4;
  border-radius: 15px;
  padding: 20px;
  max-width: 80%;
  max-height: 80%;
  display: flex;
  flex-direction: column;
  align-items: center;
  position: relative;
  color: #2c3e50;
}

.image-popup-content img {
  max-width: 100%;
  max-height: 500px;
  border-radius: 10px;
  object-fit: contain;
  margin-bottom: 15px;
}

.image-popup-content .image-text {
  font-size: 1.1em;
  margin-bottom: 5px;
  text-align: center;
}

.image-popup-content .image-upload-date {
  font-size: 0.9em;
  color: #666;
  text-align: center;
}

.close-popup-btn {
  position: absolute;
  top: 10px;
  right: 10px;
  background-color: #f86b86;
  color: white;
  border: none;
  border-radius: 50%;
  width: 30px;
  height: 30px;
  font-size: 1.1em;
  cursor: pointer;
  display: flex;
  justify-content: center;
  align-items: center;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

.close-popup-btn:hover {
  background-color: #e05a73;
}
</style>