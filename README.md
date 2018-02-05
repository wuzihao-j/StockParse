股票分析
=
爬虫
-
    resource/a_stock.py爬取A股数据 
    
    代码 简称 最新价 涨跌幅 涨跌额 5分钟涨幅 成交量(手) 成交额(万元) 换手率 振幅 量比 委比 市盈率
    并存放到hdfs
    
    resource/StockInfo爬取每个股票的详细信息
    
    * 用此命令每天调度a_stock.py:*/30 16-20 * * 1-5 /usr/python3/Python-3.6.4/python /home/paper/a_stock.py

分析
-
    1,stockInfo包对A股数据清洗,执行后把数据存放到/user/hive/warehouse/stock.db/stockInfo中.已当前日期为分区
    2,stockindex对每个股票的详细信息清洗
    3,stockindustr根据对股票数据清洗后的结果，得出每个股票的行业数据
    每个mapreduce用zookeep进行管理,例如当2执行完后在zookeeper生产节点/stock/stock_index,
    当检测出具有此节点后，3执行，成功后产生/stock/industry节点，
    当两个节点都存在可以生成/stock/stock_industry_parse节点，此时可以启动新的mapreduce去分析数据
    

输出到MYSQL
-




