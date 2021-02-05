package org.wxw.parse;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.*;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
                List<String> urlList = getNextPage(htmlPage);
                downLoadByUrl(urlList,savePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 循环处理网页，获取需要的资源 "产品定义PDF" 的下载网页路径
     * @param htmlPage 可处理网页页面
     * @return List<String> 文件地址
     */
    private static List<String> getNextPage(HtmlPage htmlPage) {
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