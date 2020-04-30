<script>
import { mapActions, mapState } from 'vuex'
import { AmplifyEventBus } from 'aws-amplify-vue'
import _ from 'lodash'

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
  beforeDestroy () {
    AmplifyEventBus.$off('authState')
  },
  computed: {
    ...mapState('auth', ['redirectFrom'])
  },
  methods: {
    ...mapActions('auth', ['clearRedirect']),
    redirectAfterLogin () {
      if (this.redirectFrom) {
        const next = _.cloneDeep(this.redirectFrom)
        this.clearRedirect()
        this.$router.push(next)
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
