<script>
import authService from '@/auth/auth-service'

export default {
  data () {
    return {
      currentSession: null
    }
  },
  created () {
    authService.currentSession()
      .then(session => (this.currentSession = session))
  }
}
</script>

<template>
  <el-menu :router="true">
    <el-menu-item index="/">
      <i class="el-icon-location"></i> Campground Home
    </el-menu-item>
    <el-menu-item index="/campsites">
      <i class="el-icon-tickets"></i> Campsite List
    </el-menu-item>
    <el-menu-item index="/map">
      <i class="el-icon-picture"></i> Campsite Map
    </el-menu-item>
    <el-menu-item index="/orders" v-if="currentSession">
      <i class="el-icon-s-order"></i> Orders
    </el-menu-item>
  </el-menu>
</template>

<style module>
</style>
