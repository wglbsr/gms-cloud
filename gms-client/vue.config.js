module.exports = {
    // 输出文件目录
    // baseUrl:'',
    outputDir: 'dist',
    productionSourceMap: false,
    devServer: {
        port: 8098,
        // proxy: "https://www.yn-iot.cn"//
    },
    // css相关配置
    css: {
        // 是否使用css分离插件 ExtractTextPlugin
        extract: true,
        // 开启 CSS source maps?
        sourceMap: false,
        // css预设器配置项
        loaderOptions: {},
        // 启用 CSS modules for all css / pre-processor files.
        modules: false
    },

};

