package com.wzh.stockindustry;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class StockIndustryReducer extends Reducer<LongWritable, Text, Text, IntWritable> {

    private int sum;
    private int count;

    public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException,InterruptedException {
        for(IntWritable val:values){
            sum += val.get();
            count++;
        }
        context.write(key, new IntWritable(sum / count));
    }


}