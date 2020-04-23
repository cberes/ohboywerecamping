<script>
import { mapActions, mapState } from 'vuex'
import campsiteService from '../campsite/mock-campsite-service'

export default {
  name: 'pending-reservation',
  props: {
  },
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
    confirm () {
      // TODO add the reservation to the database
      this.clearPending()
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
