package org.wxw.hadoop.mapreduce.sortjoin;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * @author wxw
 */
public class OneIndexDriver {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

        args = new String[]{"W:/10-workspace/java_space/Inputdir/sortjoin",
                            "W:/10-workspace/java_space/Outputdir/output_sortjoin1"};

        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        job.setJarByClass(OneIndexDriver.class);
        job.setMapperClass(OneIndexMapper.class);
        job.setReducerClass(OneIndexReduce.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        FileInputFormat.setInputPaths(job,new Path(args[0]));
        FileOutputFormat.setOutputPath(job,new Path(args[1]));

        job.waitForCompletion(true);
    }
}
