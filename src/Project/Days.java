package Project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Event;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import Base.Base;

public class Days extends JPanel implements Base{
	
	JPanel p[] = new JPanel[3];
	JLabel lab;
	
	ArrayList<ArrayList<String>> list = new ArrayList<>();
	
	Color color[] = {Color.magenta, Color.blue, Color.orange};
	
	JFrame f;
	LocalDate date;
	String category;
	
	public Days(JFrame f,LocalDate date, String category) {
		
		this.f = f;
		this.date = date;
		this.category = category;
		
		setLayout(new GridLayout(4, 1));
		setBorder(new LineBorder(Color.black));
		
		design();
		action();
		
	}

	@Override
	public void design() {
		
		Query("select * from perform where left(pf_no, 1) like ? and p_date =?", list, "%" + category + "%", date.toString());
		
		int r = date.getDayOfWeek().getValue();
		
		add(lab = get(new JLabel(date.getDayOfMonth() + "", JLabel.RIGHT), setf(r == 6 ? Color.blue : r == 7 ? Color.red : Color.black)));
		for (int i = 0; i < p.length; i++) {
			add(p[i] = new JPanel(new FlowLayout(0, 5, 0)));
		}
		
		for (int i = 0; i < list.size(); i++) {
			
			if (i == 3) {
				return;
			}
			
			String pfno = list.get(i).get(1).substring(0, 1);
			int cindex = pfno.contentEquals("M") ? 0 : pfno.contentEquals("O") ? 1 : 2;
			
			JLabel lb = get(new JLabel(pfno, JLabel.CENTER),  setf(Color.white), setb(color[cindex]), set(new LineBorder(Color.black)), set(15, 15));
			lb.setOpaque(true);
			
			int j = i;
			
			lb.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					f.dispose();
					new Ticketing(list.get(j).get(0));
				}
			});
			
			p[i].add(lb);
			p[i].add(new JLabel(list.get(i).get(2)));
			
		}
		
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		
	}

}
