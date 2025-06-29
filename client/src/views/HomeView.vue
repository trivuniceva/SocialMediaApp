<template>
  <div class="home">
    <h1 class="main-title"> SOCIAL MEDIA </h1>
    <BackgroundVideo />
  </div>
</template>

<script>

import BackgroundVideo from "@/views/BackgroundVideo.vue";

export default {
  name: 'HomeView',
  components: {
    BackgroundVideo
  }
}
</script>

<style>

.main-title{
  color: white;
  font-size: 178px;
  font-style: italic;
  font-family: "Galvji";
  margin: 12% auto;


}
</style>