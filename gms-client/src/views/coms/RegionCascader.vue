<template>
  <div>
    <el-cascader
      v-model="regionIds"
      :options="regionObjectList"
      @change="handleRegionChange"
      separator=" "
      :props="props"
      filterable
      size="mini"
      change-on-select
      style="width:100%"
    >
    </el-cascader>
  </div>
</template>

<script>
  import region from '@/assets/data/region.json'

  export default {
    data() {
      return {
        regionObjectList: [],
        props: {value: 'id', label: 'name', children: 'children'},
      }
    },
    props: ['regionIds'],
    mounted: function () {
    },
    created: function () {
      this.regionObjectList = region.children;
    },
    beforeDestroy: function () {
    },
    methods: {
      handleRegionChange(val) {
        this.$emit("change", val);
      },
    },
  }
</script>

<style scoped>

</style>
