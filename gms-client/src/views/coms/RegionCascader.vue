<template>
    <div>
        <el-cascader
                v-model="regionIds"
                :options="regionObjectList"
                @change="handleRegionChange"
                @active-item-change="handleItemChange"
                separator=" "
                filterable
                :props="props"
                size="mini"
                style="width:100%">
        </el-cascader>
    </div>
</template>

<script>
    import qs from 'qs';

    export default {
        data() {
            return {
                tempList: null,
                lengthArray: [2, 4, 6, 9],
                regionIds: [],
                regionObjectList: [],
                props: {value: 'id', label: 'name', children: 'children'},
            }
        },
        props: ['regionId'],
        mounted: function () {
            // this.getRegionIds(-1);
            this.getRegionIds(this.regionId);
            // this.getByParentId();
            let that = this;
            this.getByParentId(this.regionId, function () {
                let temp = this.regionIds.slice(0, i);
                this.getByParentId(temp);
            });
        },
        created() {
        },
        methods: {
            handleItemChange(val) {
                if (val && val.length > 0) {
                    console.log(val);
                    this.getByParentId(val);
                }
            },
            appendChild(children, parentList, parentIdList, depth) {
                if (!parentIdList) {
                    return;
                }
                let regionId = parentIdList[depth];
                for (let key in parentList) {
                    if (regionId == parentList[key].id) {
                        if (depth == parentIdList.length - 1) {
                            parentList[key].children = children;
                        } else {
                            depth++;
                            this.appendChild(children, parentList[key].children, parentIdList, depth);
                        }
                        break;
                    }
                }
            },
            getByParentId(parentIdArray, callback) {
                let that = this;
                let parentId = 0;
                if (parentIdArray && parentIdArray.length > 0) {
                    parentId = parentIdArray[parentIdArray.length - 1];
                }
                this.$http.post("/mid-region/region/select", qs.stringify({parentId: parentId})).then(res => {
                    if (res.data.result && res.data.data) {
                        let tempData = res.data.data;
                        //判断是否还有下级区域
                        if (tempData && tempData.length > 0 && tempData[0].level <= that.lengthArray.length) {
                            if (tempData[0].level < that.lengthArray.length) {
                                for (let key in tempData) {
                                    tempData[key].children = [];
                                }
                            }
                            if (!that.regionObjectList || that.regionObjectList.length == 0) {
                                that.regionObjectList = tempData;
                            } else {
                                that.appendChild(tempData, that.regionObjectList, parentIdArray, 0);
                            }
                        }
                        callback && callback();
                    }
                });
            },
            getRegionIds(regionId) {
                if (!regionId) {
                    return;
                }
                regionId = regionId + "";
                let lengthArray = this.lengthArray;
                let regionIdArray = [];
                this.getNextRegionId(regionId, regionIdArray, 0, lengthArray);
                this.regionIds = regionIdArray;
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
