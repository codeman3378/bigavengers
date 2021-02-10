package org.wxw.hadoop.mapreduce.sortjoin;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * @author wxw
 */
public class TwoIndexMapper extends Mapper<LongWritable,Text, Text,Text> {

    Text k = new Text();
    Text v = new Text();
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        // 1 获取一行
        String line = value.toString();
        // 2 分割
        String[] files = line.split("--");
        // 3 封装
        k.set(files[0]);
        v.set(files[1]);
        // 4 写出
        context.write(k,v);
    }
}
