import { createApp } from 'vue'
import App from './App.vue'
import './registerServiceWorker'
import router from './router'
import store from './store'

const app = createApp(App)

const userStr = localStorage.getItem('loggedUser')
if (userStr) {
    store.commit('setLoggedUser', JSON.parse(userStr))
}

app.use(store).use(router).mount('#app')
