package org.wxw.hadoop.mapreduce.flowsum;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class FlowSumReduce extends Reducer<Text, FlowBean, Text, FlowBean> {

    FlowBean v = new FlowBean();
    @Override
    protected void reduce(Text k, Iterable<FlowBean> values, Context context) throws IOException, InterruptedException {
        long sum_upFlow = 0;
        long sum_downFlow = 0;
        long sum_allFlow = 0;
        for (FlowBean value:values) {
            sum_upFlow += value.getUpFlow();
            sum_downFlow += value.getDownFlow();
            sum_allFlow += sum_upFlow + sum_downFlow;
        }

        v.setUpFlow(sum_upFlow);
        v.setDownFlow(sum_downFlow);
        v.setSumFlow(sum_allFlow);

        context.write(k,v);
    }
}
