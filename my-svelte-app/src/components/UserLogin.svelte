<script lang="ts">
    import { defaultFetch } from '../utils/defaultFetch';
    import { setUserToken } from '../utils/sessionStorageUtil';
    import { writable } from 'svelte/store';

    const email = writable(''); // Store for email input
    const password = writable(''); // Store for password input
    const loading = writable(false); // Optional loading indicator
    const errormsg = writable('');

    async function login() {
        loading.set(true);

        try {
            let emailValue: string, passwordValue: string;
            email.subscribe(value => (emailValue = value))();
            password.subscribe(value => (passwordValue = value))();

            const body = {
                email: emailValue,
                password: passwordValue,
            };
            console.log('body', body);
            const response = await defaultFetch('/users/login', 'POST', undefined, body);
            if (response.token != undefined) {
                console.log('Token received:', JSON.stringify(response.token));
                if (typeof window !== 'undefined') {
                    setUserToken(JSON.stringify(response.token));
                    location.reload();
                }
            } else {
                // wrong login
                errormsg.set('Wrong login information :(');
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
        <h2>Login</h2>
        <label>Email:</label>
        <input type="text" bind:value={$email} />
        <label>Password:</label>
        <input type="password" bind:value={$password} />
        <button on:click={login} disabled={$loading}>
            {#if $loading}
                Logging in...
            {:else}
                Login
            {/if}
        </button>
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
