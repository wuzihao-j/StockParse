#!/usr/bin/env python3.6
#coding=utf-8

from hdfs import *
import urllib
import urllib.request
import re
import random
import time
import os
import sys
import importlib
importlib.reload(sys)

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


url='http://quote.stockstar.com/stock/ranklist_a_3_1_1'+'.html'
request=urllib.request.Request(url=url,headers={"User-Agent":random.choice(user_agent)})
try:
    response=urllib.request.urlopen(request)
except urllib.error.HTTPError as e:
    print('page=1','',e.code)
except urllib.error.URLError as e:
    print('page=1','',e.reason)
pagecontent=response.read().decode('gbk')
print('get page1')
pattern=re.compile('.*?id="ClientPageControl1_hdnPageSize" value="(.*?)" />.*?id="ClientPageControl1_hdnTotalCount" value="(.*?)"', re.S)
body=re.findall(pattern,pagecontent)

totalPage = round(int(body[0][1]) / int(body[0][0]))
currentDate = time.strftime('%Y-%m-%d',time.localtime(time.time()))
dirName = ("/home/paper/stock_data/a_stock")
hdfsName = "/input/stock/a_stock"
fp = open(os.path.join(dirName,currentDate), 'w', encoding='utf-8')

for page in range(1, totalPage):
    url='http://quote.stockstar.com/stock/ranklist_a_3_1_'+str(page)+'.html'
    request=urllib.request.Request(url=url,headers={"User-Agent":random.choice(user_agent)})
    try:
        response=urllib.request.urlopen(request)
    except urllib.error.HTTPError as e:
        print('page=',page,'',e.code)
    except urllib.error.URLError as e:
        print('page=',page,'',e.reason)
    content=response.read().decode('gbk')
    print('get page',page)
    pattern=re.compile('<tbody[\s\S]*</tbody>')
    body=re.findall(pattern,str(content))
    pattern=re.compile('>(.*?)<')
    if len(body) >= 1:
        stock_page=re.findall(pattern,body[0])
        stock_last=stock_page[:]
        for data in stock_page:
            if data=='' or data == None:
                stock_last.remove('')
    for i in range(0, len(stock_last), 13):
        fp.write(stock_last[i]+'\t'+stock_last[i+1]+'\t'+stock_last[i+2]+'\t'+stock_last[i+3]+'\t'+stock_last[i+4]+'\t'+stock_last[i+5]+'\t'+stock_last[i+6]+'\t'+stock_last[i+7]+'\t'+stock_last[i+8]+'\t'+stock_last[i+9]+'\t'+stock_last[i+10]
                 +'\t'+stock_last[i+11]+'\t'+stock_last[i+12]+'\n')
        fp.flush()
    time.sleep(random.randrange(1,2))
fp.close

client = Client("http://127.0.0.1:50070",root="/",timeout=100,session=False)
if currentDate in client.list(hdfsName):
    fsize = os.path.getsize(os.path.join(dirName,currentDate))
    blockSize = client.status(os.path.join(hdfsName ,currentDate))['length']
    if fsize > blockSize:
        client.delete(os.path.join(hdfsName ,currentDate))
        client.upload(hdfsName, os.path.join(dirName, currentDate))
else :
    client.upload(hdfsName, os.path.join(dirName, currentDate))








