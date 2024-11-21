<script lang="ts">
    import { onMount } from 'svelte';
    import { defaultFetch } from '../utils/defaultFetch';
    import { writable, derived } from 'svelte/store';

    interface Vote {
        pollid: number;
        voteOptionId: number;
    }

    interface Poll {
        id: number;
        creatorUserID: string;
        question: string;
        publishedAt: string;
        validUntil: string;
        options: VoteOption[];
        isPublic: boolean;
    }

    interface VoteOption {
        id: number;
        caption: string;
        presentationOrder: number;
    }

    const votes = writable<Vote[]>([]);
    const polls = writable<Poll[]>([]);
    const loading = writable(true);

    async function fetchVotes() {
        try {
            const response = await defaultFetch('/votes', 'GET');
            votes.set(response);
            console.log('votes: ', response);
        } catch (error) {
            console.error('Error:', error);
            alert('An error occurred. Please try again.');
        } finally {
            loading.set(false);
        }
    }

    async function fetchPolls() {
        try {
            const response = await defaultFetch('/polls', 'GET');
            polls.set(response);
            console.log('polls: ', response);
        } catch (error) {
            console.error('Error:', error);
            alert('An error occurred. Please try again.');
        }
    }

    const mappedVotes = derived([votes, polls], ([$votes, $polls]) => {
        return $votes.map(vote => {
            const poll = $polls.find(p => p.id === vote.pollid);
            const voteOption = poll?.options.find(option => option?.id === vote.voteOptionId);
            return {
                ...vote,
                pollName: poll?.question || 'Unknown Poll',
                voteOptionCaption: voteOption?.caption || 'Unknown Option',
            };
        });
    });

    onMount(() => {
        fetchPolls();
        fetchVotes();
    });
</script>

<main>
    <div>
        <!-- Poll creation form -->
        <div class="modal">
            <h2>Vote</h2>
            {#if $loading}
                <p>Loading users...</p>
            {:else}
                <ul>
                    {#each $mappedVotes as vote (vote.pollName)}
                        <li>Poll: {vote.pollName} | Answer: {vote.voteOptionCaption}</li>
                    {/each}
                </ul>
            {/if}
        </div>
    </div>
</main>

<style scoped>
    .modal {
        background-color: white;
        padding: 20px;
        border: 1px solid #ccc;
        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    }a
</style>