package com.dyny.common.utils;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @Author wanggl(lane)
 * @Description //TODO  代码生成工具
 * @Date 下午4:33 2018/12/4
 **/
public class MyBatisPlusGenerator {
    public static void main(String[] args) throws SQLException {
        Map<String, String> table = new HashMap<>();

        //直接在这里添加表名和前缀即可,key为表名,value为前缀
        table.put("gms_customer", "gms_");

        MyBatisPlusGenerator myBatisPlusGenerator = new MyBatisPlusGenerator();
        myBatisPlusGenerator.generateCode(table);
    }


    private void generateCode(Map<String, String> table) {
        //1. 全局配置
        GlobalConfig config = new GlobalConfig();
        config.setActiveRecord(true) // 是否支持AR模式
                .setAuthor("wanggl") // 作者
                .setOutputDir("/Users/lane/IdeaProjects/gms-cloud/biz-g1/src/main/java") // 生成路径
                .setFileOverride(true)  // 文件覆盖
                .setIdType(IdType.AUTO) // 主键策略
                .setServiceName("%sService")  // 设置生成的service接口的名字
                .setBaseResultMap(true)//生成基本的resultMap
                .setBaseColumnList(true);//生成基本的SQL片段

        //2. 数据源配置
        DataSourceConfig dsConfig = new DataSourceConfig();
        dsConfig.setDbType(DbType.MYSQL)  // 设置数据库类型
                .setDriverName("com.mysql.jdbc.Driver")
                .setUrl("jdbc:mysql://120.79.91.131:3306/gms_cloud?useUnicode=true&useSSL=false&characterEncoding=utf8")
                .setUsername("nacos_admin")
                .setPassword("nacos_admin");

        //4. 包名策略配置
        PackageConfig pkConfig = new PackageConfig();
        pkConfig.setParent("com.dyny.bizg1")
                .setMapper("db.dao")//dao
                .setService("service")//servcie
                .setServiceImpl("service.impl")//
                .setController("controller")//controller
                .setXml("db.xml")
                .setEntity("db.entity");//dao.xml


        Iterator<String> keySet = table.keySet().iterator();


        while (keySet.hasNext()) {
            AutoGenerator ag = new AutoGenerator();
            ag.setGlobalConfig(config)
                    .setDataSource(dsConfig)
                    .setPackageInfo(pkConfig);
            //3. 策略配置globalConfiguration中
            String tableName = keySet.next();
            String prefix = table.get(tableName);
            StrategyConfig stConfig = new StrategyConfig();
            stConfig.setCapitalMode(true) //全局大写命名
                    .setNaming(NamingStrategy.underline_to_camel) // 数据库表映射到实体的命名策略
                    .setColumnNaming(NamingStrategy.underline_to_camel) // 字段名映射策略
                    .setFieldPrefix("s_","b_","i_","d_")
                    .setTablePrefix(prefix)
                    .setInclude(tableName)// 目标表
                    .setRestControllerStyle(true)
                    .setSuperControllerClass("com.dyny.common.utils.BaseController")
                    .setSuperMapperClass("com.dyny.common.utils.CommonMapper");//默认为BaseMapper
            //6. 执行
            ag.setStrategy(stConfig);
            ag.execute();
        }


    }

}
