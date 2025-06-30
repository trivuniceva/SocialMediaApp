<template>
  <div class="add-image-container">
    <h2>Add a New Photo</h2>
    <form @submit.prevent="addImage">
      <div class="form-group">
        <label for="image-upload">Choose Photo:</label>
        <input type="file" id="image-upload" @change="onFileChange" required>
      </div>
      <div class="form-group">
        <label for="image-text">Description:</label>
        <textarea id="image-text" v-model="imageText" rows="4" placeholder="Write a description for your photo..." required></textarea>
      </div>
      <button type="submit" class="submit-button">Upload</button>
      <p v-if="successMessage" class="success-message">{{ successMessage }}</p>
      <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
    </form>
  </div>
</template>

<script>
import axios from 'axios';
import { mapState } from 'vuex';

export default {
  name: 'AddImage',
  data() {
    return {
      selectedFile: null,
      imageText: '',
      successMessage: '',
      errorMessage: ''
    };
  },
  computed: {
    ...mapState(['loggedUser'])
  },
  methods: {
    onFileChange(event) {
      this.selectedFile = event.target.files[0];
    },
    async addImage() {
      this.successMessage = '';
      this.errorMessage = '';

      if (!this.selectedFile) {
        this.errorMessage = 'Please select a file to upload.';
        return;
      }

      if (!this.loggedUser || !this.loggedUser.id) {
        this.errorMessage = 'You must be logged in to add a photo.';
        return;
      }

      // In a real app, you would upload the file to a server and get a path back.
      // For this file-based backend, we'll simulate a path.
      // NOTE: This will NOT actually save the file on the backend.
      // It will just save the path in the JSON file.
      const imagePath = `/images/user_uploads/${this.selectedFile.name}`;

      const newImage = {
        userId: this.loggedUser.id,
        path: imagePath,
        text: this.imageText,
      };

      try {
        const response = await axios.post('http://localhost:8080/api/images/add', newImage);
        console.log('Image added successfully:', response.data);
        this.successMessage = 'Photo uploaded successfully!';

        // Optionally, clear the form
        this.imageText = '';
        this.selectedFile = null;
        document.getElementById('image-upload').value = null; // Clear file input

      } catch (error) {
        console.error('Error adding image:', error);
        this.errorMessage = 'Failed to upload photo. Please try again.';
      }
    }
  }
};
</script>

<style scoped>
.add-image-container {
  max-width: 600px;
  margin: 40px auto;
  padding: 20px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

h2 {
  color: #333;
  text-align: center;
  margin-bottom: 20px;
}

.form-group {
  margin-bottom: 20px;
}

label {
  display: block;
  font-weight: bold;
  margin-bottom: 8px;
  color: #555;
}

input[type="file"],
textarea {
  width: 100%;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 6px;
  box-sizing: border-box;
  font-size: 16px;
  background-color: #f9f9f9;
}

textarea {
  resize: vertical;
}

.submit-button {
  width: 100%;
  padding: 15px;
  background-color: #2c3e50;
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 18px;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.submit-button:hover {
  background-color: #F8AFB4;
  color: #2c3e50;
}

.success-message {
  color: #28a745;
  text-align: center;
  margin-top: 15px;
  font-weight: bold;
}

.error-message {
  color: #dc3545;
  text-align: center;
  margin-top: 15px;
  font-weight: bold;
}
</style>