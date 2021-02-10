package org.wxw.hadoop.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author wxw
 */
public class HDFSClient {
    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
        // 1 mkdir
        HDFSClient.mkdir();
    }

    private static boolean mkdir() throws URISyntaxException, IOException, InterruptedException {
        // 1 获取客户端
        FileSystem fs = FileSystem.get(new URI("hdfs://hadoop102:9000"),new Configuration(),"hadoop");
        // 2 创建目录
        fs.mkdirs(new Path("/hadoop/hdfs"));
        // 3 关闭资源
        fs.close();
        System.out.print("mkdir Sucess!!!");
        return true;
    }

}
