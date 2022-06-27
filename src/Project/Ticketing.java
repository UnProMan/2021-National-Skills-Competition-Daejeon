package Project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import Base.Base;

public class Ticketing extends JFrame implements Base{
	
	JPanel p1 = get(new JPanel(new BorderLayout(10, 10)), set(new EmptyBorder(10, 10, 10, 10)));
	JPanel p2 = get(new JPanel(new BorderLayout()), set(new EmptyBorder(0, 0, 40, 0)));
	JPanel p3 = get(new JPanel(new BorderLayout(0, 10)), set(150, 0));
	JPanel p4 = get(new JPanel(new BorderLayout()), set(new LineBorder(Color.black)));
	JPanel p5 = get(new JPanel(new BorderLayout(10, 10)), set(new EmptyBorder(5, 5, 5, 5)));
	JPanel p6 = get(new JPanel(new GridLayout(0, 1, 0, 10)));
	
	JLabel lab1 = get(new JLabel(""), setb(30));
	JLabel img = new JLabel("");
	
	Vector v1;
	Vector v2 = new Vector(Arrays.asList("날짜, 여유좌석".split(", ")));
	DefaultTableModel model = new DefaultTableModel(v1, v2) {
		public boolean isCellEditable(int row, int column) {
			return false;
		};
	};
	JTable tbl = new JTable(model);
	JScrollPane scl = new JScrollPane(tbl);
	
	JButton btn1 = new JButton("예매하기");
	
	ArrayList<ArrayList<String>> list = new ArrayList<>();
	ArrayList<ArrayList<String>> temp = new ArrayList<>();
	
	String pno;
	int r = 0;
	DateTimeFormatter dt  = DateTimeFormatter.ofPattern("MM. dd");
	
	public Ticketing(String pno) {
		
		this.pno = pno;
		
		SetFrame(this, "예매", DISPOSE_ON_CLOSE, 500, 300);
		design();
		action();
		setVisible(true);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				new Find();
			}
		});
		
	}

	@Override
	public void design() {
		
		update();
		table();
		
		lab1.setText(list.get(0).get(2));
		
		add(p1);
		
		p1.add(lab1, "North");
		p1.add(p2);
		p1.add(p3, "East");
		
		p2.add(p4);
		p4.add(p5);
		
		p3.add(scl);
		p3.add(btn1, "South");
		
		p5.add(img = getimg(file("공연사진/" + list.get(0).get(1) + ".jpg"), 120, 130, set(new LineBorder(Color.black))), "West");
		p5.add(p6);
		
		for (int i = 0; i < temp.size(); i++) {
			if (temp.get(i).get(2).contentEquals(pno)) {
				tbl.setRowSelectionInterval(i, i);
			}
		}
		
	}
	
	public void update() {
		
		Query("select * from perform where p_no = ?", list, pno);
		p6.removeAll();
		
		p6.add(new JLabel("장소 : " + list.get(0).get(3)));
		p6.add(new JLabel("출연 : " + list.get(0).get(2)));
		p6.add(new JLabel("가격 : " + df.format(intnum(list.get(0).get(4)))));
		p6.add(new JLabel("날짜 : " + list.get(0).get(6)));
		
		revalidate();
		repaint();
		
	}
	
	public void table() {
		
		model.setNumRows(0);
		
		Query("select p_date, 60 - (select sum(length(replace(t_discount, ',', ''))) from ticket where ticket.p_no = perform.p_no), p_no from perform where pf_no = ?;", temp, list.get(0).get(1));
		
		for (int i = 0; i < temp.size(); i++) {
			model.addRow(new Object[] {LocalDate.parse(temp.get(i).get(0)).format(dt), temp.get(i).get(1)});
		}
		
		tbl.setRowSelectionInterval(r, r);
		
		revalidate();
		repaint();
		
	}
	
	@Override
	public void action() {
		
		tbl.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				
				pno = temp.get(tbl.getSelectedRow()).get(2);
				r = tbl.getSelectedRow();
				update();
				table();
				
			}
		});

		btn1.addActionListener(e->{
			
			if (member.isEmpty()) {
				
				int a = JOptionPane.showConfirmDialog(null, "<html>회원만이 가능한 서비스 입니다.<br>로그인 하시겠습니까?", "로그인", JOptionPane.YES_NO_OPTION);
				
				if (a == 0) {
					dispose();
					new Login();
				}
				
			}else {
				
				if (LocalDate.parse(temp.get(tbl.getSelectedRow()).get(0)).isBefore(LocalDate.now())) {
					err("종료된 공연입니다.");
				}else {
					dispose();
					new Seat(list.get(0), 0);
				}
				
			}
			
		});
		
	}

}
