<template>
  <div class="refresh-bar-div">
    <el-button type="primary" :disabled="autoRefresh" size="mini" @click="refresh">刷新</el-button>
    <el-switch
      style="width:210px;margin-left: 5px;margin-right: 5px"
      @change="modeChange"
      v-model="autoRefresh"
      :active-text="'定时刷新('+countdown+')'"
      inactive-text="手动刷新">
    </el-switch>
    <el-select v-model="timeout" placeholder="刷新时间" @change="resetTimeout" :disabled="!autoRefresh"
               size="mini"
               style="width:100px;">
      <el-option
        v-for="item in timeoutOptions"
        :key="item.value"
        :label="item.label"
        :value="item.value">
      </el-option>
    </el-select>
  </div>
</template>

<script>
  export default {
    name: "RefreshBar",
    data() {
      return {
        timeoutOptions: [
          {value: 15, label: "15秒"},
          {value: 30, label: "30秒"},
          {value: 90, label: "90秒"},
          {value: 120, label: "2分钟"}, {
            value: 300,
            label: "5分钟"
          }, {
            value: 600,
            label: "10分钟"
          }, {value: 900, label: "15分钟"}],
        timeout: 90,
        countdown: 0,
        autoRefresh: true,
        getDataTimer: null,
        countdownTimer: null,
      }
    },
    props: ['defaultTimeout'],
    mounted: function () {
      if (this.defaultTimeout && this.defaultTimeout > 0) {
        for (let index in this.timeoutOptions) {
          if (this.timeoutOptions[index].value == this.defaultTimeout) {
            this.timeout = this.defaultTimeout;
            break;
          }
        }
      }
      this.modeChange();

    },
    beforeDestroy: function () {
      this.clearRefreshDataTimer();
    },
    methods: {
      setCountdownTimer: function () {
        let that = this;
        this.countdown = this.timeout;
        this.countdownTimer = setInterval(function () {
          if (that.countdown > 0) {
            that.countdown--;
          } else {
            that.countdown = that.timeout;
          }
        }, 1001);//
      },
      clearCountdownTimer: function () {
        this.countdown = 0;
        if (this.countdownTimer != null) {
          clearTimeout(this.countdownTimer);
          this.countdownTimer = null;
        }
      },
      modeChange: function () {
        if (this.autoRefresh) {
          this.setRefreshDataTimer();
        } else {
          this.clearRefreshDataTimer();
        }
      },
      resetTimeout: function () {
        this.clearRefreshDataTimer();
        this.setRefreshDataTimer();
      },
      setRefreshDataTimer: function () {
        let that = this;
        this.setCountdownTimer();
        this.getDataTimer = setInterval(function () {
          that.refresh();
        }, that.timeout * 1000);
      },
      clearRefreshDataTimer: function () {
        this.clearCountdownTimer();
        if (this.getDataTimer != null) {
          clearTimeout(this.getDataTimer);
          this.getDataTimer = null;
        }
      },
      refresh: function () {
        this.$emit("refresh");
      },
    },
  }
</script>

<style scoped>

</style>
