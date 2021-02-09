package org.wxw.parse;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.*;
import org.wxw.model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import static org.wxw.utils.WebClint.getWebClint;
import static org.wxw.utils.WebDownload.downLoadByUrl;

/**
 * @author wxw
 */
public class IaChainParseGetProduct {

    /**
     * 保险行业协会产品定义文件爬取主流程
     */
    public void parseDeal(){
        String sourceUrl = "http://tiaokuan.iachina.cn:8090/sinopipi/loginServlet/publicQuery.do";
        String savePath= "D:/test";
        try {
            HtmlPage htmlPage = getSourcePage(sourceUrl);
            if (htmlPage.isHtmlPage()){

                // 提交爬取信息提交到kafka进行数据持久化
                List<HtmlPage> htmlPages = getNextPage(htmlPage);
                for (HtmlPage htmlPage1: htmlPages) {
                    List<Product> productList = getProductData(htmlPage1);
                    kafkaProducer(productList);
                }
                // 保持产品条款文件到本地或者保存产品条款文件到hdfs

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static boolean kafkaProducer(List<Product> productList){
        Properties props = new Properties();
        //kafka 集群， broker-list
        props.put("bootstrap.servers", "hadoop102:9092");
        props.put("acks", "all");
        //重试次数
        props.put("retries", 1);
        //批次大小
        props.put("batch.size", 16384);
        //等待时间
        props.put("linger.ms", 1);
        //RecordAccumulator 缓冲区大小
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(props);
        for (Product product: productList) {
            kafkaProducer.send( new ProducerRecord<String, String>("product",product.toString()));
        }
        kafkaProducer.close();
        return true;
    }
    private static List<Product> getProductData(HtmlPage htmlPage){
        List<Product> productList = new ArrayList<>();
        Product product = new Product();

        // 第一步获取产品定义下载url
        // <object data="../03/cf0c5e13-9bac-40b4-9b43-83875b69a6b3_TERMS.PDF" type="application/pdf" width="900" height="1210">
        String url = htmlPage.getElementsByName("SRC").get(0).getAttribute("value");
        url = "http://www.iachina.cn/IC/tkk/"+url;
        product.setClauseWebUrl(url);
        // 第二步获取产品描述信息
        try {
            htmlPage = htmlPage.getDocumentElement().getElementsByAttribute("td","class","mingxi").get(0)
                    .getElementsByTagName("a").get(0).click();
            HtmlTable htmlTable = (HtmlTable)htmlPage.getDocumentElement().getElementsByAttribute("table","class","biaoge").get(0);
            for (HtmlTableRow row : htmlTable.getRows()) {

                if (row.getCell(0).asText().contains("公司名称")){
                    product.setCompanyName(row.getCell(1).asText());
                } else if(row.getCell(0).asText().contains("产品名称")){
                    product.setProductName(row.getCell(1).asText());
                } else if(row.getCell(0).asText().contains("产品类别")){
                    product.setProductType(row.getCell(1).asText());
                } else if(row.getCell(0).asText().contains("设计类型")){
                    product.setDesignType(row.getCell(1).asText());
                } else if(row.getCell(0).asText().contains("产品特殊属性")){
                    product.setSpecialFlag(row.getCell(1).asText());
                } else if(row.getCell(0).asText().contains("承保方式")){
                    product.setSaleType(row.getCell(1).asText());
                } else if(row.getCell(0).asText().contains("保险期间类型")){
                    product.setInsuranCecycle(row.getCell(1).asText());
                } else if(row.getCell(0).asText().contains("产品交费方式")){
                    product.setPayintv(row.getCell(1).asText());
                } else if(row.getCell(0).asText().contains("产品条款文字编码")){
                    product.setClauseTextCode(row.getCell(1).asText());
                } else if(row.getCell(0).asText().contains("产品销售状态")){
                    product.setSaleStatus(row.getCell(1).asText());
                }
            }
            productList.add(product);
            System.out.println("产品详细信息："+product.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return productList;
    }

    /**
     * 循环处理网页，获取需要的资源 "产品定义PDF" 的下载网页路径
     * @param htmlPage 可处理网页页面
     * @return List<String> 文件地址
     */
    private static List<HtmlPage> getNextPage(HtmlPage htmlPage) {
        List<HtmlPage> htmlPages = new ArrayList<>();
        // 第一步 获取可解析网页的总页数 fm.pageCount.value
        String pageCountValue = htmlPage.getElementsByName("pageCount").get(0).getAttribute("value");
        int pageCount = 0;
        if(StringUtils.isNotEmpty(pageCountValue)){
            pageCount = Integer.parseInt(pageCountValue);
        }

        try {

            for (int i = 1; i <= pageCount; i++) {

                // 第二步 获取翻页方法
                HtmlPage htmlPageA = htmlPage;
                System.out.println("产品列表翻页至第"+i+"页\n"+"网页内容："+htmlPageA.asXml());

                //HtmlForm form2 = htmlPageA.getFormByName("fm");
                // 设置跳转函数对应参数
                //<a href="#" onclick="toPage()"><img src="/sinopipi/images/queryimages/icon_15.png" align="absmiddle" title="跳转"/></a>
                //form2.setAttribute("pageNo",String.valueOf(3));

                // 跳转方法1 执行跳转函数 nextPage toPage
                String hrefValue = "nextPage()";
                ScriptResult sr = htmlPageA.executeJavaScript(hrefValue);
                htmlPageA =(HtmlPage)sr.getNewPage();

                System.out.println("产品列表翻页至第"+i+"页\n"+"网页内容："+htmlPageA.asXml());

                // 跳转方法1 执行跳转函数
                //htmlPageA = htmlPage.getDocumentElement().getElementsByAttribute("a","onclick","toPage()").get(0).click();

                // 第三步 获取采集目标数据网页
                List<HtmlInput> toPageList = htmlPageA.getFormByName("fm").getInputsByValue("详细信息");
                for (HtmlElement element : toPageList) {
                    HtmlButtonInput button3 = (HtmlButtonInput)element;
                    htmlPages.add(button3.click());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return htmlPages;
    }

    /**
     * 模拟浏览器进行操作，完成动态加载后，获得可后续循环处理的网页
     * @param sourceUrl 需要解析的原始网页地址
     * @return 可处理浏览器网页
     */
    private static HtmlPage getSourcePage(String sourceUrl) throws IOException {
        // 获取一个模拟浏览器
        WebClient webClient = getWebClint("");

        //设置请求转码
        URL url = new URL(sourceUrl);
        WebRequest webRequest = new WebRequest(url, HttpMethod.POST);
        webRequest.setCharset("GBK");

        // 第一个原始网页
        HtmlPage page1 = webClient.getPage(webRequest);
        // 等待js加载完毕
        webClient.waitForBackgroundJavaScript(10000*3);
        // 根据form的名字获取页面表单
        HtmlForm form1 = page1.getFormByName("fm");
        HtmlSelect sOne = form1.getSelectByName("prodTypeCodeOne");
        HtmlOption option1 = sOne.getOptionByValue("ProdTypeCode_03");

        //模拟选择事件 选择下拉选项并点击
        //第二页面：点击下拉选择项
        HtmlPage page2 = option1.click();

        //模拟点击事件 获取搜索按钮并点击
        //第三个页面：点击查询按钮
        HtmlForm form2 = page2.getFormByName("fm");
        HtmlButtonInput button2 = form2.getInputByValue("查 询");
        HtmlPage page3 = button2.click();
        // 等待JS驱动dom完成获得还原后的网页
        webClient.waitForBackgroundJavaScript(1000);
        //输出跳转网页的地址
        System.out.println(page3.getUrl().toString());
        System.out.println(page3.asXml());
        return page3;
    }

    /**
     * 根据爬取有效的网页元素、属性、属性值等信息，拼装有效的目标数据的url
     * @param str 原始网页元素
     * @return 目标数据url
     */
    private static String getUrl(String str){
        String url = null;
        try {
            if(StringUtils.isNotEmpty(str)){
                str = str.substring(str.indexOf("(")+1,str.indexOf(")")).replace("'","");
                String [] files = str.split(",");
                url = files[2].replace("01","03")+files[0]+"_TERMS.PDF";
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return url;
    }
}