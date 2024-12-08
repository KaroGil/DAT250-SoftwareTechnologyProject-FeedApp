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

interface User {
  id: number
  name: string
  email: string
}

interface VoteOption {
  id: number
  caption: string
  presentationOrder: number
}

const polls = ref<Poll[]>([])
const loading = ref(true)
const pollOwners = ref<Record<string, User | null>>({})

// Fetch all polls
async function fetchPolls() {
  try {
    const token: string | undefined = getUserToken()
    const response = await defaultFetch('/polls', 'GET', token)
    polls.value = await response

    await fetchPollOwners()
  } catch (error) {
    console.error('Error:', error)
  } finally {
    loading.value = false
  }
}

async function vote(pollid: string, optionid: number) {
  const body = {
    voteOptionId: optionid,
    pollId: pollid,
  }

  try {
    await defaultFetch('/votes', 'POST', getUserToken(), body)
  } catch (error) {
    console.error('Error:', error)
  }
}

// Fetch the poll owners
async function fetchPollOwners() {
  try {
    const promises = polls.value.map(async poll => {
      if (!pollOwners.value[poll.creatorUserID]) {
        const url = `/users/${poll.creatorUserID}`
        const response = await defaultFetch(url, 'GET', getUserToken())
        pollOwners.value[poll.creatorUserID] = await response
      }
    })

    // Wait for all requests to complete
    await Promise.all(promises)
  } catch (error) {
    console.error('Error fetching poll owners:', error)
  }
}

// Call the fetchPolls function when the component is mounted
fetchPolls()
</script>

<template>
  <main>
    <div>
      <div class="modal">
        <h2>All Polls</h2>
        <p v-if="loading">Loading polls...</p>
        <ul v-else v-for="poll in polls" :key="poll.id">
          <li>
            {{ poll.question }} created by:
            {{ pollOwners[poll.creatorUserID]?.email || 'Unknown' }}
          </li>
          <div v-for="option in poll.options" :key="option.id">
            <button @click="vote(poll.id, option.id)">
              {{ option.caption }}
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
