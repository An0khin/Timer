package com.home.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimerControllerTest {
    private static final String PATH = "src/test/resources/Times.dat";
    Map<String, List<String>> tasksMap;
    public static TimerController timerController;

    @BeforeAll
    public static void setUpController() {
        timerController = new TimerController(PATH);
    }

    @BeforeEach
    public void setUp() {
        tasksMap = new HashMap<>();

        List<String> times1 = Arrays.asList("2023-05-13T21:00:00.000", "2023-05-14T21:00:00.123");
        List<String> times2 = Arrays.asList("2023-05-13T21:00:00.000", "2023-05-14T21:00:00.000", "2023-05-15T20:00:00.000");

        tasksMap.put("Task_1", times1);
        tasksMap.put("Task_2", times2);

        timerController.tasksMap = new HashMap<>(tasksMap);
    }

    @Test
    public void durationToString() {
        List<String> times = tasksMap.get("Task_1");
        Duration time = Duration.between(LocalDateTime.parse(times.get(0)), LocalDateTime.parse(times.get(1)));

        String timeString = timerController.durationToString(time);

        Assertions.assertEquals("1:0:0.123", timeString);
    }

    @Test
    public void clickStart() {
        timerController.listSelectedValue = "Task_1";

        timerController.clickTimer();
        LocalDateTime now = LocalDateTime.now();
        Map<String, List<String>> tasks = TimesWorker.readDates(PATH);

        Assertions.assertEquals(tasks.get("Task_2"), tasksMap.get("Task_2"));

        String lastTask1Time = tasks.get("Task_1").get(tasks.get("Task_1").size() - 1);
        Assertions.assertEquals(now.toString().substring(0, 18), lastTask1Time.substring(0, 18));
    }

    @Test
    public void createTask() {
        String newTaskName = "Task_3";

        timerController.createTaskWithName(newTaskName);
        Map<String, List<String>> tasks = TimesWorker.readDates(PATH);

        Assertions.assertTrue(tasks.containsKey(newTaskName));
    }

    @Test
    public void deleteTask() {
        timerController.listSelectedValue = "Task_1";

        timerController.deleteTask();
        Map<String, List<String>> tasks = TimesWorker.readDates(PATH);

        Assertions.assertFalse(tasks.containsKey("Task_1"));
    }

    @Test
    public void deleteLastTime() {
        timerController.listSelectedValue = "Task_1";

        timerController.deleteLastTime();
        Map<String, List<String>> tasks = TimesWorker.readDates(PATH);

        List<String> beforeTask1 = tasksMap.get("Task_1");
        List<String> afterTask1 = tasks.get("Task_1");

        Assertions.assertEquals(beforeTask1.size() - 1, afterTask1.size());
        Assertions.assertEquals(beforeTask1.get(beforeTask1.size() - 2), afterTask1.get(afterTask1.size() - 1));
    }
}
