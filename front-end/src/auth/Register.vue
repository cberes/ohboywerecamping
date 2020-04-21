<script>

export default {
  name: 'register',
  data () {
    return {
      signUpConfig: {
        defaultCountryCode: '1'
      }
    }
  }
}
</script>

<template>
  <div class="container">
    <amplify-sign-up :signUpConfig="signUpConfig"></amplify-sign-up>
  </div>
</template>

<style scoped>
</style>
