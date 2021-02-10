package org.wxw.hadoop.mapreduce.comparable;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * @author wxw
 */
public class FlowCompareMapper extends Mapper<LongWritable , Text ,FlowCompareBean ,Text> {
    //13470253144	180	180	360

    FlowCompareBean k = new FlowCompareBean();
    Text v = new Text();
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        String[] files = value.toString().split("\t");
        String phoneNumber = files[0];

        v.set(phoneNumber);

        k.set(Long.parseLong(files[1]),Long.parseLong(files[3]));
        context.write(k,v);

    }
}
