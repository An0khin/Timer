package com.home;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.time.*;

public class Timer
{
	JList listTasks;
	JLabel timeWithNoRest = new JLabel();
	JLabel timeWithRest = new JLabel();

	String selectedVal = null;

	String rest = "Time(incl rest): ";
	String noRest = "Time(excl rest): ";

	Hashtable<String, java.util.List<String>> tasksAr = new Hashtable<String, java.util.List<String>>();

	boolean startTimer = false; //true - timer was turned on; false - timer was turned off
	JButton startTimerButton = new JButton();
	JButton deleteLastTime = new JButton();

	public static void main(String[] args)
	{
		Timer t = new Timer();
		t.go();
	}
	public void go()
	{
		setTasks();
		setGUI();
		Thread thr = new Thread(new Checker());
		thr.start();
	}
	public void setTasks()
	{
		try{
			BufferedReader br = new BufferedReader(new FileReader("E:/Programming/ALL/Java/My Projects/Timer/Classes/Times.dat"));
			String line;
			String name = ""; //
			while((line = br.readLine()) != null)
			{
				String[] tempList = line.split(";");
				//System.out.println(Arrays.toString(tempList));
				name = tempList[0];//
				tasksAr.put(tempList[0], Arrays.asList(Arrays.copyOfRange(tempList, 1, tempList.length)));
			}
			//System.out.println(tasksAr);
			
		}catch(IOException e) {}
	}
	public void setIcon()
	{
		if(startTimer)
			startTimerButton.setIcon(new ImageIcon("E:/Programming/ALL/Java/My Projects/Timer/Classes/buttonred.png"));
		else
			startTimerButton.setIcon(new ImageIcon("E:/Programming/ALL/Java/My Projects/Timer/Classes/buttongreen.png"));
	}
	public void getDiff(String name)
	{
		if(!tasksAr.isEmpty() && name != null && tasksAr.containsKey(name) && tasksAr.get(name).size() > 0)
		{
			java.util.List<String> times = tasksAr.get(name);
			//System.out.println(times);
			Duration differenceRest = Duration.ofSeconds(0);
			Duration differenceNoRest = Duration.ofSeconds(0);

			if(times.size() % 2 == 0)
			{
				startTimer = true;
				for(int i = 0; i < times.size(); i += 2)
				{
					differenceRest = differenceRest.plus(Duration.between(LocalDateTime.parse(times.get(i)),
						LocalDateTime.parse(times.get(i+1))));
				}
				differenceNoRest = Duration.between(LocalDateTime.parse(times.get(0)),LocalDateTime.parse(times.get(times.size() - 1)));
			}
			else
			{
				startTimer = false;
				for(int i = 0; i < times.size(); i += 2)
				{
					if(i != times.size() - 1)
						differenceRest = differenceRest.plus(Duration.between(LocalDateTime.parse(times.get(i)),
							LocalDateTime.parse(times.get(i+1))));
				}
				differenceNoRest = Duration.between(LocalDateTime.parse(times.get(0)),Timer.time());
				differenceRest = differenceRest.plus(Duration.between(LocalDateTime.parse(times.get(times.size() - 1)),Timer.time()));
			}
			timeWithNoRest.setText(noRest + normDuration(differenceNoRest));
			timeWithRest.setText(rest + normDuration(differenceRest));
			//System.out.println(normDuration(differenceNoRest));
			//System.out.println(normDuration(differenceRest));
		}
		else
		{
			startTimer = true;
			timeWithNoRest.setText(noRest + "0");
			timeWithRest.setText(rest + "0");
		}
		setIcon();
	}
	public String normDuration(Duration dur)
	{
		String str = dur.toString();
		str = str.substring(2, str.length() - 1);
		if(str.indexOf("H") != -1)
		{
			int hours = Integer.parseInt(str.substring(0,str.indexOf("H")));
			str = str.substring(str.indexOf("H") + 1, str.length());
			int days = hours / 24;
			hours %= 24;
			str = days + ":" + hours + ":" + str;
		}
		str = str.replace("M",":");
		str = str.replace("S",":");
		return str;
	}
	public void setGUI()
	{
		JFrame mainFrame = new JFrame("Timer");
		JPanel mainPanel = new JPanel(new GridLayout(1,2));

		JPanel panel = new JPanel(new BorderLayout());
		Box box = new Box(BoxLayout.Y_AXIS);
		JPanel boxPanel = new JPanel(new BorderLayout());


		timeWithNoRest.setText(noRest + "0");
		timeWithRest.setText(rest + "0");

		box.add(timeWithNoRest);
		box.add(timeWithRest);
		boxPanel.add(BorderLayout.CENTER, box);

		startTimerButton.setIcon(new ImageIcon("E:/Programming/ALL/Java/My Projects/Timer/Classes/buttonred.png"));
		startTimerButton.addActionListener(new ButtonListener());

		deleteLastTime.setText("Delete last time");
		deleteLastTime.addActionListener(new ButtonListener());

		panel.add(BorderLayout.NORTH, boxPanel);
		panel.add(BorderLayout.CENTER, startTimerButton);
		panel.add(BorderLayout.SOUTH, deleteLastTime);

		JPanel panelList = new JPanel(new BorderLayout());

		listTasks = new JList(); //list where you can select one or more values (will be used for showing time that was took for task)
		listTasks.addListSelectionListener(new ListSelection());
		listTasks.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scr = new JScrollPane(listTasks);
		listTasks.setListData(tasksAr.keySet().toArray());

		JPanel panelButtons = new JPanel(new GridLayout(1,2));

		JButton createTask = new JButton("Create");
		createTask.addActionListener(new ButtonListener());

		JButton deleteTask = new JButton("Delete");
		deleteTask.addActionListener(new ButtonListener());

		panelButtons.add(createTask);
		panelButtons.add(deleteTask);

		panelList.add(BorderLayout.NORTH, listTasks);
		panelList.add(BorderLayout.SOUTH, panelButtons);

		mainPanel.add(panelList);
		mainPanel.add(panel);

		mainFrame.getContentPane().add(mainPanel);
		// mainFrame.getContentPane().add(BorderLayout.WEST, panelList);
		// mainFrame.getContentPane().add(BorderLayout.CENTER, panel);
		// mainFrame.getContentPane().add(BorderLayout.WEST, new JButton(new ImageIcon("C:/Users/anohi/Desktop/button.png")));
		// mainFrame.getContentPane().add(BorderLayout.EAST, new JButton(new ImageIcon("C:/Users/anohi/Desktop/button.png")));

		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setSize(380,300);
		mainFrame.setResizable(false);
		mainFrame.setVisible(true);
	}
	public static LocalDateTime time()
	{
		return LocalDateTime.now();
	}
	public void saveFile()
	{
		try
		{
			BufferedWriter bw = new BufferedWriter(new FileWriter("E:/Programming/ALL/Java/My Projects/Timer/Classes/Times.dat"));
			String key;
			String line;
			for(Enumeration<String> keys = tasksAr.keys(); keys.hasMoreElements();)
			{
				key = keys.nextElement();
				line = key + ";";
				for(String date : tasksAr.get(key))
				{
					line += date + ";";
				}
				line = line.substring(0, line.length() - 1);
				
				bw.write(line);
				bw.newLine();
			}
			bw.close();
		} catch(Exception e) {}
	}
	class ListSelection implements ListSelectionListener
	{
		public void valueChanged(ListSelectionEvent e) 
		{
			if(!e.getValueIsAdjusting())
			{
				selectedVal = (String) listTasks.getSelectedValue();	
				//getDiff(selectedVal);
				//System.out.println(Timer.time());
				//System.out.println(selectedVal);
			}
		}
	}
	class ButtonListener implements ActionListener
	{
		//JFrame createFrame;
		//JTextField name;
		public void actionPerformed(ActionEvent ae)
		{
			JButton but = (JButton) ae.getSource();
			ArrayList<String> tempList;

			switch(but.getText())
			{
				case "Delete":
					tasksAr.remove(selectedVal);
					//System.out.println(tasksAr);
					listTasks.setListData(tasksAr.keySet().toArray());
					saveFile();
					break;
				case "Create":
					String name = (String) JOptionPane.showInputDialog("Write a name of the task"); //creating dialog with user

					if(name != null && !name.equals(""))
					{
						tasksAr.put(name, new ArrayList<String>());
						listTasks.setListData(tasksAr.keySet().toArray());
						saveFile();
					}

					// anyFrame.setUndecorated(true); //Frame without borders
					// anyFrame.setAlwaysOnTop(true); //That's understanded
					// anyFrame.dispatchEvent(new WindowEvent(createFrame, WindowEvent.WINDOW_CLOSING)); //This is program's window closing
					break;
				case "Delete last time":
					tempList = new ArrayList<String>(tasksAr.get(selectedVal));
					if(tempList.size() > 0)
					{
						tempList.remove(tempList.size() - 1);
						tasksAr.replace(selectedVal, tempList);
						//getDiff(selectedVal);
						saveFile();
					}
					break;
				default:
					tempList = new ArrayList<String>(tasksAr.get(selectedVal));
					tempList.add((Timer.time()).toString());
					tasksAr.replace(selectedVal, tempList);
					//getDiff(selectedVal);
					saveFile();
			}
		}
	}
	class Checker implements Runnable
	{
		public void run()
		{
			while(true)
				getDiff(selectedVal);
		}
	}
}