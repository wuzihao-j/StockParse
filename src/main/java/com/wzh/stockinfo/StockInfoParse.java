package com.wzh.stockinfo;

import com.wzh.common.MyDateFormat;
import com.wzh.common.Stock;
import com.wzh.hive.IndustryParse;
import com.wzh.model.StockInfo;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class StockInfoParse {


    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = new Job(conf, "stockindex");
        job.setMapperClass(StockInfoMapper.class);
        // 设置输出类型
        job.setOutputKeyClass(StockInfo.class);
        job.setOutputValueClass(Text.class);
        // 将输入的数据集分割成小数据块splites，提供一个RecordReder的实现
        job.setInputFormatClass(TextInputFormat.class);
        // 提供一个RecordWriter的实现，负责数据输出
        job.setOutputFormatClass(TextOutputFormat.class);

//        String currentDate = MyDateFormat.getDateStr();
        String currentDate = "2018-02-08";
        String output = Stock.HIVE_PATH + "stock_info/" + currentDate;
        FileSystem fileSystem = FileSystem.get(new URI(Stock.HDFS_PATH), new Configuration());
        if(fileSystem.exists(new Path(output))){
            fileSystem.delete(new Path(output), true);
        }
        FileInputFormat.addInputPath(job, new Path(Stock.INPUT_PATH + "a_stock/" + currentDate));
        FileOutputFormat.setOutputPath(job, new Path(output));

        ZooKeeper zk = new ZooKeeper("120.78.205.73:2181", 60000, null);
        Stat exists = zk.exists("/stock/industry_code", null);
        if(job.waitForCompletion(true) && exists != null){
            IndustryParse.run(MyDateFormat.getDateStr());
        }

        System.exit(job.waitForCompletion(true)?0:1);
    }

}
