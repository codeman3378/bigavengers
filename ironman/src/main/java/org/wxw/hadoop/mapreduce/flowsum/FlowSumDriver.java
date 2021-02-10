package org.wxw.hadoop.mapreduce.flowsum;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 *
 * @author wxw
 */
public class FlowSumDriver {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        args = new String[] {"W:/10-workspace/java_space/Inputdir/phone",
                             "W:/10-workspace/java_space/Outputdir/output_flowsum"} ;
        // 获取配置信息并封装任务
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        // job加载路径
        job.setJarByClass(FlowSumDriver.class);

        // 关联mapper 和 reduce
        job.setMapperClass(FlowSumMapper.class);
        job.setReducerClass(FlowSumReduce.class);

        // mapper 输出参数类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(FlowBean.class);

        // 最终    输出参数类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FlowBean.class);

        // 输入输出路径
        FileInputFormat.setInputPaths(job,new Path(args[0]));
        FileOutputFormat.setOutputPath(job,new Path(args[1]));

        // 提交job
        job.waitForCompletion(true);
    }
}
