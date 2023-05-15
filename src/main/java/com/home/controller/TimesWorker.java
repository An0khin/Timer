package com.home.controller;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class TimesWorker {
    private static final String datesPath = "src/main/resources/Times.dat";

    public static HashMap<String, List<String>> readDates() {
        HashMap<String, List<String>> datesMap = new HashMap<>();

        Optional<FileReader> optionalFileReader = openFile();

        if(optionalFileReader.isEmpty()) {
            return datesMap;
        }

        try(Scanner scanner = new Scanner(optionalFileReader.get())) {
            while(scanner.hasNext()) {
                String line = scanner.nextLine();

                String[] tempList = line.split(";");
                String name = tempList[0];
                List<String> times = getTimes(tempList);

                datesMap.put(name, times);
            }
        }

        return datesMap;
    }

    public static void saveFile(Map<String, List<String>> timesMap) {
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(datesPath))) {

            for(String key : timesMap.keySet()) {
                String line = createLine(key, timesMap);

                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static Optional<FileReader> openFile() {
        try {
            return Optional.of(new FileReader(datesPath));
        } catch(IOException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public static List<String> getTimes(String[] list) {
        return Arrays.asList(Arrays.copyOfRange(list, 1, list.length));
    }

    public static String createLine(String key, Map<String, List<String>> timesMap) {
        List<String> values = timesMap.get(key);
        StringBuilder line = new StringBuilder();
        line.append(key);
        line.append(";");

        for(String date : values) {
            line.append(date);
            line.append(";");
        }

        line.deleteCharAt(line.length() - 1);

        return line.toString();
    }
}
