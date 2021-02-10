package org.wxw.hadoop.mapreduce.sortjoin;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;


/**
 * @author wxw
 */
public class TwoIndexDriver {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

        args = new String[] {"W:/10-workspace/java_space/Outputdir/output_sortjoin1",
                             "W:/10-workspace/java_space/Outputdir/output_sortjoin2"};

        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        job.setJarByClass(TwoIndexDriver.class);
        job.setMapperClass(TwoIndexMapper.class);
        job.setReducerClass(TwoIndexReduce.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.setInputPaths(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        job.waitForCompletion(true);
    }
}
