<template>
    <div>
        <el-cascader
                v-model="regionIds"
                :options="regionObjectList"
                @change="handleRegionChange"
                separator=" "
                filterable
                size="mini"
                :props="props"
                change-on-select
                style="width:100%">
        </el-cascader>
    </div>
</template>

<script>
    import region from '@/assets/data/regions.json'

    export default {
        data() {
            return {
                regionIds: [],
                regionObjectList: [],
                props: {value: 'adcode', label: 'name', children: 'districts'},
            }
        },
        props: ['regionId'],
        mounted: function () {
            this.regionObjectList = region.children;
            this.getRegionIds(this.regionId);
        },
        methods: {
            beforeDestroy: function () {

            },
            getRegionIds(regionId) {
                if (!regionId) {
                    return;
                }
                regionId = regionId + "";
                let lengthArray = [2, 4, 6];
                this.regionIds = [];
                this.getNextRegionId(regionId, this.regionIds, 0, lengthArray);
            },
            getNextRegionId(regionId, array, index, lengthArray) {
                if (lengthArray.length <= index) {
                    return;
                }
                regionId = regionId + "";
                let temp = lengthArray[index];
                array.push(Number(regionId.substring(0, temp)));
                index++;
                this.getNextRegionId(regionId, array, index, lengthArray);
            },
            handleRegionChange(val) {
                this.$emit("change", val[val.length - 1]);
            },
        },
    }
</script>

<style scoped>

</style>
