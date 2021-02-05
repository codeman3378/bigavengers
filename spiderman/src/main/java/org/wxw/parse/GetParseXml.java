package org.wxw.parse;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.util.Cookie;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import static org.wxw.utils.WebClint.getWebClint;
import static org.wxw.utils.WebDownload.downLoadByUrl;

/**
 * @author wxw
 */
public class GetParseXml {

    public static void main(String[] args) throws IOException {

        // 获取一个模拟浏览器
        WebClient webClient = getWebClint("");

        //设置请求转码
        URL url = new URL("http://tiaokuan.iachina.cn:8090/sinopipi/loginServlet/publicQuery.do");
        WebRequest webRequest = new WebRequest(url, HttpMethod.POST);
        webRequest.setCharset("GBK");

        /**
         * 模拟浏览器进行网页操作：根据网页进行动态的操作并加载到需要的页面
         */
        // 第一个原始网页
        HtmlPage page1 = webClient.getPage(webRequest);
        // 等待js加载完毕
        webClient.waitForBackgroundJavaScript(10000*3);
        // 根据form的名字获取页面表单
        HtmlForm form1 = page1.getFormByName("fm");
        HtmlSelect sOne = (HtmlSelect) form1.getSelectByName("prodTypeCodeOne");
        HtmlOption option1 = sOne.getOptionByValue("ProdTypeCode_03");

        //模拟选择事件 选择下拉选项并点击
        //第二页面：点击下拉选择项
        HtmlPage page2 = option1.click();

        //模拟点击事件 获取搜索按钮并点击
        //第三个页面：点击查询按钮
        HtmlForm form2 = page2.getFormByName("fm");
        HtmlButtonInput button2 = form2.getInputByValue("查 询");
        HtmlPage Page3 = button2.click();
        // 等待JS驱动dom完成获得还原后的网页
        webClient.waitForBackgroundJavaScript(1000);
        //输出跳转网页的地址
        System.out.println(Page3.getUrl().toString());

        // 循环获取后续url或者动作
        List<HtmlElement> inputList = Page3.getDocumentElement().getElementsByAttribute("input","class", "button");
        for (HtmlElement element : inputList) {
            String sourceStr = element.getOnClickAttribute();
            String targetUrl = getUrl(sourceStr);
            System.out.println(targetUrl);
        }

        HtmlInput button3 = (HtmlInput)Page3.getHtmlElementById("detailed9");
        // 等待JS驱动dom完成获得还原后的网页
        webClient.waitForBackgroundJavaScript(1000);
        HtmlPage Page4 = button3.click();
        System.out.println(Page4.getUrl().toString());
        System.out.println(Page4.asXml());


        webClient.close();
        System.out.println("Success!");
    }


    /**
     * 拼装有效的目标数据的url
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

    /**
     * 保险行业协会产品定义文件爬取主流程
     */
    public void parseDeal(){
        String url = "http://tiaokuan.iachina.cn:8090/sinopipi/loginServlet/publicQuery.do";
        try {
            HtmlPage htmlPage = getSourcePage(url);
            if (htmlPage.isHtmlPage()){
                List<String> urlList = getNextPage(htmlPage);
                downLoadByUrl(urlList,"D:/test");
            };
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 对原始页面进行解析读取后续所需网页原始
     * @param htmlPage
     * @return
     */
    private static List<String> getNextPage(HtmlPage htmlPage) throws IOException {
        List<String> urlList = new ArrayList<>();

        // 第一步 获取可解析网页的总页数 fm.pageCount.value
        String pageCountValue = htmlPage.getElementsByName("pageCount").get(0).getAttribute("value");
        int pageCount = 0;
        if(StringUtils.isNotEmpty(pageCountValue)){
            pageCount = Integer.parseInt(pageCountValue);
        }

        try {
            // 第二步 获取翻页方法
            HtmlForm form2 = htmlPage.getFormByName("fm");
            for (int i = 0; i < pageCount-1; i++) {
                form2.setAttribute("goToPage","1");
                HtmlButtonInput button2 = form2.getInputByValue("查 询");
                htmlPage = button2.click();

                // 第三步 循环遍历url
                List<HtmlElement> inputList = htmlPage.getDocumentElement().getElementsByAttribute("input","class", "button");
                for (HtmlElement element : inputList) {
                    String sourceStr = element.getOnClickAttribute();
                    String url = getUrl(sourceStr);
                    urlList.add(url);
                    System.out.println("原始元素："+sourceStr+"转换为目标url："+url);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return urlList;
    }

    /**
     * 获取一个可解析状态的动态网页，便于后续翻页解析
     * @param sourceUrl 需要解析的网页地址
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
        HtmlSelect sOne = (HtmlSelect) form1.getSelectByName("prodTypeCodeOne");
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
}