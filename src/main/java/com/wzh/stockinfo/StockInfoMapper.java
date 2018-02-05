package com.wzh.stockinfo;

import com.wzh.model.StockInfo;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.StringTokenizer;

public class StockInfoMapper extends Mapper<LongWritable, Text, StockInfo, Text> {

    public void map(LongWritable key, Text value, Context context) throws IOException,InterruptedException {
        String line = value.toString();
        StockInfo stockInfo = StockInfo.parseLine(line);
        context.write(stockInfo, new Text(""));
    }
}
