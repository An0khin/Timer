package com.home;

import com.home.controller.TimerController;
import com.home.view.TimerView;

public class Main {
    public static void main(String[] args) {
        TimerView timer = new TimerView();
        TimerController timerController = new TimerController(timer);
    }
}
