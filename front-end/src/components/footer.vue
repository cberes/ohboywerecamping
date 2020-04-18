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
  <div>
    &copy; 2018 Corey Beres.
    <a href="/blog/" v-if="demo">Development Blog</a>
  </div>
</template>

<style scoped>
</style>
