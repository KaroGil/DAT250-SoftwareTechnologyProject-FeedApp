<script setup lang="ts">
import { ref } from 'vue';
import { defaultFetch } from '@/components/defaultFetch';

// Define interfaces for Poll and VoteOption
interface Poll {
  id: string;
  question: string;
  options: VoteOption[];
}

interface VoteOption {
  id: string;
  text: string;
}

// Reactive variables to store poll data and loading state
const polls = ref<Poll[]>([]);
const loading = ref(true);
const openPollId = ref<string | null>(null); // Stores the ID of the currently open poll

// Fetches all polls from the server
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

// Sends a vote to the server for a specific poll option
// TODO
async function vote(pollId: string, optionId: string) {
  try {
    await defaultFetch(`/polls/${pollId}/vote`, "POST", undefined, { optionId });
    alert('Vote submitted successfully!');
  } catch (error) {
    console.error('Error:', error);
    alert('An error occurred. Please try again.');
  }
}

// Toggles dropdown visibility for a specific poll
function toggleOptions(pollId: string) {
  openPollId.value = openPollId.value === pollId ? null : pollId;
}

// Fetch polls when the component is mounted
fetchPolls();
</script>

<template>
  <main>
    <div class="modal">
      <h2>All Polls</h2>
      <p v-if="loading">Loading polls...</p>
      <div v-else>
        <!-- Loop through each poll and display its question -->
        <ul v-for="poll in polls" :key="poll.id" class="poll">
          <li @click="toggleOptions(poll.id)" class="poll-question">
            <!-- Dropdown arrow rotates when open -->
            <span :class="{'arrow': true, 'open': openPollId === poll.id}">▼</span>
            {{ poll.question }}
          </li>
          <!-- Show options if this poll is open -->
          <div v-if="openPollId === poll.id" class="poll-options">
            <!-- Display each option as a button -->
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
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1); /* Light shadow for modal effect */
}

.poll {
  font-size: 18px;
  margin-bottom: 20px; /* Spacing between polls */
}

.poll-question {
  font-weight: bold;
  cursor: pointer; /* Makes the question clickable */
  color: #333;
  display: flex;
  align-items: center;
  gap: 10px; /* Space between arrow and question */
}

.arrow {
  display: inline-block;
  transition: transform 0.3s ease; /* Smooth rotation transition */
}

.arrow.open {
  transform: rotate(180deg); /* Rotate arrow when open */
}

.poll-options {
  margin-top: 10px; /* Space between question and options */
}

.poll-option button {
  display: block;
  background-color: mediumpurple;
  color: white;
  border-radius: 5px;
  padding: 10px;
  margin: 5px 40px; /* Adds space around each option */
  cursor: pointer;
  width: 200px;
  text-align: left; /* Aligns text to the left within button */
}

.poll-option button:hover {
  background-color: darkslateblue; /* Darker color on hover */
}
</style>
