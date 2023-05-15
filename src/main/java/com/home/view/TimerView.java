package com.home.view;

import javax.swing.*;
import java.awt.*;

public class TimerView {
    private final JLabel activeTimeLabel = new JLabel();
    private final JLabel allTimeLabel = new JLabel();
    private JList<String> listTasks;
    private JButton startTimerButton;
    private JButton deleteLastTime;
    private JButton deleteTaskButton;
    private JButton createTaskButton;

    public TimerView() {
        initGUI();
    }

    private void initGUI() {
        JFrame mainFrame = setupFrame();
    }

    private JFrame setupFrame() {
        JFrame frame = new JFrame("Timer");
        JPanel mainPanel = setupMainPanel();

        frame.getContentPane().add(mainPanel);

        setDefaultSettings(frame);

        return frame;
    }

    private void setDefaultSettings(JFrame mainFrame) {
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(380, 300);
        mainFrame.setResizable(false);
        mainFrame.setVisible(true);
    }

    private JPanel setupMainPanel() {
        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        JPanel listPanel = setupListPanel();
        JPanel buttonsPanel = setupTimeButtonsPanel();

        mainPanel.add(listPanel);
        mainPanel.add(buttonsPanel);

        return mainPanel;
    }

    private JPanel setupListPanel() {
        JPanel listPanel = new JPanel(new BorderLayout());

        JScrollPane scrollPane = setupListScrollPane();

        JPanel buttonsPanel = setupTaskButtonsPanel();

        listPanel.add(BorderLayout.CENTER, scrollPane);
        listPanel.add(BorderLayout.SOUTH, buttonsPanel);

        return listPanel;
    }

    private JScrollPane setupListScrollPane() {
        listTasks = setupListTasks();

        return new JScrollPane(listTasks);
    }

    private JList<String> setupListTasks() {
        JList<String> list = new JList<>();
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        return list;
    }

    private JPanel setupTaskButtonsPanel() {
        JPanel buttonsPanel = new JPanel(new GridLayout(1, 2));

        createTaskButton = setupCreateTaskButton();
        deleteTaskButton = setupDeleteTaskButton();

        buttonsPanel.add(createTaskButton);
        buttonsPanel.add(deleteTaskButton);

        return buttonsPanel;
    }

    private JButton setupCreateTaskButton() {
        return new JButton("Create");
    }

    private JButton setupDeleteTaskButton() {
        return new JButton("Delete");
    }

    private JPanel setupTimeButtonsPanel() {
        JPanel buttonsPanel = new JPanel(new BorderLayout());
        JPanel boxPanel = setupTimeLabelsPanel();

        startTimerButton = setupStartTimerButton();
        deleteLastTime = setupDeleteLastTimeButton();

        buttonsPanel.add(BorderLayout.NORTH, boxPanel);
        buttonsPanel.add(BorderLayout.CENTER, startTimerButton);
        buttonsPanel.add(BorderLayout.SOUTH, deleteLastTime);

        return buttonsPanel;
    }

    private JPanel setupTimeLabelsPanel() {
        JPanel boxPanel = new JPanel(new BorderLayout());
        Box box = setupVerticalTimeBox();

        boxPanel.add(BorderLayout.CENTER, box);

        return boxPanel;
    }

    private Box setupVerticalTimeBox() {
        Box box = new Box(BoxLayout.Y_AXIS);
        addTimesToTimeBox(box);

        return box;
    }

    private void addTimesToTimeBox(Box box) {
        box.add(allTimeLabel);
        box.add(activeTimeLabel);
    }

    private JButton setupStartTimerButton() {
        JButton startButton = new JButton();

        startButton.setIcon(new ImageIcon("src/main/resources/buttonred.png"));

        return startButton;
    }

    private JButton setupDeleteLastTimeButton() {
        return new JButton("Delete last time");
    }


    public JList<String> getListTasks() {
        return listTasks;
    }

    public JLabel getAllTimeLabel() {
        return allTimeLabel;
    }

    public JLabel getActiveTimeLabel() {
        return activeTimeLabel;
    }

    public JButton getStartTimerButton() {
        return startTimerButton;
    }

    public JButton getDeleteLastTime() {
        return deleteLastTime;
    }

    public JButton getDeleteTaskButton() {
        return deleteTaskButton;
    }

    public JButton getCreateTaskButton() {
        return createTaskButton;
    }
}
