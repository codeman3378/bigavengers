package org.wxw.hadoop.mapreduce.comparable;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

/**
 * @author wxw
 */
public class ProvincePartitioner extends Partitioner<FlowCompareBean, Text> {
    private final String PROVINCE_136 = "136";
    private final String PROVINCE_137 = "137";
    private final String PROVINCE_138 = "138";
    private final String PROVINCE_139 = "139";

    @Override
    public int getPartition(FlowCompareBean key, Text value, int i) {
        // 获取手机号前3位 归属地
        String phoneProvince = value.toString().substring(0,3);

        // 根据归属地进行分区
        if (StringUtils.equals(phoneProvince,PROVINCE_136)){
            return 0;
        }else if (StringUtils.equals(phoneProvince,PROVINCE_137)){
            return 1;
        }else if (StringUtils.equals(phoneProvince,PROVINCE_138)){
            return 2;
        }else if (StringUtils.equals(phoneProvince,PROVINCE_139)){
            return 3;
        }
        return 4;
    }
}
