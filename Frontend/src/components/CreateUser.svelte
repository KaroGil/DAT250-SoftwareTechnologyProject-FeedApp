<script lang="ts">
import { onMount } from 'svelte';
import { writable } from 'svelte/store';
import { defaultFetch } from '@/utils/defaultFetch';
import { getUserToken } from '@/utils/sessionStorageUtil';

const email = writable(''); // Writable store for email input
const password = writable(''); // Writable store for password input
const loading = writable(false); // Optional loading indicator
const errormsg = writable('');

async function createUser() {
  loading.set(true);

  try {
    const body = {
      email: $email,
      password: $password,
    };

    const response = await defaultFetch('/users', 'POST', getUserToken(), body);
    if (!response.ok) {
      // wrong info
      errormsg.set('Wrong format for information:(');
    } else {
      location.reload();
    }
  } catch (error) {
    console.error('Error:', error);
  } finally {
    loading.set(false);
  }
}
</script>

<div class="login">
<div class="modal">
  <h2>Create User</h2>
  <label>Email:</label>
  <input type="text" bind:value={$email} />
  <label>Password:</label>
  <input type="password" bind:value={$password} />
  <button on:click={createUser} disabled={$loading}>Signup</button>
  {#if $errormsg}
  <p class="error-msg">{$errormsg}</p>
  {/if}
</div>
</div>

<style>
.modal {
  background-color: white;
  padding: 30px;
  border: 1px solid #ccc;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.login {
  display: flex;
  justify-content: center;
}

.error-msg {
  color: red;
}
</style>
