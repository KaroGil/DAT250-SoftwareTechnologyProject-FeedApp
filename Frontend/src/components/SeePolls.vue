<script setup lang="ts">
import { ref } from 'vue';
import { defaultFetch } from '@/components/defaultFetch';

// Definer grensesnitt for Poll og VoteOption
interface Poll {
  id: string;
  question: string;
  options: VoteOption[];
}

interface VoteOption {
  id: string;
  text: string;
}

const polls = ref<Poll[]>([]);
const loading = ref(true);
const openPollId = ref<string | null>(null); // ID-en til den avstemningen som er åpen (dropdown)

// Funksjon for å hente alle avstemninger
async function fetchPolls() {
  try {
    const response = await defaultFetch("/polls", "GET");
    polls.value = await response;
  } catch (error) {
    console.error('Error:', error);
    alert('An error occurred. Please try again.');
  } finally {
    loading.value = false;
  }
}

// Funksjon for å stemme på et alternativ
async function vote(pollId: string, optionId: string) {
  try {
    await defaultFetch(`/polls/${pollId}/vote`, "POST", undefined, { optionId });
    alert('Vote submitted successfully!');
  } catch (error) {
    console.error('Error:', error);
    alert('An error occurred. Please try again.');
  }
}

// Funksjon for å åpne/lukke dropdown for en bestemt avstemning
function toggleOptions(pollId: string) {
  openPollId.value = openPollId.value === pollId ? null : pollId;
}

// Henter avstemninger når komponenten monteres
fetchPolls();
</script>

<template>
  <main>
    <div class="modal">
      <h2>All Polls</h2>
      <p v-if="loading">Loading polls...</p>
      <div v-else>
        <ul v-for="poll in polls" :key="poll.id" class="poll">
          <!-- Klikk på spørsmålet for å vise/skjule alternativene -->
          <li @click="toggleOptions(poll.id)" class="poll-question">
            {{ poll.question }}
          </li>
          <div v-if="openPollId === poll.id" class="poll-options">
            <div v-for="option in poll.options" :key="option.id" class="poll-option">
              <button @click="vote(poll.id, option.id)">
                {{ option.text }}
              </button>
            </div>
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

.poll {
  font-size: 18px;
  margin-bottom: 20px;
}

.poll-question {
  font-weight: bold;
  cursor: pointer;
  color: #333;
}

.poll-options {
  margin-top: 10px;
}

.poll-option button {
  display: block;
  background-color: mediumpurple;
  color: white;
  border-radius: 5px;
  padding: 10px;
  margin: 5px 0;
  cursor: pointer;
  width: 200px;
  text-align: left;
}

.poll-option button:hover {
  background-color: darkslateblue;
}
</style>
