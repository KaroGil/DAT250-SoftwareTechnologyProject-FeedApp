<script setup lang="ts">
/*interface User {
  id: number;
  name: string;
  email: string;
}*/

//const users = ref<User[]>([]);


async function login() {
  // TODO: implement login logic
  /*try {
   /!* const response = await defaultFetch("/users", "POST");
    users.value = await response;
    console.log("users: ", users)*!/
  } catch (error) {
    console.error('Error:', error);
    alert('An error occurred. Please try again.');
  } finally {
    loading.value = false;
  }*/
}

</script>

<template>
  <div class="login">
    <!-- Poll creation form -->
    <div class="modal">
      <h2>Login</h2>
      <label>Email:</label>
      <input type="text" />
      <label>Password:</label>
      <input type="text" />
      <div>
        <button @click="login">Login</button>
        <button @click="props.onCancel">Cancel</button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.modal {
  background-color: white;
  padding: 30px;
  border: 1px solid #ccc;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.login{
  display: flex;
  justify-content: center;
}
</style>
