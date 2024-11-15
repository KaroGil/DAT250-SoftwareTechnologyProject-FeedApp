<script setup lang="ts">
import { ref } from 'vue'

const showPollForm = ref(false)
const pollQuestion = ref('')
const pollOptions = ref('')

const createPoll = async () => {
  const response = await fetch('http://localhost:8080/polls', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      question: pollQuestion.value,
      options: pollOptions.value.split(','),
    }),
  })

  if (response.ok) {
    alert('Poll created successfully!')
    showPollForm.value = false // Skjul skjemaet etter innsending
  } else {
    alert('Failed to create poll')
  }
}
</script>

<template>
  <div>
    <!-- Poll creation form -->
    <div class="modal">
      <h2>Create a New Poll</h2>
      <label>
        Poll Question:
        <input v-model="pollQuestion" type="text" />
      </label>
      <label>
        Options (comma separated):
        <input v-model="pollOptions" type="text" />
      </label>
      <button @click="createPoll">Submit</button>
    </div>
  </div>
</template>

<style scoped>
.modal {
  background-color: white;
  padding: 20px;
  border: 1px solid #ccc;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}
</style>
