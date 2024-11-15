<script setup lang="ts">
import { defaultFetch } from '@/utils/defaultFetch'
import { ref } from 'vue'
import { setUserToken } from '@/utils/sessionStorageUtil'

const email = ref('') // Ref for email input
const password = ref('') // Ref for password input
const loading = ref(false) // Optional loading indicator
const errormsg = ref('')

async function login() {
  loading.value = true

  try {
    const body = {
      email: email.value,
      password: password.value,
    }
    console.log('body', body)
    const response = await defaultFetch('/users/login', 'POST', undefined, body)
    if (response.token != undefined) {
      console.log('Token received:', JSON.stringify(response.token))
      if (typeof window !== 'undefined') {
        setUserToken(JSON.stringify(response.token))
        location.reload()
      }
    } else {
      // wrong login
      errormsg.value = 'Wrong login information:('
    }
  } catch (error) {
    console.error('Error:', error)
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="login">
    <div class="modal">
      <h2>Login</h2>
      <label>Email:</label>
      <input type="text" v-model="email" />
      <label>Password:</label>
      <input type="password" v-model="password" />
      <button @click="login">Login</button>
      <p class="error-msg" v-if="errormsg">{{ errormsg }}</p>
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

.login {
  display: flex;
  justify-content: center;
}

.error-msg {
  color: red;
}
</style>
