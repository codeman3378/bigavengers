package org.wxw.hadoop.mapreduce.flowsum;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * @author wxw
 */
public class FlowSumMapper extends Mapper<LongWritable, Text, Text, FlowBean> {
    Text k = new Text();
    FlowBean v = new FlowBean();
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        String [] files = line.split("\t");

        String phone = files[1];
        Long upflow = Long.parseLong(files[files.length-3]);
        Long downflow = Long.parseLong(files[files.length-2]);

        k.set(phone);
        v.setUpFlow(upflow);
        v.setDownFlow(downflow);

        context.write(k,v);

    }
}

/*
* 需求：获取每个手机号的   上行流量      下行流量      总流量
*      phone           upflow       downflow    sumflow
*
* 解析：
* mapper key：手机号  value：bean(upflow,downflow,sumflow)
* reduce key：手机号  value：bean(upflow,downflow,sumflow)
*
* */
