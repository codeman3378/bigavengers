package org.wxw.hadoop.mapreduce.wordcount;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class WordCountReduce extends Reducer<Text, IntWritable, Text, IntWritable> {
    int sum;
    IntWritable v = new IntWritable(0);
    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        // 1 累加求和
        sum = 0;
        for (IntWritable value:values) {
            sum += value.get();
        }
        // 2 输出结果
        v.set(sum);
        context.write(key,v);
    }
}
