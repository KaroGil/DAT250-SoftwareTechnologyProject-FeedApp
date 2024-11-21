<script lang="ts">
    import { onMount } from 'svelte';
    import { defaultFetch } from '../utils/defaultFetch';
    import { getUserToken } from '../utils/sessionStorageUtil';
    import { writable } from 'svelte/store';

    const pollQuestion = writable('');
    const pollOptions = writable('');
    const validUntil = writable('');
    const isPublic = writable(false);
    const minDateTime = writable('');

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
</div>

<style scoped>
    .modal {
        background-color: white;
        padding: 20px;
        border: 1px solid #ccc;
        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    }
</style>