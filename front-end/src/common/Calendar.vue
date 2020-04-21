<script>
import moment from 'moment'

const selectModes = ['NONE', 'SINGLE', 'MULTIPLE']

export default {
  name: 'calendar',
  props: {
    month: {
      type: Number,
      required: true,
      validator: i => i >= 1 && 1 <= 12
    },
    year: {
      type: Number,
      required: true,
      validator: i => i >= 0
    },
    info: {
      type: Object,
      required: false,
      default: () => ({})
    },
    selectMode: {
      type: String,
      required: false,
      validator: s => selectModes.includes(s)
    }
  },
  data () {
    return {
      days: ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'],
      selected: {}
    }
  },
  computed: {
    firstDay () {
      return moment({ year: this.year, month: this.month, day: 1 })
    },
    lastDay () {
      return moment(this.firstDay).endOf('month')
    },
    weeks () {
      const daysByWeek = []
      let firstDayOfWeek = this.firstDay
      let done = false
      while (!done) {
        const days = []
        let index = 0
        while (index < firstDayOfWeek.day()) {
          days.push({ index: index++, value: null })
        }
        const emptyDays = index
        while (index < 7) {
          const day = moment(firstDayOfWeek).add({ days: index - emptyDays })
          const value = day.month() === firstDayOfWeek.month() ? day : null
          days.push({ index: index++, value: value })
          done = !value
        }
        daysByWeek.push({ index: daysByWeek.length, days: days })
        firstDayOfWeek = moment(firstDayOfWeek.add({ days: index - emptyDays }))
        done = done || firstDayOfWeek.isAfter(this.lastDay)
      }
      return daysByWeek
    },
    emptyWeeks () {
      const weeksToAdd = 6 - this.weeks.length
      return [...Array(weeksToAdd).keys()].map(index => [{ index }])
    }
  },
  methods: {
    formatDate (date) {
      return date ? date.date() : ''
    },
    infoClass (date) {
      const value = date && this.info[date.format('Y-MM-DD')]
      return value ? value.cssClass : ''
    },
    infoValue (date) {
      const value = date && this.info[date.format('Y-MM-DD')]
      return value ? value.value : '&nbsp;'
    },
    isSelected (date) {
      if (!date) {
        return false
      }

      const key = date.format('Y-MM-DD')
      return this.selected[key] === true
    },
    toggleSelected (date) {
      if (!date || this.selectMode === 'NONE') {
        return
      }

      if (this.selectMode === 'SINGLE') {
        this.$set(this, 'selected', {})
      }

      const key = date.format('Y-MM-DD')
      this.$set(this.selected, key, !this.selected[key])
    }
  }
}
</script>

<template>
  <table>
    <tr>
      <th colspan="7">{{ firstDay.format('MMMM Y') }}</th>
    </tr>
    <tr>
      <th :key="day" v-for="day in days">{{day.substring(0, 3)}}</th>
    </tr>
    <tr :key="week.index" v-for="week in weeks">
      <td :key="day.index" v-for="day in week.days" @click="toggleSelected(day.value)" :class="{ selected: isSelected(day.value) }">
        <span class="date">{{ formatDate(day.value) }}</span>
        <span class="info" :class="infoClass(day.value)" v-html="infoValue(day.value)"></span>
      </td>
    </tr>
    <!-- Add extra weeks so that the calendar is the same size for every month -->
    <tr :key="week.index" v-for="week in emptyWeeks">
      <td colspan="7">
        <span class="date" v-html="'&nbsp;'"></span>
        <span class="info" v-html="'&nbsp;'"></span>
      </td>
    </tr>
  </table>
</template>

<style scoped>
* {
  text-align: center;
}
th, td {
  padding: 5px;
}
td {
  position: relative;
}
td.selected {
  background-color: lightblue;
}
.date {
  position: absolute;
  top: 0;
  left: 0;
  background-color: white;
  border-radius: 3px;
  line-height: 1;
  padding: 0.1em;
}
.info {
  display: block;
  border-radius: 5px;
  padding: 0.5em 1em;
  font-family: Consolas,Monaco,Lucida Console,Liberation Mono,DejaVu Sans Mono,Bitstream Vera Sans Mono,Courier New, monospace;
  margin: 2px;
}
.warning {
  background-color: #fdf6ec;
  color: #e6a23c;
}
.idle {
  background-color: #f4f4f5;
  color: #909399;
}
.success {
  background-color: #f0f9eb;
  color: #67c23a;
}
.loading {
  color: #555555;
}
</style>
