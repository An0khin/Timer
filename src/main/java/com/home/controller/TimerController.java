package com.home.controller;

import com.home.view.TimerView;

import javax.swing.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TimerController {
    private static final String ACTIVE_TIME_STRING = "Active time: ";
    private static final String ALL_TIME_STRING = "All time: ";
    private static final String RED_BUTTON_PATH = "src/main/resources/buttonred.png";
    private static final String GREEN_BUTTON_PATH = "src/main/resources/buttongreen.png";
    private final TimerView timerView;
    private HashMap<String, List<String>> tasksMap;
    private String listSelectedValue = null;
    private boolean isTimerTurnedOn = false;

    public TimerController(TimerView timerView) {
        this.timerView = timerView;

        setTasks();
        setListeners();
        setRealTimeRenderer();
    }

    private void setTasks() {
        tasksMap = TimesWorker.readDates();

        timerView.getListTasks().setListData(tasksMap.keySet().toArray(new String[0]));
    }

    private void updateListData() {
        timerView.getListTasks().setListData(tasksMap.keySet().toArray(new String[0]));
    }

    private void setListeners() {
        setListSelectionListener();

        setButtonsListeners();
    }

    private void setListSelectionListener() {
        JList<String> list = timerView.getListTasks();
        list.addListSelectionListener(event -> {
            if(!event.getValueIsAdjusting()) {
                listSelect(list);
            }
        });
    }

    private void listSelect(JList<String> list) {
        listSelectedValue = list.getSelectedValue();

        isTimerTurnedOn = isSelectedTaskRunning();
        setIcon();
    }

    private void setIcon() {
        JButton startTimerButton = timerView.getStartTimerButton();
        if(isTimerTurnedOn) {
            startTimerButton.setIcon(new ImageIcon(GREEN_BUTTON_PATH));
        } else {
            startTimerButton.setIcon(new ImageIcon(RED_BUTTON_PATH));
        }
    }

    private boolean isSelectedTaskRunning() {
        if(listSelectedValue == null) {
            return false;
        }

        List<String> times = tasksMap.get(listSelectedValue);

        return times.size() % 2 != 0;
    }

    private void setButtonsListeners() {
        setClickTimerListener();
        setCreateTaskListener();
        setDeleteTaskListener();
        setDeleteLastTimeListener();
    }

    private void setClickTimerListener() {
        JButton clickTimerButton = timerView.getStartTimerButton();
        clickTimerButton.addActionListener(event -> clickTimer());
    }

    private void clickTimer() {
        List<String> tempList = new ArrayList<>(tasksMap.get(listSelectedValue));
        tempList.add(LocalDateTime.now().toString());
        tasksMap.replace(listSelectedValue, tempList);
        saveTasks();

        isTimerTurnedOn = !isTimerTurnedOn;
        setIcon();
    }

    private void setCreateTaskListener() {
        JButton createTaskButton = timerView.getCreateTaskButton();
        createTaskButton.addActionListener(e -> createTask());
    }

    private void createTask() {
        String name = JOptionPane.showInputDialog("Write a name of the task"); //creating dialog with user

        if(name != null && !name.equals("")) {
            tasksMap.put(name, new ArrayList<>());
            updateListData();
            saveTasks();
        }
    }

    private void setDeleteTaskListener() {
        JButton deleteTaskButton = timerView.getDeleteTaskButton();
        deleteTaskButton.addActionListener(e -> deleteTask());
    }

    private void deleteTask() {
        tasksMap.remove(listSelectedValue);
        updateListData();
        saveTasks();
    }

    private void setDeleteLastTimeListener() {
        JButton deleteLastTimeButton = timerView.getDeleteLastTime();
        deleteLastTimeButton.addActionListener(e -> deleteLastTime());
    }

    private void deleteLastTime() {
        List<String> tempList = new ArrayList<>(tasksMap.get(listSelectedValue));
        if(tempList.isEmpty()) {
            return;
        }

        tempList.remove(tempList.size() - 1);
        tasksMap.replace(listSelectedValue, tempList);
        saveTasks();
    }

    private void saveTasks() {
        TimesWorker.saveFile(tasksMap);
    }

    private void setRealTimeRenderer() {
        Thread thread = new Thread(() -> {
            while(true) {
                renderTime();
            }
        });
        thread.start();
    }

    private void renderTime() {
        Duration activeTime = Duration.ofSeconds(0);
        Duration allTime = Duration.ofSeconds(0);

        if(isSelectedValueValid()) {
            List<String> times = tasksMap.get(listSelectedValue);

            for(int i = 0; i < times.size() - 1; i += 2) {
                activeTime = activeTime.plus(Duration.between(LocalDateTime.parse(times.get(i)),
                        LocalDateTime.parse(times.get(i + 1))));
            }

            if(isTimerTurnedOn) {
                activeTime = activeTime.plus(Duration.between(LocalDateTime.parse(times.get(times.size() - 1)), LocalDateTime.now()));
                allTime = Duration.between(LocalDateTime.parse(times.get(0)), LocalDateTime.now());
            } else {
                allTime = Duration.between(LocalDateTime.parse(times.get(0)), LocalDateTime.parse(times.get(times.size() - 1)));
            }

        }

        setTimeLabels(activeTime, allTime);
    }

    private boolean isSelectedValueValid() {
        return !tasksMap.isEmpty() && listSelectedValue != null && tasksMap.containsKey(listSelectedValue) && !tasksMap.get(listSelectedValue).isEmpty();
    }

    private void setTimeLabels(Duration activeTime, Duration allTime) {
        JLabel activeTimeLabel = timerView.getActiveTimeLabel();
        JLabel allTimeLabel = timerView.getAllTimeLabel();

        activeTimeLabel.setText(ACTIVE_TIME_STRING + durationToString(activeTime));
        allTimeLabel.setText(ALL_TIME_STRING + durationToString(allTime));
    }

    private String durationToString(Duration time) {
        String stringTime = time.toString();
        stringTime = stringTime.substring(2, stringTime.length() - 1);

        if(stringTime.contains("H")) {
            int hours = Integer.parseInt(stringTime.substring(0, stringTime.indexOf("H")));
            stringTime = stringTime.substring(stringTime.indexOf("H") + 1);
            int days = hours / 24;
            hours %= 24;
            stringTime = days + ":" + hours + ":" + stringTime;
        }

        stringTime = stringTime.replace("M", ":");
        stringTime = stringTime.replace("S", ":");

        return stringTime;
    }
}
