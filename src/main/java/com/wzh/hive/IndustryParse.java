package com.wzh.hive;

import com.wzh.common.MyShell;

import java.io.IOException;

public class IndustryParse {

    public static void run(String partition) {
        String sql = "insert overwrite table stock.industry_info PARTITION('"+partition+"') " +
                " select IC.industry, avg(SI.chg), avg(SI.change), sum(volume), sum(turnover)\n " +
                " from stock.stock_info SI left join stock.industry_code IC on SI.symbol = IC.code\n " +
                " group by IC.industry";
        try {
            MyShell.execHQL(sql);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
