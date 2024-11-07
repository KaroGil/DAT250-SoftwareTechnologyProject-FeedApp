<script setup lang="ts">
import { onMounted, ref } from 'vue'
import PollComponent from './components/PollComponent.vue'
import UserComponent from '@/components/UserComponent.vue'
import VoteComponent from '@/components/VoteComponent.vue'
import Login from '@/components/UserLogin.vue'
import SeePolls from '@/components/SeePolls.vue'
import { deleteUserToken, getUserToken } from '@/utils/sessionStorageUtil'
import CreateUser from '@/components/CreateUser.vue'

const currentView = ref('login') // login as default to make sure the user logs in
const changeView = (view: string) => {
  currentView.value = view
}

const isLoggedIn = ref(false) // Tracks if the user is logged in

// Check if a user token exists in sessionStorage or localStorage to set login status
onMounted(() => {
  isLoggedIn.value = Boolean(getUserToken())
  if (isLoggedIn.value) {
    changeView('seePoll')
  }
})

function logout() {
  // Remove the token and update login status
  changeView('login')
  deleteUserToken()
  isLoggedIn.value = false
}
</script>

<template>
  <header>
    <h1>Feed App</h1>
    <div class="wrapper">
      <p>
        Best group ever == group22 == {names: [Kaja, Karolina, Mina, Mampenda]}
      </p>
    </div>
  </header>

  <main class="navbar">
    <button v-if="!isLoggedIn" @click="changeView('login')">Login</button>
    <button  v-if="!isLoggedIn" @click="changeView('createUser')">Create User</button>
    <button v-if="isLoggedIn" @click="logout()">Logout</button>
    <button @click="changeView('seePoll')">See Polls</button>
    <!--    Only visable if user logged in-->
    <button v-if="isLoggedIn" @click="changeView('addPoll')">Add Poll</button>
    <button v-if="isLoggedIn" @click="changeView('vote')">Vote</button>
    <button v-if="isLoggedIn" @click="changeView('seeUsers')">See Users</button>
  </main>

  <section>
    <Login v-if="currentView === 'login'" />
    <CreateUser v-if="currentView === 'createUser'" />
    <SeePolls v-if="currentView === 'seePoll'" />
    <!--    Only visable if user logged in-->
    <UserComponent v-if="isLoggedIn && currentView === 'seeUsers'" />
    <PollComponent v-if="isLoggedIn && currentView === 'addPoll'" />
    <VoteComponent v-if="isLoggedIn && currentView === 'vote'" />
  </section>
</template>

<style scoped>
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
