package com.wzh.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.ZooDefs.Ids;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

public class MyZookeeper {
    public static ZooKeeper zk;

    public static void getInstance(Watcher watcher) throws Exception {
        zk = new ZooKeeper("120.78.205.73:2181", 60000, watcher);
    }

    public static void createNode(String root, String nodeName) throws KeeperException, InterruptedException {
        if(zk.exists("/" + root, true) == null){
            zk.create("/" + root, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
        zk.create("/" + root + "/" + nodeName, "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        List<String> children = zk.getChildren("/" + root, false);

    }
}
