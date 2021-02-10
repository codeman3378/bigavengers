package org.wxw.hadoop.mapreduce.comparable;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class FlowCompareDriver {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        args = new String[] {"W:/10-workspace/java_space/Outputdir/output_flowsum",
                             "W:/10-workspace/java_space/Outputdir/output_compareflowsum"} ;
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        job.setJarByClass(FlowCompareDriver.class);
        job.setMapperClass(FlowCompareMapper.class);
        job.setReducerClass(FlowCompareReduce.class);

        job.setMapOutputKeyClass(FlowCompareBean.class);
        job.setMapOutputValueClass(Text.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FlowCompareBean.class);

        job.setPartitionerClass(ProvincePartitioner.class);
        job.setNumReduceTasks(5);

        FileInputFormat.setInputPaths(job,new Path(args[0]));
        FileOutputFormat.setOutputPath(job,new Path(args[1]));
        job.waitForCompletion(true);

    }
}
