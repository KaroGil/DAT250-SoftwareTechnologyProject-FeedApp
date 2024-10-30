<script setup lang="ts">
  import { ref } from 'vue';
  import PollComponent from "./components/PollComponent.vue";
  import UserComponent from '@/components/UserComponent.vue';
  import VoteComponent from '@/components/VoteComponent.vue';
  import Login from '@/components/Login.vue';
  import SeePolls from '@/components/SeePolls.vue'

  const currentView = ref('login'); // login as defualt to make sure the user logs in
  const changeView = (view: string) =>{
    currentView.value = view;
  };
</script>

<template>
  <header>
    <h1>Feed App</h1>
    <div class="wrapper">
      <p>Best group ever == group22 == {names: [Kaja, Karolina, Mina, Mampenda]}</p>
    </div>
  </header>

  <main class="navbar">
    <button @click="changeView('login')"> Login </button>
    <button @click="changeView('seePoll')"> See Polls </button>
    <!--    Only visable if user logged in-->
    <button @click="changeView('addPoll')"> Add Poll </button>
    <button @click="changeView('vote')"> Vote </button>
    <button @click="changeView('seeUsers')"> See Users </button>
  </main>

  <section>
    <Login v-if="currentView === 'login'" />
    <SeePolls v-if="currentView === 'seePoll'" />
    <!--    Only visable if user logged in-->
    <UserComponent v-if="currentView === 'seeUsers'" />
    <PollComponent v-if="currentView === 'addPoll'" />
    <VoteComponent v-if="currentView === 'vote'" />
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
