#!/usr/bin/env python3.6
# coding=utf-8


import urllib
import urllib.request
import re
import random
import time
import os
user_agent = ["Mozilla/5.0 (Windows NT 10.0; WOW64)", 'Mozilla/5.0 (Windows NT 6.3; WOW64)',
              'Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.64 Safari/537.11',
              'Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; rv:11.0) like Gecko',
              'Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.95 Safari/537.36',
              'Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; rv:11.0) like Gecko)',
              'Mozilla/5.0 (Windows; U; Windows NT 5.2) Gecko/2008070208 Firefox/3.0.1',
              'Mozilla/5.0 (Windows; U; Windows NT 5.1) Gecko/20070309 Firefox/2.0.0.3',
              'Mozilla/5.0 (Windows; U; Windows NT 5.1) Gecko/20070803 Firefox/1.5.0.12',
              'Opera/9.27 (Windows NT 5.2; U; zh-cn)',
              'Mozilla/5.0 (Macintosh; PPC Mac OS X; U; en) Opera 8.0',
              'Opera/8.0 (Macintosh; PPC Mac OS X; U; en)',
              'Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.8.1.12) Gecko/20080219 Firefox/2.0.0.12 Navigator/9.0.0.6',
              'Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; Win64; x64; Trident/4.0)',
              'Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; Trident/4.0)',
              'Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; InfoPath.2; .NET4.0C; .NET4.0E)',
              'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Maxthon/4.0.6.2000 Chrome/26.0.1410.43 Safari/537.1 ',
              'Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; InfoPath.2; .NET4.0C; .NET4.0E; QQBrowser/7.3.9825.400)',
              'Mozilla/5.0 (Windows NT 6.1; WOW64; rv:21.0) Gecko/20100101 Firefox/21.0 ',
              'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.92 Safari/537.1 LBBROWSER',
              'Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0; BIDUBrowser 2.x)',
              'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.11 (KHTML, like Gecko) Chrome/20.0.1132.11 TaoBrowser/3.0 Safari/536.11']
stock_total=[] 
dirName = ("/home/paper/stock_data")


url='http://quote.stockstar.com/stock/stock_index.htm'
request=urllib.request.Request(url=url,headers={"User-Agent":random.choice(user_agent)})
try:
    response=urllib.request.urlopen(request)
except urllib.error.HTTPError as e:            
    print('page=1','',e.code)
except urllib.error.URLError as e:
    print('page=1','',e.reason)
pagecontent=response.read().decode('gbk')       
print('get index')
pattern=re.compile('<ul.*?id="index_data_0">(.*?)</ul>', re.S)
allStock=re.findall(pattern,str(pagecontent))
pattern=re.compile('>(\d*?)<', re.S)
allStockCode=re.findall(pattern,str(allStock))

stockLast = allStockCode[:]
for code in allStockCode:
    if code == '':
        stockLast.remove(code)

allStockInfo = []
url = "http://stock.quote.stockstar.com/corp_"
for code in stockLast:
    infoUrl = url + code + ".shtml"
    request=urllib.request.Request(url=infoUrl,headers={"User-Agent":random.choice(user_agent)})
    try:
        response=urllib.request.urlopen(request, timeout=5)
        infoContent=response.read().decode('gbk')       
        pattern=re.compile('<div class="con gsjj_wrap">(.*?)</div>', re.S)
        stockInfo=re.findall(pattern,str(infoContent))
        stockInfo = stockInfo[0].replace("\\n", "").replace("\\r", "").replace(" ", "")
        pattern=re.compile('>(.*?)<')
        stockInfo=re.findall(pattern,str(stockInfo))
    except urllib.error.HTTPError as e:            
        print('page=1','',e.code)
    except urllib.error.URLError as e:
        print('page=1','',e.reason)
    except BaseException as e:
        print(e)

    stockInfoLast = stockInfo[:]
    if len(stockInfo) > 0:
        for word in stockInfo:
            if word == '':
                stockInfoLast.remove(word)          
        if len(stockInfoLast) >= 18:
            stockInfoLast = stockInfoLast[0:18]
    fp = open(os.path.join(dirName, "stock_info"), 'a+', encoding='utf-8')
    fp.write("股市代码" + "\t" + code + "\t")
    for data in stockInfoLast:
        fp.write(data + "\t")
    fp.write("\n")
    time.sleep(random.randrange(200,400) / 1000)
