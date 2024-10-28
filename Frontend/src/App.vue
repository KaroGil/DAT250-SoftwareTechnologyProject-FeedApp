<script setup lang="ts">
  import { ref } from 'vue';
  import PollComponent from "./components/PollComponent.vue";

  const showPollForm = ref(false);
</script>

<template>
  <header>
    <h1>Feed App</h1>
    <div class="wrapper">
      <p>Best group ever == group22 == {names: [Kaja, Karolina, Mina, Mampenda]}</p>
    </div>
  </header>

  <main>
    <button @click="change">
      Login
    </button>
    <button @click="showPollForm = true">
      Add Poll
    </button>
    <button @click="count++">
      See Polls
    </button>
    <button @click="count++">
      Vote
    </button>
    <button @click="count++">
      See Users
    </button>
  </main>
  
  <PollComponent v-if="showPollForm" :onCancel="handleCancel" />
</template>
