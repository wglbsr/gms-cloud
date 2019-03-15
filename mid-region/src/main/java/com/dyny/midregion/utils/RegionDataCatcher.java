package com.dyny.midregion.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dyny.common.utils.Utils;
import com.dyny.midregion.db.entity.Region;
import com.dyny.midregion.service.RegionService;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * @Auther: lane
 * @Date: 2019-02-19 16:00
 * @Description:
 * @Version 1.0.0
 */
public class RegionDataCatcher {
    private static final String BASE_URL = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2018/";
    private static final String START_URL = "index.html";
    private static String REGION_ID_REG_EXP_FIRST = "^\\d{2,12}(?=\\.html)";//后发零宽断言,抓取形如"*1101.html"里面的1101
    private static final String KEY_ID = "id";
    private static final String KEY_PARENT_ID = "parent_id";
    private static final String KEY_LEVEL = "level";
    private static final String KEY_NAME = "name";
    private static final String KEY_CHILDREN = "children";
    private static final int[] DIVISORS = {1, 2, 2, 2, 3};
    private static final String SAVE_LEVEL = "2";
    private static String[] ELE_LIST = {"tr.provincetr td a", "tr.citytr td a", "tr.countytr td a", "tr.towntr td a", "tr.villagetr td"};

    public static void main(String[] args) throws InterruptedException, IOException {
        RegionDataCatcher dataCatcher = new RegionDataCatcher();
        dataCatcher.start(null);
    }

    public void start(RegionService regionService) throws InterruptedException, IOException {
        JSONObject jsonObject = new JSONObject();
        int maxLevel = 4;
        int startLevel = 4;
        get(START_URL, startLevel, maxLevel, jsonObject, regionService);
        get("11/01/06/110106004.html", startLevel, maxLevel, jsonObject,regionService);
        Utils.File.fileWrite("/Users/lane/Desktop/region" + maxLevel + ".json", jsonObject.toJSONString());
    }

    private Document getHtmlContent(String url) throws InterruptedException {
        Thread.sleep(200);
        Document document = null;
        try {
            document = Jsoup.connect(BASE_URL + url)
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:49.0) Gecko/20100101 Firefox/49.0")
                    .header("Connection", "close")
                    .timeout(15000).get();
//            String temp = new String(document.toString().getBytes("UTF-8"), "GBK");
//            document = Jsoup.parse(temp);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return document;
    }

    //level      id       parentId        name
    //省        0 href      固定          0 text
    //市        0 text    parent_url     1 text
    //区        0 text    parent_url     1 text
    //镇、街道   0 text   parent_url     1 text
    //村委       0 text   parent_url     2 text
    private void get(String url, int level, int maxLevel, JSONObject regionJsonObj, RegionService regionService) throws InterruptedException {
        Document document = getHtmlContent(url);
        String parentId = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("."));
        // findStrByRegEx(url, REGION_ID_REG_EXP_FIRST);//url.substring(0, url.lastIndexOf("."));//
        Elements elements = document.select(ELE_LIST[level]);
        JSONArray jsonArray = new JSONArray();
        int divisor = DIVISORS[level];
        for (int i = 0; i < elements.size(); i += divisor) {
            Element idElement = elements.get(i);
            Element nameElement = elements.get(i + divisor - 1);

            int thisLevel = level;
            JSONObject jsonObject = new JSONObject();
            String id;
            if (level == 0) {
                String tempUrl = idElement.attr("href");
                id = tempUrl.substring(0, tempUrl.lastIndexOf("."));
            } else {
                id = idElement.text();
            }

            String name = nameElement.text();
            jsonObject.put(KEY_PARENT_ID, StringUtils.isEmpty(parentId) ? 0 : parentId);//**
            jsonObject.put(KEY_NAME, name);
            jsonObject.put(KEY_ID, id);
            jsonObject.put(KEY_LEVEL, thisLevel);

            if (SAVE_LEVEL.contains(level + "") && regionService != null) {
                Region region = new Region();
                region.setId(Long.valueOf(id));
                region.setParentId(Long.valueOf(parentId));
                region.setLevel(thisLevel);
                region.setName(name);
                regionService.saveOrUpdate(region);
            }
            if (thisLevel < maxLevel) {
                String nextUrl = (url.lastIndexOf("/") >= 0 ? url.lastIndexOf("/") : "") + idElement.attr("href");
                thisLevel++;
                get(nextUrl, thisLevel, maxLevel, jsonObject, regionService);
            }
            jsonArray.add(jsonObject);
        }
        regionJsonObj.put(KEY_CHILDREN, jsonArray);
    }


//
//    public static String findStrByRegEx(String content, String regEx) {
//        if (content == null || content.equals("")) {
//            return null;
//        }
//        Pattern p = Pattern.compile(regEx);
//        Matcher m = p.matcher(content);
//        if (m.find()) {
//            return m.group();
//        }
//        return null;
//    }

}
