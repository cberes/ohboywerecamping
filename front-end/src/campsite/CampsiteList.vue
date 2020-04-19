<script>
import moment from 'moment'
import campgroundService from '../campground/mock-campground-service'
import availabilityService from './mock-availability-service'
import CampsiteListItem from './CampsiteListItem'

export default {
  components: { CampsiteListItem },
  name: 'campsite-list',
  data () {
    return {
      availability: {},
      campgroundId: 2, // TODO real campground ID
      campsites: null,
      error: null
    }
  },
  mounted () {
    this.loadCampsites()
    this.loadAvailability()
  },
  methods: {
    loadCampsites () {
      campgroundService.getCampsites(this.campgroundId)
        .then(result => (this.campsites = result))
        .catch(reason => (this.error = reason.message))
    },
    loadAvailability () {
      const start = moment().add(1, 'days')
      const end = start.clone().add(5, 'days')
      availabilityService.getCampground(this.campgroundId, start, end)
        .then(result => (this.availability = availabilityService.keyByCampsiteId(result)))
        .catch(reason => (this.error = reason.message))
    }
  }
}
</script>

<template>
  <div class="container">
    <h2>Campsite list</h2>
    <el-container direction="vertical" v-if="campsites">
      <el-row>
        <el-col :xs="24" :sm="24" :md="24" :lg="12" :xl="8" :key="campsite.id" v-for="campsite in campsites">
          <campsite-list-item
            :campsite="campsite"
            :availability="availability[campsite.id]"></campsite-list-item>
        </el-col>
      </el-row>
    </el-container>
    <el-container class="error" v-else-if="error">{{ error }}</el-container>
    <el-container class="loading" v-else>Loading...</el-container>
  </div>
</template>

<style scoped>
.loading {
  color: #555555;
}

.error {
  color: #660000;
}
</style>
