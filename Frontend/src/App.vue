<script lang="ts">
import { writable, onMount } from 'svelte/store';
import PollComponent from './components/PollComponent.svelte';
import UserComponent from '@/components/UserComponent.svelte';
import VoteComponent from '@/components/VoteComponent.svelte';
import Login from '@/components/UserLogin.svelte';
import SeePolls from '@/components/SeePolls.svelte';
import { deleteUserToken, getUserToken } from '@/utils/sessionStorageUtil';
import CreateUser from '@/components/CreateUser.svelte';

const currentView = writable('login'); // Default view to ensure user logs in
const isLoggedIn = writable(false); // Tracks if the user is logged in

function changeView(view: string) {
  currentView.set(view);
}

function logout() {
  // Remove the token and update login status
  changeView('login');
  deleteUserToken();
  isLoggedIn.set(false);
}

onMount(() => {
  isLoggedIn.set(Boolean(getUserToken()));
  if (getUserToken()) {
    changeView('seePoll');
  }
});
</script>

<header>
<h1>Feed App</h1>
<div class="wrapper">
  <p>
    Best group ever == group22 == {names: [Kaja, Karolina, Mina, Mampenda]}
  </p>
</div>
</header>

<main class="navbar">
{#if !$isLoggedIn}
<button on:click={() => changeView('login')}>Login</button>
<button on:click={() => changeView('createUser')}>Create User</button>
{:else}
<button on:click={logout}>Logout</button>
<button on:click={() => changeView('addPoll')}>Add Poll</button>
<button on:click={() => changeView('vote')}>Vote</button>
<button on:click={() => changeView('seeUsers')}>See Users</button>
{/if}
<button on:click={() => changeView('seePoll')}>See Polls</button>
</main>

<section>
{#if $currentView === 'login'}
<Login />
{/if}
{#if $currentView === 'createUser'}
<CreateUser />
{/if}
{#if $currentView === 'seePoll'}
<SeePolls />
{/if}
{#if $isLoggedIn && $currentView === 'seeUsers'}
<UserComponent />
{/if}
{#if $isLoggedIn && $currentView === 'addPoll'}
<PollComponent />
{/if}
{#if $isLoggedIn && $currentView === 'vote'}
<VoteComponent />
{/if}
</section>

<style>
/* Make the navbar horizontal */
.navbar {
  display: flex;
  gap: 1rem;
  flex-direction: row;
  justify-content: center;
  margin-bottom: 1rem;
}

button {
  cursor: pointer;
}
</style>
