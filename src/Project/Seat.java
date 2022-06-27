package Project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import Base.Base;

public class Seat extends JFrame implements Base{
	
	JPanel p1 = get(new JPanel(new BorderLayout()));
	JPanel p2 = get(new JPanel(new BorderLayout(10,10)), set(new EmptyBorder(10, 10, 20, 10)));
	JPanel p3 = get(new JPanel(new GridLayout(0, 1, 0, 10)));
	JPanel p4 = get(new JPanel(new GridLayout(0, 10, 5, 5)));
	SelectSeat panel;
	
	JLabel lab1 = get(new JLabel("STAGE",0), setb(25), set(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.black)), set(0, 60));
	JLabel lab2 = get(new JLabel("", JLabel.RIGHT), setb(15));
	JLabel lab[][] = new JLabel[6][10];
	JLabel wlab;
	
	String txt[] = "A, B, C, D, E, F".split(", ");
	
	ArrayList<ArrayList<String>> list = new ArrayList<>();
	ArrayList<String> data;
	int type;
	
	static int seat = 0;
	static int count = 1;
	
	public Seat(ArrayList<String> data, int type) {
		
		this.data = data;
		this.type = type;
		
		select.clear();
		seat = 0;
		count = 1;
		
		SetFrame(this, "좌석", DISPOSE_ON_CLOSE, 900, 500);
		design();
		action();
		setVisible(true);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				if (type == 0) {
					new Ticketing(data.get(0));
				}else {
					new Mypage();
				}
			}
		});
		
	}

	public void design() {
		
		lab2.setText("날짜 : " + data.get(6));
		
		add(p1);
		add(panel = new SelectSeat(this, data, type), "East");
		
		p1.add(lab1, "North");
		p1.add(p2);
		
		p2.add(lab2, "North");
		p2.add(p3, "West");
		p2.add(p4);
		
		for (int i = 0; i < txt.length; i++) {
			p3.add(wlab = get(new JLabel(txt[i], 0), setb(20)));
		}
		
		for (int i = 0; i < lab.length; i++) {
			for (int j = 0; j < lab[i].length; j++) {
				p4.add(lab[i][j]= get(new JLabel((j + 1) + "", 0), set(new LineBorder(Color.black)), setb(Color.white)));
				lab[i][j].setOpaque(true);
			}
		}
		
		Query("select * from ticket where p_no = ?", list, data.get(0));
		
		for (int i = 0; i < list.size(); i++) {
			
			String seat[] = list.get(i).get(3).split(",");
			
			for (int j = 0; j < seat.length; j++) {
				
				int row = Arrays.asList(txt).indexOf(seat[j].substring(0, 1));
				int col = intnum(seat[j].substring(1));
				
				lab[row][col-1].setBackground(Color.gray);
				
			}
			
		}
		
		if (type == 1) {
			
			String me[] = data.get(10).split(",");
			String dis[] = data.get(11).split(",");
			
			for (int i = 0; i < dis.length; i++) {
				
				int row = Arrays.asList(txt).indexOf(me[i].substring(0, 1));
				int col = intnum(me[i].substring(1));
				
				lab[row][col - 1].setBackground(Color.orange);
				
				ArrayList r = new ArrayList<>();
				
				r.add(me[i]);
				r.add(data.get(4));
				r.add(dis[i]);
				
				select.add(r);
				
			}
			
		}
		
		panel.select();
		
	}
	
	public void action() {
		
		for (int i = 0; i < lab.length; i++) {
			for (int j = 0; j < lab[i].length; j++) {
				
				int r = i;
				int c = j;
				
				lab[i][j].addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						
						String name = txt[r] + (lab[r][c].getText().length() == 1 ? "0" + lab[r][c].getText() : lab[r][c].getText());
						
						if (lab[r][c].getBackground().equals(Color.orange)) {
							
							lab[r][c].setBackground(Color.white);
							seat--;
							
							for (int k = 0; k < select.size(); k++) {
								if (select.get(k).get(0).contentEquals(name)) {
									select.remove(k);
								}
							}
							
						}else {
							
							if (lab[r][c].getBackground().equals(Color.gray)) {
								err("이미 예매된 좌석입니다.");
							}else if (seat == count) {
								err("더 이상 선택이 불가합니다.");
							}else {
								
								lab[r][c].setBackground(Color.orange);
								
								ArrayList row = new ArrayList<>();
								seat++;
								
								row.add(name);
								row.add(data.get(4));
								row.add("0");
								
								select.add(row);
								
							}
							
						}
						
						panel.select();
						
					}
				});
				
			}
		}
		
	}

}
