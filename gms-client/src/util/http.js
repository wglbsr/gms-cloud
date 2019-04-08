import axios from "axios";
import {Notification} from "element-ui";
import store from "../store";


let HOST = 'https://www.yn-iot.cn';
//开发环境
if (process.env.NODE_ENV !== 'production') {
    HOST = 'http://localhost:8010';
}

axios.defaults.baseURL = HOST;
window.axios = axios;
window.HOST = HOST;
const codeMessage = {
    201: "操作成功！",
    202: "您的请求已提交，请耐心等待服务器处理！",
    204: "删除成功！",
    400: "发出的请求有错误，服务器没有进行新建或修改数据的操作。",
    401: "用户没有权限（令牌、用户名、密码错误）。",
    403: "用户得到授权，但是访问是被禁止的。",
    404: "发出的请求针对的是不存在的记录，服务器没有进行操作。",
    406: "请求的格式不可得。",
    410: "请求的资源被永久删除，且不会再得到的。",
    422: "当创建一个对象时，发生一个验证错误。",
    500: "服务器发生错误，请检查服务器。",
    502: "网关错误。",
    503: "服务不可用，服务器暂时过载或维护。",
    504: "网关超时。"
};

axios.interceptors.request.use(
    config => {
        const requestConfig = config;
        //axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';
        // if (requestConfig.method == "post") {
        //     requestConfig.data = qs.stringify(requestConfig.data);
        //     requestConfig.headers['Content-Type'] = 'application/json';
        // }
        if (store.state.token) {
            requestConfig.headers.auth_token = `${store.state.token}`;
        }
        return requestConfig;
    },
    err => {
        Promise.reject(err);
    }
);

axios.interceptors.response.use(
    response => {
        if (response.data.status == 401) {
            Notification.error("请先登录!");
            window.location = "/";
        }
        let message = codeMessage[response.status];
        if (response.data && response.data.status) {
            message = codeMessage[response.data.status];
        }
        return response;
    },
    error => {
        let message = codeMessage[error.response.status];
        if (error.response.data.errorMsg) {
            message = error.response.data.errorMsg;
        }
        Notification.error({
            title: "系统提示",
            dangerouslyUseHTMLString: true,
            message,
            duration: 2000
        });
        return Promise.reject(error);
    }
);

export default axios;
