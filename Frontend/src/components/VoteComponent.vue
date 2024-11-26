<script setup lang="ts">
import { defaultFetch } from '@/utils/defaultFetch'
import { ref, watch } from 'vue'
interface Vote {
  pollId: string
  voteOptionId: number
}

interface Poll {
  id: number
  creatorUserID: string
  question: string
  publishedAt: string
  validUntil: string
  options: VoteOption[]
  isPublic: boolean
}

interface VoteOption {
  id: number
  caption: string
  presentationOrder: number
}

const votes = ref<Vote[]>([])
const polls = ref<Poll[]>([])
const error = ref<string | null>(null)
const voteQuestionsAnswers = ref<Record<string, string>>({})
const loading = ref(true)

async function fetchVotes() {
  try {
    const response = await defaultFetch('/votes', 'GET')
    votes.value = await response
  } catch (error) {
    console.error('Error:', error)
    error.value = 'An error occurred. Please try again.'
  } finally {
    loading.value = false
  }
}

async function fetchPolls() {
  try {
    const response = await defaultFetch('/polls', 'GET')
    polls.value = await response
  } catch (error) {
    console.error('Error:', error)
    error.value = 'An error occurred. Please try again.'
  }
}
const updateVoteQuestionsAnswers = () => {
  const result: Record<string, string> = {}

  votes.value.forEach(vote => {
    // Find the poll corresponding to the vote
    const poll = polls.value.find(p => String(p.id) === vote.pollId)
    if (!poll) {
      result['Unkown'] = 'Unknown Poll'
      return
    }
    // Find the vote option corresponding to the vote
    const voteOption = poll.options.find(
      option => option.id === vote.voteOptionId,
    )
    if (!voteOption) {
      result['Unkown'] = 'Unknown VoteOption'
      return
    }
    // If there is a poll and voteOption make a result
    result[poll.question] = `${voteOption.caption}`
  })

  voteQuestionsAnswers.value = result
}

// Watch for changes in `votes` and `polls`
watch([votes, polls], updateVoteQuestionsAnswers, { immediate: true })

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
          <li v-for="(answer, pollId) in voteQuestionsAnswers" :key="pollId">
            Poll: {{ pollId }} | Answer: {{ answer }}
          </li>
          <p v-if="error">{{ error }}</p>
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
