<script setup lang="ts">
// Get users
import { ref } from 'vue'
import { defaultFetch } from '@/utils/defaultFetch'
import { getUserToken } from '@/utils/sessionStorageUtil'

interface Poll {
  id: string // UUID generated for the poll
  creatorUserID: string // UUID of the user who created the poll
  question: string // The poll's question
  publishedAt: string // The time when the poll was published (ISO 8601 format)
  validUntil: string // The expiration date and time for the poll (ISO 8601 format)
  options: VoteOption[] // An array of vote options for the poll
  isPublic: boolean // Indicates whether the poll is public
}

interface VoteOption {
  text: string // The text for the vote option
  // Add any additional fields for VoteOption if necessary
}

const polls = ref<Poll[]>([])
const loading = ref(true)

// Fetch all users
async function fetchPolls() {
  try {
    const token: string | undefined = getUserToken()
    const response = await defaultFetch('/polls', 'GET', token)
    polls.value = await response
  } catch (error) {
    console.error('Error:', error)
  } finally {
    loading.value = false
  }
}

// Call the fetchUsers function when the component is mounted
fetchPolls()
</script>

<template>
  <main>
    <div>
      <!-- Poll creation form -->
      <div class="modal">
        <h2>All Polls</h2>
        <p v-if="loading">Loading polls...</p>
        <ul v-else v-for="poll in polls" :key="poll.id">
          <li>
            {{ poll.question }}
          </li>
          <div v-for="option in poll.options" :key="option.text">
            <button>
              {{ option.text }}
            </button>
          </div>
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
