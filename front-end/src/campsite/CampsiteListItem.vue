<script>
import QuickAvailability from './QuickAvailability'

export default {
  components: { QuickAvailability },
  name: 'campsite-list-item',
  props: ['campsite', 'availability'],
  methods: {
    viewDetails () {
      this.$router.push({ name: 'campsite', params: { id: this.campsite.id } })
    }
  }
}
</script>

<template>
  <el-card class="box-card" :body-style="{ padding: '0px' }">
    <div slot="header" class="clearfix">
      <span class="name">{{ campsite.name }}</span>
      <el-button class="button" type="text" @click="viewDetails">View Details</el-button>
    </div>
    <img class="small" src="/campsite-example.jpg"/>
    <div style="padding: 14px;">
      <ul>
        <li class="size">{{ campsite.size }}'</li>
        <li class="electric">{{ campsite.electric }}</li>
        <li class="vehicles">Max {{ campsite.maxVehicles }} vehicle(s)</li>
        <li class="occupancy">Max {{ campsite.maxOccupancy }} guests</li>
        <li class="pets">Max {{ campsite.petsAllowed }} pets</li>
      </ul>
      <quick-availability :availability="availability"></quick-availability>
    </div>
  </el-card>
</template>

<style scoped>
.button {
  float: right;
  padding: 3px 0;
}

img {
  width: 100%;
}

.text {
  font-size: 14px;
}

.item {
  margin-bottom: 18px;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}
.clearfix:after {
  clear: both
}

.box-card {
  width: 480px;
  margin-bottom: 25px;
}
</style>
