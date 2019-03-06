import Vue from "vue";
import ElementUI from "element-ui";
import _ from "lodash";
import axios from "./util/http";
import "@/style/app.scss";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import "./registerServiceWorker";



const bus = new Vue();
window.bus = bus;

const moment = require("moment");
require("moment/locale/zh-cn");


Vue.config.productionTip = false;
Vue.use(require("vue-moment"), {moment});
Vue.use(ElementUI);

Vue.prototype.$http = axios;
Vue.prototype._ = _;
Vue.filter('date-filter', function (value) {
    let d = new Date(value); //根据时间戳生成的时间对象
    let date = (d.getFullYear()) + "-" +
        (d.getMonth() + 1) + "-" +
        (d.getDate()) + " " +
        (d.getHours()) + ":" +
        (d.getMinutes()) ;
    return date;
});

let myFilter = Vue.filter('date-filter');
new Vue({
    router,
    store,
    render: h => h(App)
}).$mount("#app");
