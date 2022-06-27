package Project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

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

public class SelectSeat extends JPanel implements Base{
	
	JPanel p1 = get(new JPanel(new BorderLayout()), set(new EmptyBorder(5, 5, 5, 5)));
	JPanel p2 = get(new JPanel(new BorderLayout(0, 10)));
	JPanel p3 = get(new JPanel(new FlowLayout(0)));
	JPanel p4 = get(new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10)), set(new TitledBorder(new LineBorder(Color.black), "선택한 좌석")));
	JPanel p5 = get(new JPanel(new GridLayout(2, 2, 5, 5)));
	
	JButton btn1 = new JButton("취소하기");
	JButton btn2 = new JButton("다음으로");
	
	JLabel lab1 = get(new JLabel(""), setb(25), set(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black)));
	JLabel lab2 = get(new JLabel("총금액 : 0"));
	
	String txt[] = "청소년 할인 20%, 어린이 할인 40%, 장애인 할인 50%".split(", ");
	JLabel people[] = new JLabel[5];
	
	ArrayList<String> data;
	JFrame f;
	int type;
	
	public SelectSeat(JFrame f, ArrayList<String> data, int type) {
		
		this.type = type;
		this.f = f;
		this.data = data;
		
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(250, 0));
		setBorder(new LineBorder(Color.black));
		
		design();
		action();
		
	}

	@Override
	public void design() {
		
		if (type == 1) {
			btn2.setText("수정하기");
			Seat.count = data.get(10).split(",").length;
			Seat.seat = data.get(10).split(",").length;
		}
		
		lab1.setText(data.get(2));
		
		add(p1);
		
		p1.add(lab1, "North");
		p1.add(p2);
		
		p2.add(p3, "North");
		p2.add(p4);
		p2.add(p5, "South");
		
		p3.add(new JLabel("인원수 : "));
		for (int i = 0; i < people.length; i++) {
			if (type == 0) {
				p3.add(people[i] = get(new JLabel(Seat.count > i ? "●" : "○"), setb(15)));
			}else {
				p3.add(people[i] = get(new JLabel(Seat.count > i ? "●" : "○"), setb(15)), setf(Color.gray));
			}
		}
		
		p5.add(lab2);
		p5.add(new JLabel(""));
		
		p5.add(btn1);
		p5.add(btn2);
		
		select();
		
	}
	
	public void sum() {
		
		int sum = 0;
		
		for (int i = 0; i < select.size(); i++) {
			sum += intnum(select.get(i).get(1));
		}
		
		lab2.setText(df.format(sum));
		
	}
	
	public void select() {
		
		p4.removeAll();
		
		JPanel pn1[] = new JPanel[select.size()];
		JPanel pn2[] = new JPanel[select.size()];
		JLabel lb1[] = new JLabel[select.size()];
		JLabel lb2[] = new JLabel[select.size()];
		
		for (int i = 0; i < select.size(); i++) {
			
			pn1[i] = get(new JPanel(new BorderLayout()), set(new LineBorder(Color.black)), setb(Color.white), set(220, 30));
			pn2[i] = get(new JPanel(new GridLayout(3, 1)), set(0, 90));
			lb1[i] = get(new JLabel(select.get(i).get(0) + " : " + df.format(intnum(select.get(i).get(1))), 0));
			lb2[i] = get(new JLabel("▼"), setb(15), set(50, 0));
			JCheckBox ck[] = new JCheckBox[3];
			ButtonGroup group = new ButtonGroup();
			
			pn1[i].add(lb1[i]);
			pn1[i].add(lb2[i], "East");
			pn1[i].add(pn2[i], "South");
			
			for (int j = 0; j < ck.length; j++) {
				
				pn2[i].add(ck[j] = new JCheckBox(txt[j]));
				group.add(ck[j]);
				
				int n = i;
				int nn = j;
				
				ck[j].addActionListener(e->{
					
					double a = nn == 0 ? 0.8 : nn == 1 ? 0.6 : nn == 2 ? 0.5 : 1;
					
					select.get(n).set(1, (int) (intnum(data.get(4)) * a) + "" );
					select.get(n).set(2, (nn + 1) + "");
					
					lb1[n].setText(select.get(n).get(0) + " : " + df.format(intnum(select.get(n).get(1))));
					sum();
					
				});
				
			}
			
			if (type == 1) {
				if (!select.get(i).get(2).contentEquals("0")) {
					ck[intnum(select.get(i).get(2)) - 1].setSelected(true);
				}
			}
			
			pn2[i].setVisible(false);
			
			int n = i;
			lb2[i].addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					
					if (lb2[n].getText().contentEquals("▼")) {
						
						for (int j = 0; j < select.size(); j++) {
							pn1[j].setPreferredSize(new Dimension(220, 30));
							pn2[j].setVisible(false);
							lb2[j].setText("▼");
						}
						
						pn1[n].setPreferredSize(new Dimension(220, 120));
						pn2[n].setVisible(true);
						lb2[n].setText("▲");
						
					}else {
						pn1[n].setPreferredSize(new Dimension(220, 30));
						pn2[n].setVisible(false);
						lb2[n].setText("▼");
					}
					
					revalidate();
					repaint();
					
				}
			});
			
			p4.add(pn1[i]);
			
		}
		
		sum();
		
		revalidate();
		repaint();
		
	}
	
	@Override
	public void action() {
		
		for (int i = 0; i < people.length; i++) {
			int n = i;
			people[i].addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					
					if (type != 1) {
						
						for (JLabel lab : people) {
							lab.setText("○");
						}
						
						for (int j = 0; j <= n; j++) {
							people[j].setText("●");
						}
						
						Seat.count = n + 1;
						
					}
					
				}
			});
		}
		
		btn1.addActionListener(e->{
			f.dispose();
			if (type == 0) {
				new Ticketing(data.get(0));
			}else {
				new Mypage();
			}
		});
		
		btn2.addActionListener(e->{
			
			if (Seat.seat == 0) {
				err("좌석을 선택해주세요.");
			}else if (Seat.seat != Seat.count) {
				err("인원수에 맞게 좌석을 선택해주세요.");
			}else {
				
				if (type == 0) {
					f.dispose();
					new Pay(data);
				}else {
					
					jop("수정되었습니다.");
					
					String seat = "";
					String dis = "";
					
					for (int i = 0; i < select.size(); i++) {
						seat = seat.isBlank() ? select.get(i).get(0) : seat + "," + select.get(i).get(0);
						dis = dis.isBlank() ? select.get(i).get(2) : dis + "," + select.get(i).get(2);
					}
					
					Update("update ticket set t_seat = ?, t_discount = ? where t_no =?", seat, dis, data.get(7));
					
					f.dispose();
					new Mypage();
					
				}
				
			}
			
		});
		
	}

}
