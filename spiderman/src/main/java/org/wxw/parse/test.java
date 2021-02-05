package org.wxw.parse;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;

public class test {
    public static void main(String[] args) {


        GetParseXml d = new GetParseXml();
        d.parseDeal();
    }


    public void testhtmlunit() throws IOException {
        //创建webclient
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        //htmlunit 对css和javascript的支持不好，所以请关闭之
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);
        HtmlPage page = (HtmlPage)webClient.getPage("http://www.baidu.com/");
        //获取搜索输入框并提交搜索内容
        HtmlInput input = (HtmlInput)page.getHtmlElementById("kw");
        System.out.println(input.toString());
        input.setValueAttribute("雅蠛蝶");
        System.out.println(input.toString());
        //获取搜索按钮并点击
        HtmlInput btn = (HtmlInput)page.getHtmlElementById("su");
        HtmlPage page2 = btn.click();
        //输出新页面的文本
        System.out.println(page2.asText());
        System.out.println("怎么会乱码");
    }
}
