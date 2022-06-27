package Project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import Base.Base;

public class Main extends JFrame implements Base{
	
	JPanel p1 = get(new JPanel(new BorderLayout()), set(0, 40));
	JPanel p2 = get(new JPanel(new BorderLayout()), set(new EmptyBorder(10, 10, 10, 10)));
	JPanel p3 = get(new JPanel(new FlowLayout(0, 10, 0)));
	JPanel p4 = get(new JPanel(new FlowLayout(FlowLayout.RIGHT)), set(200, 0));
	JPanel p5 = get(new JPanel(new BorderLayout()), set(new TitledBorder(new LineBorder(Color.black), "인기공연(뮤지컬)", 0, 2, new Font("맑은 고딕", 1, 15))));
	JPanel p6 = get(new JPanel(new GridLayout(0, 5, 10, 5)));
	JPanel p7 = get(new JPanel());
	
	JLabel lab1 = get(new JLabel("TICKETING"), setb(15));
	JLabel lab2 = get(new JLabel("MONTH SCHEDULE"), setb(15));
	JLabel lab3 = get(new JLabel("CHART"), setb(15));
	JLabel lab4 = get(new JLabel("LOGIN"), setb(15));
	JLabel lab5 = get(new JLabel("MYPAGE"), setb(15));
	JLabel img = new JLabel();
	JLabel lab[] = new JLabel[3];
	
	String st[] = "M, O, C".split(", ");
	String title[] = "인기공연(뮤지컬), 인기공연(오페라), 인기공연(콘서트)".split(", ");
	
	ArrayList<ArrayList<String>> list = new ArrayList<>();
	
	public Main() {
		
		SetFrame(this, "메인", EXIT_ON_CLOSE, 650, 300);
		design();
		action();
		setVisible(true);
		
	}

	@Override
	public void design() {
		
		add(p1, "North");
		add(p2);
		
		p1.add(p3);
		p1.add(p4, "East");
		
		p2.add(p5);
		p5.add(p6);
		p5.add(p7, "South");
		
		p3.add(lab1);
		p3.add(lab2);
		p3.add(lab3);
		
		p4.add(img);
		p4.add(lab4);
		p4.add(lab5);
		
		for (int i = 0; i < lab.length; i++) {
			p7.add(lab[i] = new JLabel((i + 1) + "", 0));
		}
		
		new Thread(() ->{
			try {
				
				while (true) {
					
					for (int i = 0; i < 3; i++) {
						
						p6.removeAll();
						Query("select p_name, pf_no from perform p, ticket t where t.p_no = p.p_no and left(pf_no,1) = ? group by p.p_name order by count(p.p_no) desc limit 5;", list, st[i]);
						for (JLabel lab : lab) {
							lab.setForeground(Color.black);
						}
						lab[i].setForeground(Color.red);
						p5.setBorder(new TitledBorder(new LineBorder(Color.black), title[i], 0, 2, new Font("맑은 고딕", 1, 15)));
						
						for (int j = 0; j < list.size(); j++) {
							
							JPanel p = new JPanel(new BorderLayout());
							JLabel image = getimg(file("공연사진/" + list.get(j).get(1) + ".jpg"), 120, 120, set(new LineBorder(Color.black)));
							
							p.add(new JLabel((j + 1)+ "위"), "North");
							p.add(image);
							p.add(new JLabel(list.get(j).get(0), 0), "South");
							
							p6.add(p);
							
						}
						
						revalidate();
						repaint();
						Thread.sleep(2000);
						
					}
					
				}
				
			} catch (Exception e) {
			}
		}).start();
		
		log();
		
	}
	
	public void log() {
		
		if (member.isEmpty()) {
			img.setVisible(false);
			lab4.setText("LOGIN");
		}else {
			img.setVisible(true);
			img = Dbimg(member.get(0).get(0), 20, 20, set(new LineBorder(Color.black)));
			lab4.setText("LOGOUT");
		}
		
		revalidate();
		repaint();
		
	}
	
	public void action() {
		
		lab1.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				dispose();
				new Find();
			}
		});
		
		lab2.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				dispose();
				new Month();
			}
		});
		
		lab3.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				dispose();
				new Chart();
			}
		});
		
		lab4.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (member.isEmpty()) {
					dispose();
					new Login();
				}else {
					member.clear();
					log();
				}
			}
		});
		
		lab5.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				
				if (member.isEmpty()) {
					err("로그인을 하세요.");
				}else {
					dispose();
					new Mypage();
				}
				
			}
		});
		
	}
	public static void main(String[] args) {
		new Main();
	}
}
