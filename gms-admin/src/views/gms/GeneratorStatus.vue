<template>
    <div>
        <link href="//at.alicdn.com/t/font_825173_c8zu2vuwunt.css">
        <el-card class="info-card box-card">
            <div slot="header">
                <strong
                        class="header-status-strong">测试油机(V1.025)</strong><span>交流</span>
                <div class="header-status-div">
                    <span>更新时间 : </span><strong style="margin-right: 10px;">2019-03-15 11:11:11</strong>
                    <span class="header-status-span">GPRS :
            <i class="iconfont icon-signal online" v-if="true"></i>
            <i class="iconfont icon-signal offline" v-else></i>
          </span>
                </div>
            </div>
            <div class="status-top-div">
                <div class="status-left-div">
                    <strong v-bind:class="{ 'online': true}">
                        <i v-bind:class="{ 'icon-light_fill': true,'icon-xiumian':true , 'iconfont': true}"></i>发电中<br>
                    </strong>
                    <!--<canvas ref="gasTank" @click="showChartsDialog('lankLevel')"></canvas>-->
                    <svg width="150" height="150" class="fuelTank-svg">
                        <circle cx="75" cy="75" r="60" stroke-width="10" stroke="#D1D3D7" fill="none"></circle>
                        <text ref="fuelInfoText" x="75" y="75" style="font-size: 15px;">
                            44L(66%)
                        </text>
                        <circle ref="fuelTankCircle" cx="75" cy="75" r="60" stroke-width="10" stroke="#00A5E0"
                                fill="none"
                                transform="matrix(0,-1,1,0,0,150)" stroke-dasharray="0 378"></circle>
                    </svg>
                    <br>
                    <span class="right-status-header">汽油</span><br>
                </div>
                <table style="width:70%">
                    <tr class="status-tr">
                        <td>
                            <el-button class="iconfont status-icon icon-dianya4"
                            ></el-button>
                            <br>
                            <strong>44V</strong><br><span>启动电压</span>
                        </td>
                        <td>
                            <el-button class="iconfont status-icon icon-dianya4"
                            ></el-button>
                            <br>
                            <strong>12V</strong><br><span>电池电压</span>
                        </td>
                        <td>
                            <el-button class="iconfont status-icon icon-dianya4"
                            ></el-button>
                            <br>
                            <strong>55V</strong><br><span>输出电压</span>
                        </td>
                        <td>
                            <el-button class="iconfont status-icon icon-dianliu2"
                            ></el-button>
                            <br>
                            <strong>12A</strong><br><span>输出电流</span>
                        </td>
                    </tr>
                    <tr class="status-tr">
                        <td>
                            <el-button class="iconfont status-icon icon-shijian"
                            ></el-button>
                            <br>
                            <strong>123min</strong><br><span>累计发电</span>
                        </td>
                        <td>
                            <el-button class="iconfont status-icon icon-daojishi"
                            ></el-button>
                            <br>
                            <strong>12h</strong><br><span>距离保养</span>
                        </td>
                        <td>
                            <el-button class="iconfont status-icon icon-wendu4"
                            ></el-button>
                            <br>
                            <strong>11℃</strong><br><span>电机温度</span>
                        </td>
                        <td>
                            <el-button class="iconfont status-icon icon-eryanghuatan"
                            ></el-button>
                            <br>
                            <strong>123ppm</strong><br><span>一氧化碳</span>
                        </td>
                    </tr>
                </table>
            </div>
            <div style="padding-bottom: 15px">
                <div class="status-bottom-div">
                    <div id="warningChart" style="height:370px;width:60%;"
                         ref="warningChart">
                    </div>
                </div>
            </div>
        </el-card>
        <el-card class="control-card box-card">
            <div slot="header">
                <span class="right-status-header">市电:1</span>
                <span class="right-status-header">带载:2</span>
                <span class="right-status-header">2</span>
            </div>
            <div class="control-card-body">
                <div style="height: 60px;width: 100%;margin-bottom: 1px;float: left;text-align: center">
                    <table style="width:100%" class="operate-table">
                        <tr class="status-tr ">
                            <td>
                                <el-button
                                        class="iconfont   icon-qidong3 operate-button"

                                ></el-button>
                                <br>
                                <span>启动油机</span>
                            </td>
                            <td>
                                <el-button
                                        class="iconfont  icon-tingzhi operate-button"

                                ></el-button>
                                <br>
                                <span>关闭油机</span>
                            </td>
                            <td>
                                <el-button
                                        class="iconfont   icon-youpinwangtubiao- operate-button"

                                ></el-button>
                                <br>
                                <span>手动加油</span>
                            </td>
                            <td>
                                <el-button
                                        class="iconfont  icon-qiehuan1 operate-button"

                                ></el-button>
                                <br>
                                <span>模式切换</span>
                            </td>
                            <td>
                                <el-button
                                        class="iconfont  icon-weihu1 operate-button"
                                        v-bind:class="{ 'maintaining': true,'offline': true}"
                                ></el-button>
                                <br>
                                <span v-if="false">退出维护</span>
                                <span v-else>维护油机</span>
                            </td>
                        </tr>
                        <tr class="status-tr ">
                            <td>
                                <el-button
                                        class="iconfont   icon-xiugai operate-button"

                                ></el-button>
                                <br>
                                <span>停机时间</span>
                            </td>
                            <td>
                                <el-button
                                        class="iconfont   icon-shangsuo1 operate-button"

                                ></el-button>
                                <br>
                                <span>油箱上锁</span>
                            </td>
                            <td>
                                <el-button
                                        class="iconfont   icon-kaisuo1 operate-button"

                                ></el-button>
                                <br>
                                <span>油箱开锁</span>
                            </td>
                            <td>
                                <el-button
                                        class="iconfont   icon-icon2 operate-button"
                                ></el-button>
                                <br>
                                <span>定时睡眠</span>
                            </td>
                        </tr>
                    </table>
                </div>
            </div>
        </el-card>

    </div>
</template>

<script>
    import store from "../../store";
    import {Notification} from "element-ui";

    let echarts = require('echarts/lib/echarts');
    require('echarts/lib/chart/bar');
    require('echarts/lib/component/tooltip');
    require('echarts/lib/component/title');
    export default {
        data() {
            return {
                token: store.state.token,
                isConnected: false,
                websocket: null,
                reconnectTimer: null,
                deviceId: 18080008,
                timeout:
                    1,
            }
        },
        methods: {
            setReconnectTimer: function () {
                let that = this;
                this.reconnectTimer = setInterval(function () {
                    that.initWebsocket();
                }, that.timeout * 1000);
            },
            clearReconnectTimer: function () {
                if (this.reconnectTimer != null) {
                    clearTimeout(this.reconnectTimer);
                    this.reconnectTimer = null;
                }
            },
            //需要添加断线重连方法
            initWebsocket() {
                let that = this;
                let url = "ws://127.0.0.1:7600/" + that.token + "/" + that.deviceId;
                if (that.websocket == null) {
                    that.websocket = new WebSocket(url);
                }
                that.websocket.onopen = function () {
                    this.isConnected = true;
                    Notification.success("已连接到服务器!");
                    that.websocket.send("hello");
                    that.clearReconnectTimer();
                };
                that.websocket.onmessage = function (evt) {
                    console.log(evt.data);
                };
                that.websocket.onclose = function (evt) {
                    this.isConnected = false;
                    Notification.error("连接断开,准备重连!");
                    // console.log(evt);
                    that.setReconnectTimer();
                };
                that.websocket.onerror = function (evt) {
                    Notification.error("连接期间发生错误!");
                    // console.log(evt);
                };
            },
            refreshData() {

            },
        },
        beforeDestroy() {
            this.clearReconnectTimer();
        },
        mounted() {
            this.initWebsocket();
        }

    }
</script>

<style scoped>
    .info-card {
        margin: 10px 0.25% 10px 0.25%;
        width: 49%;
        float: left;
        height: 700px;
    }

    .control-card {
        margin: 10px 0.25% 10px 0.25%;
        width: 49%;
        height: 700px;
        float: right;
    }

    .status-tr {
        text-align: center;
    }

    .operate-table td, .fuelTank-svg {
        cursor: pointer;
    }

    .status-bottom-div {
        width: 100%;
        border-radius: 5px;
    }

    .status-top-div {
        width: 100%;
        padding-bottom: 30px;
    }

    .control-card-body {
        width: 100%;
        height: 100%;
    }

    .status-left-div {
        text-align: center;
        width: 30%;
        float: left
    }

    .status-icon {
        font-size: 30px;
        color: #3a8ee6;
    }

    .online {
        color: #72f133 !important;
    }

    svg {
        -webkit-transform: rotate(-0.05deg);
        transform: rotate(-0.05deg);
    }

    .maintaining {
        color: #72f133 !important;
    }

    .offline {
        color: #b4bccc !important;
    }

    .right-status-header {
    }

    .operate-button {
        font-size: 45px;
        color: #34ce57;
    }

    .header-status-div {
        float: right;
    }

    .header-status-strong {
        margin-right: 20px;
    }

    text {
        font-size: 20px;
        text-anchor: middle; /* 文本水平居中 */
        dominant-baseline: middle; /* 文本垂直居中 */
    }
</style>