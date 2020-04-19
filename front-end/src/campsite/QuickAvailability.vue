<script>
import moment from 'moment'

export default {
  name: 'quick-availability',
  props: ['availability'],
  methods: {
    formatDate (period) {
      return moment(period.date, 'Y-MM-DD').format('dd<br>D')
    },
    formatStatus (period) {
      return period.status.charAt(0).toUpperCase()
    },
    statusClass (period) {
      switch (period.status) {
        case 'FIRST_COME_FIRST_SERVE':
          return 'fcfs'
        case 'IN_PERSON_ONLY':
          return 'in-person'
        default:
          return period.status.toLowerCase()
      }
    }
  }
}
</script>

<template>
  <div class="quick-availability" v-if="availability">
    <div class="availability-period" :key="period.date" v-for="period in availability">
      <div class="header" v-html="formatDate(period)"></div>
      <div class="body" :class="statusClass(period)">
        <abbr :title="period.status">{{ formatStatus(period) }}</abbr>
      </div>
    </div>
  </div>
  <div class="loading" v-else>Loading...</div>
</template>

<style scoped>
.availability-period {
  text-align: center;
  display: inline-block;
  margin-right: 10px;
}
.header, .body {
  display: block;
}
.header {
  font-size: small;
  margin-bottom: 5px;
}
.body {
  border-radius: 5px;
  padding: 0.5em 1em;
  font-family: Consolas,Monaco,Lucida Console,Liberation Mono,DejaVu Sans Mono,Bitstream Vera Sans Mono,Courier New, monospace;
}
.body abbr {
  text-decoration: none;
  cursor: help;
}
.fcfs, .in-person, .reserved, .available {
  font-weight: bold;
  text-transform: uppercase;
}
.fcfs {
  background-color: #fdf6ec;
  color: #e6a23c;
}
.in-person {
  background-color: #f4f4f5;
  color: #909399;
}
.available {
  background-color: #f0f9eb;
  color: #67c23a;
}
.loading {
  color: #555555;
}
</style>
