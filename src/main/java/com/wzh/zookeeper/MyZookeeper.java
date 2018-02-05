package com.wzh.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.ZooDefs.Ids;

import java.util.List;

public class MyZookeeper {
    public static ZooKeeper zk;

    public static void DoIndustry(String root, String nodeName, String port) throws Exception {

        zk = new ZooKeeper("120.78.205.73:" + port, 60000, new Watcher() {
            public void process(WatchedEvent watchedEvent) {
                if(watchedEvent.getPath() != null && watchedEvent.getPath().equals("/stock/industry_parse")
                        && watchedEvent.getType() == Watcher.Event.EventType.NodeCreated){
                    System.out.println("开始分析行业数据");
                }
            }
        });

        createNode(root, nodeName);
    }

    public static void createNode(String root, String nodeName) throws KeeperException, InterruptedException {
        if(zk.exists("/" + root, true) == null){
            zk.create("/" + root, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
        zk.create("/" + root + "/" + nodeName, "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        List<String> children = zk.getChildren("/" + root, false);

    }
}
