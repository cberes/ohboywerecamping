<script>
export default {
  name: 'signin',
  data () {
    return {
      signInConfig: {
        header: 'Sign in'
      }
    }
  }
}
</script>

<template>
  <div class="container">
    <amplify-sign-in :signInConfig="signInConfig"></amplify-sign-in>
  </div>
</template>

<style scoped>
</style>
