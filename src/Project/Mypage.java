package Project;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import Base.Base;

public class Mypage extends JFrame implements Base{
	
	JPanel p1 = get(new JPanel(new BorderLayout(10, 10)), set(new EmptyBorder(10, 10, 10, 10)));
	
	JLabel lab1 = get(new JLabel(""), setb(15));

	Vector v1;
	Vector v2 = new Vector(Arrays.asList("날짜, 공연명, 좌석, 금액".split(", ")));
	DefaultTableModel model = new DefaultTableModel(v1, v2) {
		public boolean isCellEditable(int row, int column) {
			return false;
		};
	};
	JTable tbl = new JTable(model);
	JScrollPane scl = new JScrollPane(tbl);
	
	ArrayList<ArrayList<String>> list = new ArrayList<>();
	ArrayList<ArrayList<String>> temp = new ArrayList<>();
	
	JPopupMenu pop = new JPopupMenu();
	JMenuItem item1 = new JMenuItem("취소");
	JMenuItem item2 = new JMenuItem("수정");
	
	public Mypage() {
		
		SetFrame(this, "마이페이지", DISPOSE_ON_CLOSE, 600, 500);
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
		
		pop.add(item1);
		pop.add(item2);
		
		lab1.setText("회원 : " + member.get(0).get(1));
		
		add(p1);
		
		p1.add(lab1, "North");
		p1.add(scl);
		
		table();
		
	}
	
	public void table() {
		
		SetData("select p_date, p_name, t_seat, p.p_price, t_no, t_discount from ticket t, perform p where t.p_no = p.p_no and t.u_no = ? order by p_date;", list, model, 0, 4, member.get(0).get(0));
		
		for (int i = 0; i < list.size(); i++) {
			
			String st[] = list.get(i).get(5).split(",");
			int p = intnum(list.get(i).get(3));
			int sum = 0;
			
			for (int j = 0; j < st.length; j++) {
				if (st[j].contentEquals("1")) p = (int) (p * 0.8);
				if (st[j].contentEquals("2")) p = (int) (p * 0.6);
				if (st[j].contentEquals("3")) p = (int) (p * 0.5);
				sum += p;
			}
			
			tbl.setValueAt(df.format(sum), i, 3);
			
		}
		
		tbl.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				
				this.setHorizontalAlignment(JLabel.CENTER);
				
				return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			}
		});
		
		revalidate();
		repaint();
		
	}
	
	@Override
	public void action() {
		
		tbl.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				
				if (e.getButton() == 3) {
					pop.show(tbl, e.getX(), e.getY());
				}
				
			}
		});
		
		item1.addActionListener(e->{
			
			int row = tbl.getSelectedRow();
			
			if (row != -1) {
				
				jop("취소되었습니다.");
				Update("delete from ticket where t_no = ?", list.get(tbl.getSelectedRow()).get(4));
				table();
				
			}
			
		});
		
		item2.addActionListener(e->{
			
			int row = tbl.getSelectedRow();
			
			if (row != -1) {
				
				if (LocalDate.parse(list.get(row).get(0)).isBefore(LocalDate.now())) {
					err("수정이 불가합니다.");
				}else {
					
					Query("select * from perform p, ticket t where p.p_no = t.p_no and t.t_no = ?", temp, list.get(row).get(4));
					
					dispose();
					new Seat(temp.get(0), 1);
					
				}
				
			}
			
		});
		
	}

}
