<script setup lang="ts">
// Get users
import { ref } from 'vue'
import { defaultFetch } from '@/utils/defaultFetch'
import {getUserToken} from "@/utils/sessionStorageUtil";

interface User {
  id: number
  name: string
  email: string
}

const user = ref<User | null>(null)
const loading = ref(true)
const error = ref<string | null>(null)

// Fetch all users
async function fetchCurrentUser() {
  const token = getUserToken();

  if(!token){
    error.value = "No user is logged in."
    loading.value = false
    return
  }

  try {
    const response = await defaultFetch('/users/me', 'GET', token)
    user.value = await response
    console.log('users: ', user)
  } catch (error) {
    console.error('Error:', error)
    error.value = "An error occurred. Please try again."
  } finally {
    loading.value = false
  }
}

// Call the fetchUsers function when the component is mounted
fetchCurrentUser()
</script>

<template>
  <main>
    <div>
      <!-- Poll creation form -->
      <div class="modal">
        <h2>All users</h2>
        <p v-if="loading">Loading users...</p>
        <p v-if="error">{{ error }}</p>
        <div v-if="!loading && !error && user">
           <p><strong>Name:</strong> {{ user.name }}</p>
           <p><strong>Email:</strong> {{ user.email }}</p>
        </div>
        <p v-if="!loading && !error && !user">No user data found.</p>
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
