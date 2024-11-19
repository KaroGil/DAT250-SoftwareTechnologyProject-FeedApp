<script setup lang="ts">
// Get users
import { ref } from 'vue'
import { defaultFetch } from '@/utils/defaultFetch'
import { deleteUserToken } from '@/utils/sessionStorageUtil'

interface User {
  id: number
  name: string
  email: string
}

const users = ref<User[]>([])
const loading = ref(true)

// Fetch all users
async function fetchUsers() {
  try {
    const response = await defaultFetch('/users', 'GET')
    users.value = await response
    console.log('users: ', users)
  } catch (error) {
    console.error('Error:', error)
    alert('An error occurred. Please try again.')
  } finally {
    loading.value = false
  }
}

// Call the fetchUsers function when the component is mounted
fetchUsers()
</script>

<template>
  <main>
    <div>
      <!-- Poll creation form -->
      <div class="modal">
        <h2>All users</h2>
        <p v-if="loading">Loading users...</p>
        <ul v-else v-for="user in users" :key="user.id">
          <li>
            {{ user.name }} ({{ user.email }})
          </li>
        </ul>
      </div>
    </div>
  </main>
</template>

<style scoped>
.modal {
  background-color: white;
  padding: 20px;
  border: 1px solid #ccc;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}
</style>
