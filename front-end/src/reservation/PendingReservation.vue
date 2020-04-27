<script>
import { mapActions, mapState } from 'vuex'
import authService from '../auth/auth-service'
import campsiteService from '../campsite/mock-campsite-service'
import reservationService from '../reservation/reservation-service'

export default {
  name: 'pending-reservation',
  data () {
    return {
      campsite: null,
      days: [],
      error: null,
      loading: true
    }
  },
  computed: {
    ...mapState('reservation', ['pending'])
  },
  created () {
    if (!this.pending ||
        !this.pending.campsiteId ||
        !this.pending.days ||
        this.pending.days.length === 0) {
      this.$router.push({ name: 'home' })
      return
    }

    this.pending.days.forEach(day => this.days.push(day))
    this.days.sort()

    this.loadCampsite().then(() => (this.loading = true))
  },
  methods: {
    ...mapActions('reservation', ['clearPending']),
    loadCampsite () {
      return campsiteService.getCampsite(this.pending.campsiteId)
        .then(result => (this.campsite = result))
        .catch(reason => (this.error = reason.message))
    },
    async confirm () {
      const session = await authService.currentSession()
      const authToken = session.getIdToken().getJwtToken()
      reservationService.createReservation(this.campsite.campsiteId, this.days, authToken)
        .then(result => {
          this.clearPending()
          this.$router.push({ name: 'orders', params: { id: result.id } })
        }).catch(err => console.log(err))
    }
  }
}
</script>

<template>
  <div class="container" v-if="campsite">
    <h2>Reservation for {{ campsite.name }}</h2>
    <p>{{ days[0] }} - {{ days[days.length - 1] }} ({{ days.length }} days)</p>
    <el-button type="primary" @click="confirm">Confirm reservation</el-button>
  </div>
  <div class="error" v-else-if="error">{{ error }}</div>
  <div class="loading" v-else>Loading...</div>
</template>

<style scoped>
</style>
