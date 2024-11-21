<script lang="ts">
    import { onMount } from 'svelte';
    import { writable } from 'svelte/store';
    import { defaultFetch } from '../utils/defaultFetch';

    interface User {
        id: number;
        name: string;
        email: string;
    }

    const users = writable<User[]>([]);
    const loading = writable(true);

    // Fetch all users
    async function fetchUsers() {
        try {
            const response = await defaultFetch('/users', 'GET');
            users.set(await response);
            console.log('users:', $users);
        } catch (error) {
            console.error('Error:', error);
            alert('An error occurred. Please try again.');
        } finally {
            loading.set(false);
        }
    }

    // Call the fetchUsers function when the component is mounted
    onMount(fetchUsers);
</script>

<main>
    <div>
        <!-- Poll creation form -->
        <div class="modal">
            <h2>All users</h2>
            {#if $loading}
                <p>Loading users...</p>
            {:else}
                <ul>
                    {#each $users as user (user.id)}
                        <li>{user.name} ({user.email})</li>
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