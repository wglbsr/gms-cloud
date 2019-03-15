package com.dyny.midregion.utils;

import com.dyny.midregion.db.entity.Region;
import com.dyny.midregion.service.RegionService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: lane
 * @Date: 2019-02-19 16:00
 * @Description:
 * @Version 1.0.0
 */
public class RegionDataCatcher {
    private static final String BASE_URL = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2018/";
    private static final int[] DIVISORS = {1, 2, 2, 2, 3};
    private static final String SAVE_LEVEL = "234";
    private static String[] ELE_LIST = {"tr.provincetr td a", "tr.citytr td a", "tr.countytr td a", "tr.towntr td a", "tr.villagetr td"};

    //"11,12,13,14,15,21,22,23,31,32,33,34,35,36,37,41,42,43,44,45,46,50,51,52,53,54,61,62,63,64,65";
    //已完成{44,11}
    public static void main(String[] args) throws IOException {
        RegionDataCatcher dataCatcher = new RegionDataCatcher();
        dataCatcher.start(null);
    }


    public void start(RegionService regionService) throws IOException {
        int maxLevel = 4;
        int startLevel = 1;
        String startUrl = "11.html";
        get(startUrl, startLevel, maxLevel, regionService);
//        get("44.html", startLevel, maxLevel, regionService);
    }

    private Document getHtmlContent(String url) throws InterruptedException {
        Thread.sleep(100);
        Document document = null;
        try {
            document = Jsoup.connect(BASE_URL + url)
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:49.0) Gecko/20100101 Firefox/49.0")
                    .header("Connection", "close")
                    .timeout(30000).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return document;
    }

    //regionService用于保存操作
    private void get(String url, int level, int maxLevel, RegionService regionService) {
        Document document = null;
        try {
            document = getHtmlContent(url);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (document == null || regionService == null) {
            return;
        }
        String parentId = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("."));
        Elements elements = document.select(ELE_LIST[level]);
        int divisor = DIVISORS[level];
        List<Region> regionList = new ArrayList<>();
        for (int i = 0; i < elements.size(); i += divisor) {
            Element idElement = elements.get(i);
            Element nameElement = elements.get(i + divisor - 1);
            int thisLevel = level;//级别
            String id;//id
            String name = nameElement.text(); //地区名称
            parentId = parentId.matches("[0-9]+") ? parentId : "0";//父级id
            if (level == 0) {
                String tempUrl = idElement.attr("href");
                id = tempUrl.substring(0, tempUrl.lastIndexOf("."));
            } else {
                id = idElement.text();
            }
            if (SAVE_LEVEL.contains(("" + thisLevel))) {
                Region region = new Region();
                region.setId(Long.valueOf(id));
                region.setParentId(Long.valueOf(parentId));
                region.setLevel(thisLevel);
                region.setName(name);
                regionList.add(region);
                System.out.println("[" + id + "]");
            }
            if (thisLevel < maxLevel) {
                String nextUrl = (url.lastIndexOf("/") >= 0 ? url.substring(0, url.lastIndexOf("/")) + "/" : "") + idElement.attr("href");
                thisLevel++;
                get(nextUrl, thisLevel, maxLevel, regionService);
            }
        }
        System.out.println("保存结果:" + regionService.saveOrUpdateBatch(regionList));
    }
}
