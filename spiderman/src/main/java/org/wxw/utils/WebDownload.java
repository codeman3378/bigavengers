package org.wxw.utils;

import com.sun.istack.internal.NotNull;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.*;
import java.util.List;

import static org.wxw.io.Input.readInputStream;
import static org.wxw.io.Output.saveFileByBate;

/**
 * 网页下载工具箱
 * @author wxw
 */
public class WebDownload {

    /**
     * 根据目标url下载文件到本地
     * @param urlStr 目标url
     * @param fileName 文件名称
     * @param savePath 文件路径
     * @throws IOException 异常抛出
     */
	public static boolean  downLoadByUrl(@NotNull String urlStr, @NotNull String fileName, @NotNull String savePath)  {
        URL url;
        HttpURLConnection conn = null;
        InputStream inputStream = null;
        try {
            url = new URL(urlStr);
            conn = (HttpURLConnection)url.openConnection();
            //设置超时间为3秒
            conn.setConnectTimeout(5*1000);
            //防止屏蔽程序抓取而返回403错误
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            //得到输入流
            inputStream = conn.getInputStream();
            //读取输入字节流数组
            byte[] getData = readInputStream(inputStream);
            //输出文件到指定位置
            saveFileByBate(savePath,fileName,getData);
            System.out.println("info:"+url+" download success");
            inputStream.close();
            conn.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                inputStream.close();
                conn.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return true;

    }


    /**
     * 批量下载 根据目标url下载文件到本地
     * @param urlList 目标url
     * @param savePath 文件路径
     * @throws IOException 异常抛出
     */
    public static void  downLoadByUrl(List<String> urlList, String savePath){
        int i = 0;
        for (String url:urlList) {
            if(StringUtils.isNotEmpty(url)){
                String fileName = url.replace("/","-").replace(":",String.valueOf(i++));
                downLoadByUrl(url,fileName,savePath);
            }
        }
    }


    public static void main(String[] args) {

	    String urlStr = "http://www.iachina.cn/IC/tkk/03/ebe1cf41-76a4-4fbd-834b-85acd1f8c9c6_TERMS.PDF";
	    String fileName = "test.pdf";
	    String filePath = "D:/test";
		try{
            downLoadByUrl(urlStr,fileName,filePath);
        }catch (Exception e) {
        }
	}

}
