const Mock = require("mockjs");
const Laundry = 2;
const Admin = 1;
const queryUserAuthority = () => ({
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

        }
    ]
});


// Mock.mock("/user/queryUserAuthority", "get", queryUserAuthority);
