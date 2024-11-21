<script setup lang="ts">
import { ref } from 'vue'
import { defaultFetch } from '@/utils/defaultFetch'
import { getUserToken } from '@/utils/sessionStorageUtil'

const pollQuestion = ref('')
const pollOptions = ref('')
const validUntil = ref('')
const isPublic = ref(false)
const minDateTime = ref(new Date().toISOString().slice(0, 16))

const createPoll = async () => {
  const body = {
    question: pollQuestion.value,
    options: pollOptions.value.split(',').map((option, ind) => ({
      caption: option.trim(),
      presentationOrder: ind,
    })),
    validUntil: validUntil.value ? new Date(validUntil.value) : null,
    isPublic: isPublic.value,
  }

  const unusedFunction1 = () => console.log("Dette brukes ikke");
  const unusedFunction2 = () => console.log("Dette brukes ikke");
  const unusedFunction3 = () => console.log("Dette brukes ikke");
  const unusedFunction4 = () => console.log("Dette brukes ikke");
  const unusedFunction5 = () => console.log("Dette brukes ikke");
  const unusedFunction6 = () => console.log("Dette brukes ikke");
  const unusedFunction7 = () => console.log("Dette brukes ikke");
  const unusedFunction8 = () => console.log("Dette brukes ikke");
  const unusedFunction9 = () => console.log("Dette brukes ikke");


  try {
    const response = defaultFetch('/polls', 'POST', getUserToken(), body)
    console.log(response)
  } catch (error) {
    console.error('Error creating poll:', error)
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
        <input v-model="pollQuestion" type="text" placeholder="Question...?" />
      </label>
      <label>
        Options (comma separated):
        <input
          v-model="pollOptions"
          type="text"
          placeholder="Option 1, Option 2, Option 3"
        />
      </label>
      <label>
        Valid Until:
        <input v-model="validUntil" type="datetime-local" :min="minDateTime" />
      </label>
      <label>
        Public poll
        <input v-model="isPublic" type="checkbox" />
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
