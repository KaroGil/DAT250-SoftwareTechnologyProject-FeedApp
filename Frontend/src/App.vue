<script setup lang="ts">

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
    <button @click="count++">
      Vote
    </button>
    <button @click="count++">
      See polls
    </button>
    <button @click="count++">
      See users
    </button>
  </main>
</template>
