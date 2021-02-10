package org.wxw.hadoop.mapreduce.sortjoin;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;

/*
需求: 将三个文件中单词进行统计，并输出每个单词在各个文件的个数
a.txt  aa bb            aa a.txt-->1 b.txt-->1 c.txt-->1
b.txt  aa bb ----->     aa a.txt-->1 b.txt-->1 c.txt-->1
c.txt  aa bb            aa a.txt-->1 b.txt-->1 c.txt-->1
分析：第一阶段将数据处理为 k,v = aa--a.txt, 1
     第二阶段将数据处理为 k,v = aa      ,a.txt-->1 b.txt-->1 c.txt-->1
*/

/**
 * @author wxw
 */
public class OneIndexMapper extends Mapper<LongWritable,Text,Text, IntWritable> {
    String name;
    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        FileSplit fs = (FileSplit)context.getInputSplit();
        name = fs.getPath().getName().toString();
    }

    Text k = new Text();
    IntWritable v = new IntWritable(1);
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

        // 1 获取一行
        String line = value.toString();
        // 2 分割
        String[] files = line.split(" ");
        // 3 封装
        for (String file:files) {
            k.set(file+"--"+name);
            context.write(k,v);
        }

    }
}
