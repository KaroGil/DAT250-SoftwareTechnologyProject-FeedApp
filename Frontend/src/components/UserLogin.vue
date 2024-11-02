<script setup lang="ts">
import { defaultFetch } from '@/utils/defaultFetch'
import { ref } from 'vue'
import { setUserToken } from '@/utils/sessionStorageUtil'

const users = ref('');
const email = ref('');      // Ref for email input
const password = ref('');    // Ref for password input
const loading = ref(false);  // Optional loading indicator


async function login() {
  loading.value = true;

  try {
    const body = {
      "email": email.value,
      "password": password.value
    };
    console.log("body", body)
    const response = await defaultFetch("/users/login", "POST", null, body);
    if(response != null){
      // wrong login information
      // no token
      console.log("Token received:", JSON.stringify(response.token));
      if (typeof window !== 'undefined') {
        setUserToken(JSON.stringify(response.token));
      }
    }
  } catch (error) {
    console.error('Error:', error);
  } finally {
    loading.value = false;
    location.reload();
  }
}

</script>

<template>
  <div class="login">
    <div class="modal">
      <h2>Login</h2>
      <label>Email:</label>
      <input type="text" v-model="email"/>
      <label>Password:</label>
      <input type="password" v-model="password"/>
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
