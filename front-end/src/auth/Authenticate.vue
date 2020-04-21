<script>
export default {
  name: 'authenticate',
  data () {
    return {
      authConfig: {
        usernameAttributes: 'email'
      }
    }
  }
}
</script>

<template>
  <div class="container">
    <amplify-authenticator :authConfig="authConfig"></amplify-authenticator>
  </div>
</template>

<style scoped>
</style>
