<template>
  <div class="user-search">
    <h2>User search</h2>

    <div class="search-options">
      <input v-model="searchName" placeholder="Name:" class="rounded-input" />
      <input v-model="searchLastName" placeholder="Lastname:" class="rounded-input" />

      <br><br>
      <label for="startDate">Date of birth from:</label>
      <input type="date" id="startDate" v-model="startDate" class="rounded-input" />

      <br><br>
      <label for="endDate">Date of birth to:</label>
      <input type="date" id="endDate" v-model="endDate" class="rounded-input" />

      <br><br>
      <button @click="searchUsers" class="rounded-button">Search</button>
    </div>

    <div class="search-results" v-if="filteredUsers.length">
      <h3>Search results</h3>
      <select v-model="sortOption" class="rounded-input">
        <option value="firstName">Name</option>
        <option value="lastName">Lastname</option>
        <option value="dateOfBirth">Date of Birth</option>
      </select>

      <ul>
        <li v-for="user in sortedUsers" :key="user.id">
          {{ user.firstName }} {{ user.lastName }} - {{ user.dateOfBirth }}
        </li>
      </ul>
    </div>

    <p v-else-if="users.length && !filteredUsers.length">No matching users found.</p>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'

const users = ref([])
const filteredUsers = ref([])

const searchName = ref('')
const searchLastName = ref('')
const startDate = ref('')
const endDate = ref('')
const sortOption = ref('firstName')

const fetchUsers = async () => {
  const response = await fetch('http://172.20.10.4:8080/api/users')
  if (!response.ok) {
    console.error('Ne mogu da učitam korisnike')
    return
  }
  users.value = await response.json()
  filteredUsers.value = [...users.value]
}

const searchUsers = () => {
  filteredUsers.value = users.value.filter(user => {
    const matchName = user.firstName.toLowerCase().includes(searchName.value.toLowerCase())
    const matchLastName = user.lastName.toLowerCase().includes(searchLastName.value.toLowerCase())

    const dob = new Date(user.dateOfBirth)
    const start = startDate.value ? new Date(startDate.value) : null
    const end = endDate.value ? new Date(endDate.value) : null

    const matchDate =
        (!start || dob >= start) &&
        (!end || dob <= end)

    return matchName && matchLastName && matchDate
  })
}

const sortedUsers = computed(() => {
  return [...filteredUsers.value].sort((a, b) => {
    if (sortOption.value === 'firstName') {
      return a.firstName.localeCompare(b.firstName)
    } else if (sortOption.value === 'lastName') {
      return a.lastName.localeCompare(b.lastName)
    } else if (sortOption.value === 'dateOfBirth') {
      return new Date(a.dateOfBirth) - new Date(b.dateOfBirth)
    }
    return 0
  })
})

onMounted(() => {
  fetchUsers()
})
</script>

<style scoped>
.user-search {
  text-align: center;
  padding: 20px;
}

.search-options {
  max-width: 400px;
  margin: 0 auto;
}

.rounded-input {
  border: 1px solid #ccc;
  border-radius: 10px;
  padding: 8px;
  margin: 6px;
  width: 100%;
}

.search-results {
  max-width: 600px;
  margin: 20px auto;
}

h3 {
  color: #2c3e50;
}

select {
  background-color: #2c3e50;
  color: #fff;
  border: none;
  border-radius: 5px;
  padding: 8px;
  margin-bottom: 10px;
  width: 100%;
}

ul {
  list-style: none;
  padding: 0;
}

li {
  margin-bottom: 8px;
  background-color: #ecf0f1;
  padding: 10px;
  border-radius: 5px;
}

.rounded-button {
  background-color: #2c3e50;
  color: #fff;
  border: none;
  border-radius: 10px;
  padding: 10px;
  cursor: pointer;
  width: 40%;
  font-size: 16px;
}

.rounded-button:hover {
  background-color: #F8AFB4;
  color: #2c3e50;
}
</style>
