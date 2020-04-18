<script>
import Footer from '@/components/footer'
import Header from '@/components/header'
import NavBar from '@/components/nav-bar'

export default {
  components: { Footer, Header, NavBar },
  props: {
    title: {
      type: String,
      default: ''
    }
  },
  data () {
    return {
      menuOpen: false
    }
  },
  computed: {
    menuIcon () {
      return this.menuOpen ? 'el-icon-close' : 'el-icon-menu'
    }
  }
}
</script>

<template>
  <el-container>
    <el-header>
      <el-button class="hidden-md-and-up" type="text" :icon="menuIcon" @click="menuOpen = !menuOpen"></el-button>
      <Header :title="title"></Header>
    </el-header>
    <el-container>
      <el-aside width="200px" class="hidden-sm-and-down">
        <NavBar/>
      </el-aside>
      <el-container>
        <el-aside width="100%" class="hidden-md-and-up" v-if="menuOpen">
          <NavBar/>
        </el-aside>
        <el-main>
          <slot/>
        </el-main>
        <el-footer>
          <Footer></Footer>
        </el-footer>
      </el-container>
    </el-container>
  </el-container>
</template>

<style scoped>
.el-header button, .el-header button:active, .el-header button:focus {
  outline: none;
  float: left;
  font-size: 2em;
  margin: 0.67em 0.33em 0.67em 0;
  padding: 0;
  color: #000000;
}
</style>
