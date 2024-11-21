<script lang="ts">
    import { onMount } from 'svelte';
    import { defaultFetch } from '../utils/defaultFetch';
    import { getUserToken } from '../utils/sessionStorageUtil';
    import { writable } from 'svelte/store';
    import SeePolls from "./SeePolls.svelte";

    const pollQuestion = writable('');
    const pollOptions = writable('');
    const validUntil = writable('');
    const isPublic = writable(false);
    const minDateTime = writable('');

    export let items = [
        { id: 1, title: "Poll 1", description: "This is the first poll.", votes: 10 },
        { id: 2, title: "Poll 2", description: "This is the second poll.", votes: 15 },
        { id: 3, title: "Poll 3", description: "This is the third poll.", votes: 8 },
        { id: 4, title: "Poll 4", description: "This is the fourth poll.", votes: 20 },
        { id: 5, title: "Poll 5", description: "This is the fifth poll.", votes: 5 },
        { id: 6, title: "Poll 6", description: "This is the sixth poll.", votes: 12 },
        { id: 7, title: "Poll 7", description: "This is the seventh poll.", votes: 30 },
        { id: 8, title: "Poll 8", description: "This is the eighth poll.", votes: 25 },
        { id: 9, title: "Poll 9", description: "This is the ninth poll.", votes: 18 },
        { id: 10, title: "Poll 10", description: "This is the tenth poll.", votes: 22 },
    ];

    onMount(() => {
        minDateTime.set(new Date().toISOString().slice(0, 16));
    });

    const createPoll = async () => {
        const questionValue = $pollQuestion;
        const optionsValue = $pollOptions;
        const validUntilValue = $validUntil;
        const isPublicValue = $isPublic;

        const body = {
            question: questionValue,
            options: optionsValue.split(',').map((option, ind) => ({
                caption: option.trim(),
                presentationOrder: ind,
            })),
            validUntil: validUntilValue ? new Date(validUntilValue) : null,
            isPublic: isPublicValue,
        };

        try {
            const response = await defaultFetch('/polls', 'POST', getUserToken(), body);
            console.log(response);
        } catch (error) {
            console.error('Error creating poll:', error);
        }
    };
</script>

<div>
    <!-- Poll creation form -->
    <div class="modal">
        <h2>Create a New Poll</h2>
        <label>
            Poll Question:
            <input bind:value={$pollQuestion} type="text" placeholder="Question...?" />
        </label>
        <label>
            Options (comma separated):
            <input bind:value={$pollOptions} type="text" placeholder="Option 1, Option 2, Option 3" />
        </label>
        <label>
            Valid Until:
            <input bind:value={$validUntil} type="datetime-local" min={$minDateTime} />
        </label>
        <label>
            Public poll
            <input bind:checked={$isPublic} type="checkbox" />
        </label>
        <button on:click={createPoll}>Submit</button>
    </div>
    <div>
        {#each items as item}
            <SeePolls {item} />
        {/each}
    </div>
</div>

<style scoped>
    .modal {
        background-color: white;
        padding: 20px;
        border: 1px solid #ccc;
        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    }
</style>