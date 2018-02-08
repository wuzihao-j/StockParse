package com.wzh.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MyShell {

    public static void execHQL(String sql) throws IOException {
        List<String> command = new ArrayList<String>();

        command.add("hive");
        command.add("-e");
        command.add("\""+sql+"\"");

        List<String> results = new ArrayList<String>();
        ProcessBuilder hiveProcessBuilder = new ProcessBuilder(command);
        Process hiveProcess = hiveProcessBuilder.start();

        BufferedReader br = new BufferedReader(new InputStreamReader(
                hiveProcess.getInputStream()));
        String data = null;
        while ((data = br.readLine()) != null) {

            results.add(data);
        }
    }
}
