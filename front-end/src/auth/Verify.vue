<script>
export default {
  name: 'verify',
  data () {
    return {
      confirmSignUpConfig: {
        header: 'Verify your email address'
      }
    }
  }
}
</script>

<template>
  <div class="container">
    <amplify-confirm-sign-up :confirmSignUpConfig="confirmSignUpConfig"></amplify-confirm-sign-up>
  </div>
</template>

<style scoped>
</style>
