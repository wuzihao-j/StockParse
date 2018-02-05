package com.wzh.stockindex;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.StringTokenizer;

public class StockIndexMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

    public void map(LongWritable key, Text value, Context context) throws IOException,InterruptedException {
        String line = value.toString();
        StringTokenizer tokenizerArticle = new StringTokenizer(line, "\n");
        while(tokenizerArticle.hasMoreTokens()){
            StringTokenizer stringTokenizer = new StringTokenizer(tokenizerArticle.nextToken());
            String name = stringTokenizer.nextToken();
            int  score = Integer.parseInt(stringTokenizer.nextToken());
            Text text = new Text(name);
            context.write(text, new IntWritable(score));
        }
    }
}
