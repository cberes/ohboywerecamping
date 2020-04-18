<script>
export default {
  data () {
    return {
      demo: window.location.hostname === 'awayfromho.me'
    }
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
  </el-menu>
</template>

<style module>
</style>
