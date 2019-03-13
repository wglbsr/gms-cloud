package com.dyny.common.utils;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: lane
 * @Date: 2019-03-13 15:23
 * @Description:
 * @Version 1.0.0
 */
public class ControllerGenerator {
    private static final String BASE_PATH = "/Users/lane/IdeaProjects/gms-cloud/base-common/";
    private static final String TEMPLATE_PATH = BASE_PATH + "src/main/java/com/dyny/common/utils/template";
    private static final String FILE_OUTPUT = BASE_PATH + "src/main/java/com/dyny/common/utils/output";

    public static void main(String[] args) {
        // step1 创建freeMarker配置实例
        Configuration configuration = new Configuration();
        Writer out = null;
        try {
            // step2 获取模版路径
            configuration.setDirectoryForTemplateLoading(new File(TEMPLATE_PATH));
            // step3 创建数据模型
            Map<String, Object> dataMap = new HashMap<>();
            String classPath = "com.dyny.common.utils.output";
            dataMap.put("classPath", classPath);
            String entityName = "Carrier";
            dataMap.put("entityName", entityName);
            dataMap.put("entityNameLower", entityName.toLowerCase());
            dataMap.put("servicePath", "userName");
            dataMap.put("entityPath", "password");
            Template template = configuration.getTemplate("controller.tpl");
            File docFile = new File(FILE_OUTPUT + File.separator + entityName + ".java");
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(docFile)));
            template.process(dataMap, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != out) {
                    out.flush();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }
}
