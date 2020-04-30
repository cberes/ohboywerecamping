<script>
import { AmplifyEventBus } from 'aws-amplify-vue'
import authService from '@/auth/auth-service'

export default {
  name: 'authenticate',
  data () {
    return {
      loading: true,
      currentSession: null
    }
  },
  created () {
    AmplifyEventBus.$on('authState', info => {
      if (info === 'signedOut') {
        this.$router.push({ name: 'home' })
      }
    })

    authService.currentSession()
      .then(session => {
        this.currentSession = session
        this.loading = false
      })
  },
  beforeDestroy () {
    AmplifyEventBus.$off('authState')
  }
}
</script>

<template>
  <div class="container">
    <h2>Thanks for stopping by</h2>
    <p>Come back real soon now!</p>
    <amplify-sign-out></amplify-sign-out>
  </div>
</template>

<style scoped>
</style>
