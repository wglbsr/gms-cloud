import Vue from "vue";
import Vuex from "vuex";
import axios from "axios";
import _ from "lodash";

Vue.use(Vuex);
const isGranted = function (child) {
    let myRoles = JSON.parse(localStorage.getItem("myRoles"));
    if (myRoles && child.limit) {
        for (let index in child.limit) {
            for (let index2 in myRoles) {
                if (child.limit[index] == myRoles[index2].id) {
                    return true;
                }
            }
        }
        return false;
    } else {
        return true;
    }
};
const subTree = (parentNode, allMenus) => {
    const children = _.sortBy(
        _.filter(
            allMenus,
            o =>
                o.depth === parentNode.depth + 1 &&
                o.lft > parentNode.lft &&
                o.rgt < parentNode.rgt
        ),
        ["lft"]
    );
    for (let i = 0; i < children.length; i += 1) {
        const child = children[i];
        if (!isGranted(child)) {
            break;
        }
        const node = {...child, children: []};
        parentNode.children.push(node);
        subTree(node, allMenus);
    }
};


const store = new Vuex.Store({
    state: {
        userInfo: null,

        token: null,
        // 菜单列表
        menus: [],
        // 菜单树
        menuTree: [],
        // 权限点列表
        authorities: [],
        loading: false,
        // 选择日期范围
        dateRangePickerOptions: {
            shortcuts: [
                {
                    text: "最近一周",
                    onClick(picker) {
                        const end = new Date();
                        const start = new Date();
                        start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
                        picker.$emit("pick", [start, end]);
                    }
                },
                {
                    text: "最近一个月",
                    onClick(picker) {
                        const end = new Date();
                        const start = new Date();
                        start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
                        picker.$emit("pick", [start, end]);
                    }
                },
                {
                    text: "最近三个月",
                    onClick(picker) {
                        const end = new Date();
                        const start = new Date();
                        start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
                        picker.$emit("pick", [start, end]);
                    }
                }
            ]
        },
        // 选择日期
        datePickerOptions: {
            shortcuts: [
                {
                    text: "今天",
                    onClick(picker) {
                        picker.$emit("pick", new Date());
                    }
                },
                {
                    text: "昨天",
                    onClick(picker) {
                        const date = new Date();
                        date.setTime(date.getTime() - 3600 * 1000 * 24);
                        picker.$emit("pick", date);
                    }
                },
                {
                    text: "一周前",
                    onClick(picker) {
                        const date = new Date();
                        date.setTime(date.getTime() - 3600 * 1000 * 24 * 7);
                        picker.$emit("pick", date);
                    }
                }
            ]
        },
    },
    mutations: {
        setToken(state, newToken) {
            localStorage.setItem("auth_token", newToken);
            state.token = newToken;
        },
        setUserInfo(state, userInfo) {
            localStorage.setItem("userInfo", JSON.stringify(userInfo));
            state.userInfo = userInfo;
        },
        setRole(state, role) {
            localStorage.setItem("role", JSON.stringify(role));
            state.role = role;
        },
        setMenus(state, menus) {
            state.menus = menus;
        },
        setMenuTree(state, menuTree) {
            state.menuTree = menuTree;
        },
        setAuthorities(state, authorities) {
            state.authorities = authorities;
        }
    },
    actions: {
        // 重新从服务器读取用户信息
        reloadUserAuthority(context) {
            // return axios.get("/user/queryUserAuthority").then(response => {
            let response = {};
            let Laundry = 2;
            let Admin = 1;
            response.data = {
                menus: [
                    {
                        menuId: 1,
                        menuName: "根节点",
                        menuCode: "root",
                        link: "/root",
                        icon: "el-icon-menu",
                        lft: 1,
                        rgt: 14,
                        depth: 0
                    },
                    {
                        menuId: 2,
                        menuName: "首页",
                        menuCode: "dashboard",
                        link: "/dashboard",
                        icon: "el-icon-menu",
                        lft: 2,
                        rgt: 3,
                        depth: 1
                    },
                    {
                        menuId: 20,
                        menuName: "系统管理",
                        menuCode: "system",
                        link: "/system",
                        icon: "el-icon-menu",
                        lft: 4,
                        rgt: 9,
                        depth: 1
                    },
                    {
                        menuId: 21,
                        menuName: "个人设定",
                        menuCode: "profile",
                        link: "/system/profile",
                        icon: "el-icon-menu",
                        lft: 5,
                        rgt: 6,
                        depth: 2
                    },
                    {
                        menuId: 22,
                        menuName: "用户管理",
                        menuCode: "user",
                        link: "/system/user",
                        icon: "el-icon-menu",
                        lft: 5,
                        rgt: 6,
                        depth: 2,
                        limit: [Admin]
                    },
                    {
                        menuId: 23,
                        menuName: "角色管理",
                        menuCode: "role",
                        link: "/system/role",
                        icon: "el-icon-menu",
                        lft: 7,
                        rgt: 8,
                        depth: 2,
                        limit: [Admin]
                    },
                    {
                        menuId: 24,
                        menuName: "权限配置",
                        menuCode: "WeChat",
                        link: "/system/auth",
                        icon: "el-icon-menu",
                        lft: 7,
                        rgt: 8,
                        depth: 2,
                        limit: [Admin]

                    },
                    {
                        menuId: 30,
                        menuName: "油机系统",
                        menuCode: "gms",
                        link: "/gms",
                        icon: "el-icon-menu",
                        lft: 7,
                        rgt: 8,
                        depth: 1,
                    },
                    {
                        menuId: 32,
                        menuName: "基站列表",
                        menuCode: "station",
                        link: "/gms/station",
                        icon: "el-icon-menu",
                        lft: 7,
                        rgt: 8,
                        depth: 2,
                    }
                ]
            };
            const menus = response.data.menus;
            const rootMenu = _.find(menus, {menuCode: "root"});
            const rootMenuNode = {...rootMenu, children: []};
            subTree(rootMenuNode, menus);
            context.commit("setMenus", menus);
            context.commit("setMenuTree", rootMenuNode.children);
            context.commit("setAuthorities", response.data.authorities);
            // });
        },
        signOut(context) {
            localStorage.removeItem("auth_token");
            context.commit("setToken", null);
            context.commit("setMenus", []);
            context.commit("setAuthorities", []);
        }
    }
});

export default store;
