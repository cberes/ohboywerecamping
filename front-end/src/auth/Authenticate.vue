<script>
import { mapActions, mapState } from 'vuex'
import { AmplifyEventBus } from 'aws-amplify-vue'

export default {
  name: 'authenticate',
  data () {
    return {
      authConfig: {
        usernameAttributes: 'email'
      }
    }
  },
  created () {
    AmplifyEventBus.$on('authState', info => {
      if (info === 'signedIn') {
        this.redirectAfterLogin()
      }
    })
  },
  computed: {
    ...mapState('auth', ['redirectFrom'])
  },
  methods: {
    ...mapActions('auth', ['clearRedirect']),
    redirectAfterLogin () {
      if (this.redirectFrom) {
        this.clearRedirect()
        this.$router.push(this.redirectFrom)
      } else {
        this.$router.push({ name: 'home' })
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
