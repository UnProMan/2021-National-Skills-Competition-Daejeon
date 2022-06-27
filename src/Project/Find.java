package Project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import Base.Base;

public class Find extends JFrame implements Base{
	
	JMenuBar bar = new JMenuBar();
	JMenu menu = new JMenu("분류");
	JMenuItem item[] = new JMenuItem[4];
	String mtxt[] = "전체, 뮤지컬, 오페라, 콘서트".split(", ");
	
	JPanel p1 = get(new JPanel(new BorderLayout(10, 10)), set(new EmptyBorder(100, 10, 40, 10)));
	JPanel p2 = get(new JPanel(new GridLayout(2, 2, 5, 5)));
	JPanel p3 = get(new JPanel(new GridLayout(2, 1, 0, 10)));
	JPanel p4 = get(new JPanel());
	JPanel p5 = get(new JPanel());
	
	JPanel pn1 = get(new JPanel(new BorderLayout(0, 10)), set(new EmptyBorder(10, 10, 10, 10)), set(350, 0));
	JPanel pn2 = get(new JPanel(new FlowLayout(FlowLayout.RIGHT)));
	JPanel pn3 = get(new JPanel(new BorderLayout()));
	
	JLabel before = get(new JLabel("◀"), setb(25));
	JLabel next = get(new JLabel("▶"), setb(25));
	JLabel one = get(new JLabel("<<처음으로"));
	JLabel fin = get(new JLabel("마지막으로>>"));
	
	JLabel lab1 = get(new JLabel("분류 : 전체", JLabel.RIGHT), setp(15), set(new EmptyBorder(0, 0, 0, 20)));
	JLabel lab2 = get(new JLabel("현재 예매 가능 공연", JLabel.RIGHT), setp(15));
	
	String str[] = "M, O, C".split(", ");
	JLabel pages[];
	
	JTextField txt1 = new JTextField(13);
	JButton btn1 = new JButton("검색");
	
	Vector v1;
	Vector v2 = new Vector(Arrays.asList("공연날짜, 공연명, 공연가격".split(", ")));
	DefaultTableModel model = new DefaultTableModel(v1, v2) {
		public boolean isCellEditable(int row, int column) {
			return false;
		};
	};
	JTable tbl = new JTable(model);
	JScrollPane scl = new JScrollPane(tbl);
	
	int mindex = - 1;
	int pindex = 0;
	int page;
	
	ArrayList<ArrayList<String>> list = new ArrayList<>();
	ArrayList<ArrayList<String>> temp = new ArrayList<>();
	
	public Find() {
		
		SetFrame(this, "검색", DISPOSE_ON_CLOSE, 700, 600);
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

	public void design() {
		
		add(bar, "North");
		for (int i = 0; i < item.length; i++) {
			menu.add(item[i] = new JMenuItem(mtxt[i]));
			if (i == 0) {
				menu.addSeparator();
			}
		}
		bar.add(menu);
		
		add(p1);
		add(pn1, "East");
		
		p1.add(lab1, "North");
		p1.add(before, "West");
		p1.add(next, "East");
		p1.add(p2);
		p1.add(p3, "South");
		
		p3.add(p4);
		p3.add(p5);
		
		pn1.add(pn2, "North");
		pn1.add(pn3);
		
		pn2.add(new JLabel("공연명 : "));
		pn2.add(txt1);
		pn2.add(btn1);
		
		pn3.add(lab2, "North");
		pn3.add(scl);
		
		p5.add(one);
		p5.add(fin);
		
		img();
		table();
		
	}
	
	public void img() {
		
		p2.removeAll();
		p4.removeAll();
		
		String m = mindex == -1 ? "" : str[mindex];
		
		Query("select * from perform where left(pf_no, 1) like ? and p_name like ? and p_date >= date(now()) group by p_name order by p_date, p_price desc;", list, "%" + m + "%", "%" + txt1.getText() + "%");
		
		page = list.size() / 4 + (list.size()%4 == 0 ? 0 : 1);
		pages = new JLabel[page];
		
		for (int i = 0; i < pages.length; i++) {
			p4.add(pages[i] =get(new JLabel("●"), setf(pindex == i ? Color.red : Color.black)));
		}
		
		for (int i = pindex * 4; i < (pindex + 1) * 4; i++) {
			
			JLabel img;
			if (i >= list.size()) {
				img = get(new JLabel(""), set(new LineBorder(Color.black)), setb(Color.white));
				img.setOpaque(true);
			}else {
				img = getimg(file("공연사진/" + list.get(i).get(1) + ".jpg"), 120, 140, set(new LineBorder(Color.black)));
				img.setToolTipText(list.get(i).get(2) + " : " + list.get(i).get(6));
			}
			
			int j = i;
			img.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					
					if (j >= list.size()) {
						err("공연정보가 없습니다.");
					}else {
						dispose();
						new Ticketing(list.get(j).get(0));
					}
					
				}
			});
			
			p2.add(img);
			
		}
		
		revalidate();
		repaint();
		
	}
	
	public void table() {
		
		String m = mindex == -1 ? "" : str[mindex];
		
		SetData("select p_date, p_name, format(p_price, 0), p_no from perform where left(pf_no, 1) like ? and p_name like ? and p_date >= date(now()) order by p_date, p_price desc;", temp, model, 0, 3, "%" + m + "%", "%" + txt1.getText() + "%");
		
		if (temp.isEmpty()) {
			err("검색 결과가 없습니다.");
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
				dispose();
				new Ticketing(temp.get(tbl.getSelectedRow()).get(3));
			}
		});
		
		btn1.addActionListener(e->{
			table();
			img();
		});
		
		for (int i = 0; i < item.length; i++) {
			int j = i;
			item[i].addActionListener(e->{
				if (j == 0) {
					mindex = -1;
				}else {
					mindex = j -1;
				}
				table();
				img();
			});
		}
		
		before.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (pindex != 0) {
					pindex--;
					img();
				}
			}
		});
		
		next.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (pindex != page -1) {
					pindex++;
					img();
				}
			}
		});
		
		one.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				pindex = 0;
				img();
			}
		});
		
		fin.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				pindex = page - 1;
				img();
			}
		});
		
	}

}
