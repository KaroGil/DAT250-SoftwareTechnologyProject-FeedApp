<script lang="ts">
import { onMount } from 'svelte';
import { writable } from 'svelte/store';
import { defaultFetch } from '@/utils/defaultFetch';
import { getUserToken } from '@/utils/sessionStorageUtil';

interface Poll {
  id: string; // UUID generated for the poll
  creatorUserID: string; // UUID of the user who created the poll
  question: string; // The poll's question
  publishedAt: string; // The time when the poll was published (ISO 8601 format)
  validUntil: string; // The expiration date and time for the poll (ISO 8601 format)
  options: VoteOption[]; // An array of vote options for the poll
  isPublic: boolean; // Indicates whether the poll is public
}

interface VoteOption {
  caption: string;
  presentationOrder: number;
}

const polls = writable<Poll[]>([]);
const loading = writable(true);

// Fetch all polls
async function fetchPolls() {
  try {
    const token: string | undefined = getUserToken();
    const response = await defaultFetch('/polls', 'GET', token);
    polls.set(await response);
  } catch (error) {
    console.error('Error:', error);
  } finally {
    loading.set(false);
  }
}

async function vote(pollId: string, optionId: string) {
  const body = {
    voteOptionId: optionId,
    pollId: pollId,
  };

  try {
    await defaultFetch('/votes', 'POST', getUserToken(), body);
  } catch (error) {
    console.error('Error:', error);
  }
}

// Call the fetchPolls function when the component is mounted
onMount(fetchPolls);
</script>

<main>
<div>
  <div class="modal">
    <h2>All Polls</h2>
    {#if $loading}
    <p>Loading polls...</p>
    {:else}
    <ul>
      {#each $polls as poll (poll.id)}
      <li>
        {poll.question}
        <div>
          {#each poll.options as option (option.caption)}
          <button on:click={() => vote(poll.id, option.caption)}>
          {option.caption}
          </button>
          {/each}
        </div>
      </li>
      {/each}
    </ul>
    {/if}
  </div>
</div>
</main>

<style>
.modal {
  background-color: white;
  padding: 20px;
  border: 1px solid #ccc;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}
</style>
