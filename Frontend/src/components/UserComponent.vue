<script setup lang="ts">
// Get users
import { defaultFetch } from '@/components/defaultFetch'

interface User {
  id: number;
  name: string;
  email: string;
}

let users: User[] = [];

// Fetch all users
async function fetchUsers() {
  try {
    const response = await defaultFetch("/users", "GET");
    users = await response;
  } catch (error) {
    console.error('Error:', error);
    alert('An error occurred. Please try again.');
  }
}

// Delete a user
async function deleteUser(id: number) {
  try {
    await defaultFetch(`/users/${id}`, "DELETE");
    users = users.filter(user => user.id !== id);
  } catch (error) {
    console.error('Error:', error);
    alert('An error occurred. Please try again.');
  }
}

// Call the fetchUsers function when the component is mounted
fetchUsers();
</script>

<template>
  <main>
    <div>
      <!-- Poll creation form -->
      <div class="modal">
        <h2>All users</h2>
        <ul v-for="user in users" :key="user.id">
          <li>
            {user.name} ({user.email})
            <button @click="deleteUser(user.id)">Delete</button>
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
