package com.wzh.model;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Set;
import java.util.TreeSet;

public class StockInfo implements WritableComparable<StockInfo> {

    private static final DecimalFormat df = new DecimalFormat("######0.0000");

    private String symbol; //代码
    private String name;  //简称
    private double lastTrade; //最新价
    private double chg; //涨跌幅
    private double change; //涨跌额
    private double fiveMinChg; //5分钟涨跌幅
    private double volume; //成交量(手)
    private double turnover; //成交额（万元）
    private double turnoverRate; //换手率
    private double swing; //振幅
    private double qrr; //量比
    private double theCommittee; //委比
    private String peratio; //市盈率

    public StockInfo(){
    }

    public StockInfo(String line){
        parseLine(line);
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLastTrade() {
        return lastTrade;
    }

    public void setLastTrade(double lastTrade) {
        this.lastTrade = lastTrade;
    }

    public double getChg() {
        return chg;
    }

    public void setChg(double chg) {
        this.chg = chg;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }

    public double getFiveMinChg() {
        return fiveMinChg;
    }

    public void setFiveMinChg(double fiveMinChg) {
        this.fiveMinChg = fiveMinChg;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public double getTurnover() {
        return turnover;
    }

    public void setTurnover(double turnover) {
        this.turnover = turnover;
    }

    public double getTurnoverRate() {
        return turnoverRate;
    }

    public void setTurnoverRate(double turnoverRate) {
        this.turnoverRate = turnoverRate;
    }

    public double getSwing() {
        return swing;
    }

    public void setSwing(double swing) {
        this.swing = swing;
    }

    public double getQrr() {
        return qrr;
    }

    public void setQrr(double qrr) {
        this.qrr = qrr;
    }

    public double getTheCommittee() {
        return theCommittee;
    }

    public void setTheCommittee(double theCommittee) {
        this.theCommittee = theCommittee;
    }

    public String getPeratio() {
        return peratio;
    }

    public void setPeratio(String peratio) {
        this.peratio = peratio;
    }

    public static StockInfo parseLine(String line){
        String[] arr = line.split("[\t\n]");
        StockInfo stockInfo = new StockInfo();
        stockInfo.setSymbol(arr[0]);
        stockInfo.setName(arr[1]);
        stockInfo.setLastTrade(Double.parseDouble(arr[2]));
        stockInfo.setChg(Double.parseDouble(df.format(Double.parseDouble(arr[3].substring(0, arr[3].length() - 1)) / 100)));
        stockInfo.setChange(Double.parseDouble(arr[4]));
        stockInfo.setFiveMinChg(Double.parseDouble(df.format(Double.parseDouble(arr[5].substring(0, arr[5].length() - 1)) / 100)));
        stockInfo.setVolume(Double.parseDouble(arr[6]));
        stockInfo.setTurnover(Double.parseDouble(arr[7]));
        stockInfo.setTurnoverRate(Double.parseDouble(df.format(Double.parseDouble(arr[8].substring(0, arr[8].length() - 1)) / 100)));
        stockInfo.setSwing(Double.parseDouble(df.format(Double.parseDouble(arr[9].substring(0, arr[9].length() - 1)) / 100)));
        stockInfo.setQrr(Double.parseDouble(arr[10]));
        stockInfo.setTheCommittee(Double.parseDouble(arr[11]));
        stockInfo.setPeratio(arr[12]);
        return stockInfo;
    }

    public int compareTo(StockInfo o) {
        return Double.compare(this.chg, o.chg);
    }

    public void write(DataOutput out) throws IOException {
        out.writeUTF(symbol);
        out.writeUTF(name);
        out.writeDouble(lastTrade);
        out.writeDouble(chg);
        out.writeDouble(change);
        out.writeDouble(fiveMinChg);
        out.writeDouble(volume);
        out.writeDouble(turnover);
        out.writeDouble(turnoverRate);
        out.writeDouble(swing);
        out.writeDouble(qrr);
        out.writeDouble(theCommittee);
        out.writeUTF(peratio);
    }

    public void readFields(DataInput in) throws IOException {
        symbol = in.readUTF();
        name = in.readUTF();
        lastTrade = in.readDouble();
        chg = in.readDouble();
        change = in.readDouble();
        fiveMinChg = in.readDouble();
        volume = in.readDouble();
        turnover = in.readDouble();
        turnoverRate = in.readDouble();
        swing = in.readDouble();
        qrr = in.readDouble();
        theCommittee = in.readDouble();
        peratio = in.readUTF();
    }

    @Override
    public String toString() {
        return symbol + "\t" + name + "\t" + lastTrade + "\t"
                + chg + "\t" + change + "\t" + fiveMinChg +
                "\t" + volume + "\t" + turnover + "\t" + turnoverRate + "\t" + swing +
                "\t" + qrr + "\t" + theCommittee + "\t" + peratio;
    }

    @Override
    public int hashCode() {
        return Integer.parseInt(this.symbol);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null){
            return false;
        }
        if(this == obj){
            return true;
        }
        if(obj instanceof StockInfo){
            StockInfo that = (StockInfo) obj;
            if(that.getSymbol() == this.symbol){
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        StockInfo stockInfo1 = new StockInfo();
        StockInfo stockInfo2 = new StockInfo();
        StockInfo stockInfo3 = new StockInfo();
        stockInfo1.setChg(10);
        stockInfo2.setChg(100);
        stockInfo3.setChg(1);
        Set set = new TreeSet<StockInfo>();
        set.add(stockInfo1);
        set.add(stockInfo2);
        set.add(stockInfo3);
        System.out.println(set);



    }
}
