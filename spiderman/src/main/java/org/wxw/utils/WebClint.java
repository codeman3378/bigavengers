package org.wxw.utils;

import com.gargoylesoftware.htmlunit.*;
/**
 * @author wxw
 */
public class WebClint {

    public static WebClient getWebClint(String browserVersion) {
        // 模拟一个浏览器
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        // 设置webClient的相关参数
        webClient.setCssErrorHandler(new SilentCssErrorHandler());
        // 设置ajax
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        // 设置支持js
        // 注意动态页面爬取大概率需要支持js静态页面不需要
        webClient.getOptions().setJavaScriptEnabled(true);
        //CSS渲染禁止
        webClient.getOptions().setCssEnabled(false);
        //超时时间
        webClient.getOptions().setTimeout(50000);
        //设置js抛出异常:false
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        //允许重定向
        webClient.getOptions().setRedirectEnabled(true);
        //允许cookie
        webClient.getCookieManager().setCookiesEnabled(true);
        return webClient;
    }
}
