package com.wzh.stockindustry;

import com.wzh.zookeeper.MyZookeeper;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.net.URI;

public class StockIndustryCodeParse {

    private static final String inputPath = "/input/stock/stock_index/";
    private static final String outputPath = "/user/hive/warehouse/stock.db/industry_code/";
    private static final String HDFS_PATH = "hdfs://120.78.205.73:9000";

    public static void main(String[] args) throws Exception {
        FileSystem fileSystem = FileSystem.get(new URI(HDFS_PATH), new Configuration());
        if(fileSystem.exists(new Path(outputPath))){
            fileSystem.delete(new Path(outputPath), true);
        }
        Configuration conf = new Configuration();
        Job job = new Job(conf, "stockindustry");
        job.setMapperClass(StockIndustryCodeMapper.class);
        // 设置输出类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);
        // 将输入的数据集分割成小数据块splites，提供一个RecordReder的实现
        job.setInputFormatClass(TextInputFormat.class);
        // 提供一个RecordWriter的实现，负责数据输出
        job.setOutputFormatClass(TextOutputFormat.class);
        FileInputFormat.addInputPath(job, new Path(inputPath));
        FileOutputFormat.setOutputPath(job, new Path(outputPath));
        if(job.waitForCompletion(true)){
            MyZookeeper.DoIndustry("120.78.205.73", 2181);
            MyZookeeper.createNode("stock", "industry_code");
        }
        System.exit(job.waitForCompletion(true)?0:1);

    }

}
