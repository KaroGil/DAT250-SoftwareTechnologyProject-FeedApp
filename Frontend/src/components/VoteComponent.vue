<script setup lang="ts">
import { defaultFetch } from '@/utils/defaultFetch'
import { ref, computed } from 'vue'

const votes = ref<User[]>([])
const polls = ref<User[]>([])
const loading = ref(true)

async function fetchVotes() {
  try {
    const response = await defaultFetch('/votes', 'GET')
    votes.value = await response
    console.log('votes: ', votes)
  } catch (error) {
    console.error('Error:', error)
    alert('An error occurred. Please try again.')
  } finally {
    loading.value = false
  }
}

async function fetchPolls() {
  try {
    const response = await defaultFetch('/polls', 'GET')
    polls.value = await response
    console.log('polls: ', votes)
  } catch (error) {
    console.error('Error:', error)
    alert('An error occurred. Please try again.')
  }
}

const mappedVotes = computed(() => {
  return votes.value.map(vote => {
    // Find the poll corresponding to the vote
    const poll = polls.value.find(p => p.id === vote.pollId)

    // Find the vote option corresponding to the vote
    const voteOption = poll?.options.find(
      option => option.id === vote.voteOptionId,
    )

    return {
      ...vote,
      pollName: poll?.question || 'Unknown Poll',
      voteOptionCaption: voteOption?.caption || 'Unknown Option',
    }
  })
})

fetchPolls()
fetchVotes()
</script>

<template>
  <main>
    <div>
      <!-- Poll creation form -->
      <div class="modal">
        <h2>Vote</h2>
        <p v-if="loading">Loading users...</p>
        <ul v-else>
          <li v-for="vote in mappedVotes" :key="vote.id">
            Poll: {{ vote.pollName }} | Answer: {{ vote.voteOptionCaption }}
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
