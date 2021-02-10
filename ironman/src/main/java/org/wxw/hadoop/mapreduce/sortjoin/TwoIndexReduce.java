package org.wxw.hadoop.mapreduce.sortjoin;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;


/**
 * @author wxw
 */
public class TwoIndexReduce extends Reducer<Text,Text,Text,Text> {

    Text v = new Text();

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        // 1 拼接
        StringBuilder sb = new StringBuilder();
        for (Text value:values) {
            sb.append(value.toString().replace("\t", "-->")).append("\t");
        }
        // 2 封装
        v.set(sb.toString());
        // 3 写出
        context.write(key,v);
    }
}
