package Project;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import Base.Base;

public class Month extends JFrame implements Base{
	
	LocalDate now = LocalDate.now();
	
	JPanel p1 = get(new JPanel(new GridLayout(2, 1)), set(new EmptyBorder(5, 5, 5, 5)));
	JPanel p2 = get(new JPanel(new FlowLayout(0)));
	JPanel p3 = get(new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0)));
	JPanel p4 = get(new JPanel(new GridLayout(0, 7)), set(new EmptyBorder(5, 5, 5, 5)));
	
	String st[] = "일, 월, 화, 수, 목, 금, 토".split(", ");
	
	JLabel M = get(new JLabel("M", 0), set(new LineBorder(Color.black)), setf(Color.white), setb(Color.magenta), set(30, 30));
	JLabel O = get(new JLabel("O", 0), set(new LineBorder(Color.black)), setf(Color.white), setb(Color.blue), set(30, 30));
	JLabel C = get(new JLabel("C", 0), set(new LineBorder(Color.black)), setf(Color.white), setb(Color.orange), set(30, 30));
	
	JButton btn1 = new JButton("전체");
	
	JLabel before = get(new JLabel("◀"), setb(20));
	JLabel next = get(new JLabel("▶"), setb(20));
	JLabel lab1 = get(new JLabel("", 0), set(100, 30),setb(25));
	JLabel lab2 = get(new JLabel("뮤지컬"));
	JLabel lab3 = get(new JLabel("뮤지컬"));
	JLabel lab4 = get(new JLabel("뮤지컬"));
	JLabel lab;
	
	String category = "";
	
	public Month() {
		
		SetFrame(this, "월별 일정", DISPOSE_ON_CLOSE, 1000, 900);
		design();
		action();
		setVisible(true);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				new Main();
			}
		});
		
	}

	@Override
	public void design() {
		
		add(p1, "North");
		add(p4);
		
		p1.add(p2);
		p1.add(p3);
		
		p2.add(before);
		p2.add(lab1);
		p2.add(next);
		
		p3.add(M);
		p3.add(lab2);
		p3.add(O);
		p3.add(lab3);
		p3.add(C);
		p3.add(lab4);
		p3.add(btn1);
		
		M.setOpaque(true);
		O.setOpaque(true);
		C.setOpaque(true);
		
		cal();
		
	}
	
	public void cal() {
		
		p4.removeAll();
		
		for (int i = 0; i < st.length; i++) {
			p4.add(lab = get(new JLabel(st[i], 0), setf(i == 0 ? Color.red : i == 6 ? Color.blue : Color.black)));
		}
		
		lab1.setText(now.format(DateTimeFormatter.ofPattern("MM월")));
		
		int val = now.withDayOfMonth(1).getDayOfWeek().getValue();
		int start = val == 7 ? 0 : val;
		int fin = now.lengthOfMonth();
		
		for (int i = 1- start; i < 42 - start; i++) {
			
			if (i < 1 || i > fin) {
				p4.add(new JLabel(""));
			}else {
				p4.add(new Days(this, LocalDate.of(now.getYear(), now.getMonth(), i), category));
			}
			
		}
		
		revalidate();
		repaint();
		
	}
	
	public void action() {
		
		before.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				
				if (now.getMonthValue() == 1) {
					return;
				}
				
				now = now.plusMonths(-1);
				cal();
				
			}
		});
		
		next.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				
				if (now.getMonthValue() == 12) {
					return;
				}
				
				now = now.plusMonths(1);
				cal();
				
			}
		});
		
		btn1.addActionListener(e->{
			category = "";
			cal();
		});
		
		M.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					category ="M";
					cal();
				}
			}
		});
		
		O.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					category ="O";
					cal();
				}
			}
		});
		
		C.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					category ="C";
					cal();
				}
			}
		});
		
	}

}
