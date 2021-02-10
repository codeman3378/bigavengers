package org.wxw.hadoop.mapreduce.comparable;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * @author wxw
 */
public class FlowCompareReduce extends Reducer<FlowCompareBean,Text,Text,FlowCompareBean> {
    @Override
    protected void reduce(FlowCompareBean key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

        for (Text value:values) {
            context.write(value,key);
        }
    }
}
