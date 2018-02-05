package com.wzh.stockindustry;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.StringTokenizer;

public class StockIndustryMapper extends Mapper<LongWritable, Text, Text, LongWritable> {

    public void map(LongWritable key, Text value, Context context) throws IOException,InterruptedException {
        String[] line = value.toString().split("\t");
        if(line.length >= 18){
            Long code = Long.parseLong(line[1]);
            StringTokenizer industryToken = new StringTokenizer(line[17], "-");
            while(industryToken.hasMoreElements()){
                String industry = industryToken.nextToken();
                context.write(new Text(industry), new LongWritable(code));
            }
        }

    }
}
