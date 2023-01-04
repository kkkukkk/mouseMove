package moveMouse;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ToolFrame extends JFrame {
	public static JTextField textField = new JTextField("000000",6);
	public static String time = textField.getText();

	public static Long hour = Long.parseLong(time.substring(0,2));
	public static Long minute = Long.parseLong(time.substring(2,4));
	public static Long second = Long.parseLong(time.substring(4,6));
	
	public static int flag = 0;
	
	public static Dimension xy = Toolkit.getDefaultToolkit().getScreenSize();
	
	public static TimerTask taskCopy;
	
	public ToolFrame () {
		JPanel pan = new JPanel();
		JLabel label = new JLabel("Duration : ");
		JButton buttonS = new JButton("시작");
		JButton buttonF = new JButton("종료");
		JLabel explain = new JLabel("종료는 alt + f4가 빠릅니다.");
		
		textField.setText(String.format("%02d:%02d:%02d", 0, 0, 0));

		pan.add(label);
		pan.add(textField);
		pan.add(buttonS);
		pan.add(buttonF);
		pan.add(explain);
		add(pan);
		
		buttonS.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (textField.getText().equals("00:00:00")) {
					JOptionPane.showMessageDialog(null, "시간을 입력하세요!", "오류", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if (buttonS.getText().equals("시작")) {
					mouseMove();
					buttonS.setText("재 시작");
				} else {
					hour = Long.parseLong(time.substring(0,2));
					minute = Long.parseLong(time.substring(2,4));
					second = Long.parseLong(time.substring(4,6));
					mouseMove();
				}
			}
		});
		
		buttonF.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!buttonS.getText().equals("시작")) {
					buttonS.setText("재 시작");
				}
				textField.setText(String.format("%02d:%02d:%02d", 0, 0, 0));
				try {
					taskCopy.cancel();					
				} catch (Exception ex) {
					return;
				}
			}
		});
		
		setTitle("Move Mouse");
		setVisible(true);
		setSize(350,90);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void mkTask() { 
		taskCopy = new TimerTask() {
			@Override
			public void run() {
				Random rd = new Random();
				int rx = rd.nextInt(xy.width);
				int ry = rd.nextInt(xy.height);
				
				try {
					Robot robot = new Robot();
					robot.mouseMove(rx, ry);
					tick();
				} catch (AWTException e) {
					e.printStackTrace();
				}
			};
		};
	};
	
	public void mouseMove() {
		Timer timer = new Timer();
		
		long delay = 0;
		long intervalPeriod = 1 * 1000;
		
		mkTask();
		
		timer.scheduleAtFixedRate(taskCopy, delay, intervalPeriod);
	}
	
	public static void tick() {
		if (flag == 0 ) {
			flag = 1;
			setting();
		}
		
		if (second == 0) {
			if (minute == 0) {
				if (hour == 0) {
					taskCopy.cancel();
				} else {
					minute = 59L;
					second = 59L;
					hour--;
				}
			} else {
				second = 59L;
				minute--;
			}
		} else {
			second--;
		}
		
		textField.setText(String.format("%02d:%02d:%02d", hour, minute, second));
	}
	
	public static void setting() {
		time = textField.getText().replaceAll(":","");
		hour = Long.parseLong(time.substring(0,2));
		minute = Long.parseLong(time.substring(2,4));
		second = Long.parseLong(time.substring(4,6));
	}
}
