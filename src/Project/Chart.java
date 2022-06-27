package Project;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.DimensionUIResource;
import javax.swing.tree.DefaultMutableTreeNode;

import com.sun.source.tree.Tree;

import Base.Base;

public class Chart extends JFrame implements Base{
	
	JTree tree;
	String st1[] = "뮤지컬, 오페라, 콘서트".split(", ");
	String st2[] = "Red, Orange, Blue".split(", ");
	String filter[] = "M, O, C".split(", ");
	Color color[] = {Color.red, Color.orange, Color.blue};
	
	int findex = 0;
	int cindex = 0;

	JPanel p1 = get(new JPanel(new BorderLayout()), setb(Color.white), set(new TitledBorder(new LineBorder(Color.black), "TOP 5", 0, 2, new Font("", 1, 15))));
	JPanel p2 = get(new JPanel(new BorderLayout()), set(650, 350));
	JPanel cp;
	
	ArrayList<ArrayList<String>> list = new ArrayList<>();
	
	public Chart() {
		
		setTitle("차트");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		design();
		action();
		setVisible(true);
		pack();
		setLocationRelativeTo(null);
		setIconImage(new ImageIcon(file("오렌지.jpg")).getImage());
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				new Main();
			}
		});
		
	}

	@Override
	public void design() {
		
		DefaultMutableTreeNode root, node;
		root = new DefaultMutableTreeNode("Orange Ticket");
		
		node = new DefaultMutableTreeNode("분류");
		node.add(new DefaultMutableTreeNode("뮤지컬"));
		node.add(new DefaultMutableTreeNode("오페라"));
		node.add(new DefaultMutableTreeNode("콘서트"));
		root.add(node);
		
		node = new DefaultMutableTreeNode("차트디자인");
		node.add(new DefaultMutableTreeNode("막대 그래프"));
		node.add(new DefaultMutableTreeNode("꺽은선 그래프"));
		root.add(node);
		
		node = new DefaultMutableTreeNode("차트색깔");
		node.add(new DefaultMutableTreeNode("Red"));
		node.add(new DefaultMutableTreeNode("Orange"));
		node.add(new DefaultMutableTreeNode("Blue"));
		root.add(node);
		
		tree = new JTree(root);
		
		add(p1, "North");
		add(p2);
		
		p1.add(tree);
		
		chart1();
		
	}
	
	public void chart1() {
		
		p2.removeAll();
		
		p2.add(cp = new JPanel() {
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				
				Query("select count(p.p_no), p.p_name from perform p, ticket t where p.p_no = t.p_no and left(pf_no, 1) = ? group by p.p_name order by count(p.p_no) desc limit 5;", list, filter[findex]);
				
				int max = intnum(list.get(0).get(0));
				
				for (int i = 0; i < list.size(); i++) {
					
					float a = (float) (Double.parseDouble(list.get(i).get(0)) / max);
					int h = (int) (a * 250);
					
					g.setColor(color[cindex]);
					g.fillRect(45 + i * 115, 300 - h, 45, h);
					
					g.setColor(Color.black);
					g.drawRect(45 + 115 * i, 300 - h, 45, h);
					
					g.setFont(new Font("맑은 고딕", 0, 12));
					g.drawString(list.get(i).get(0), 60 + 115 * i, 290 - h);
					g.drawString(list.get(i).get(1), 45 + i * 115, 330);
					
				}
				
			}
		});
		
		revalidate();
		repaint();
		
	}
	
	public void chart2() {
		
		p2.removeAll();
		
		p2.add(cp = new JPanel() {
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				
				Graphics2D g2 = (Graphics2D) g;
				
				Query("select count(p.p_no), p.p_name from perform p, ticket t where p.p_no = t.p_no and left(pf_no, 1) = ? group by p.p_name order by count(p.p_no) desc limit 5;", list, filter[findex]);
				
				String name[] = new String[5];
				String count[] = new String[5];
				int h[] = new int[5];
				int max = intnum(list.get(0).get(0));
				
				for (int i = 0; i < list.size(); i++) {
					
					float a = (float) (Double.parseDouble(list.get(i).get(0)) / max);
					
					h[i] = (int) (250 * a);
					name[i] = list.get(i).get(1);
					count[i] = list.get(i).get(0);
				}
				
				for (int i = 0; i < 4; i++) {
					
					g2.setStroke(new BasicStroke(2));
					g2.setColor(color[cindex]);
					g2.drawLine(70 + 115 * i, 300 - h[i], 70 + 115 * (i + 1), 300 - h[i + 1]);
					
					g2.setColor(Color.black);
					g2.setFont(new Font("맑은 고딕", 0, 12));
					g2.fillOval(70 + 115 * i - 5, 300 - h[i] - 5, 10, 10);
					g2.drawString(name[i], 55 + 115 * i, 330);
					g2.drawString(count[i], 70 + 115 * i - 5, 290 - h[i]);
					
				}
				
				g2.fillOval(70 + 115 * 4 - 5, 300 - h[4] - 5, 10, 10);
				g2.drawString(name[4], 55 + 115 * 4, 330);
				g2.drawString(count[4], 70 + 115 * 4 - 5, 290 - h[4]);
				
			}
		});
		
		revalidate();
		repaint();
		
	}
	
	@Override
	public void action() {
		
		tree.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				
				pack();
				
				if (e.getClickCount() == 2) {
					
					for (int i = 0; i < st1.length; i++) {
						if (tree.getLastSelectedPathComponent().toString().contentEquals(st1[i])) {
							findex = i;
						}
						if (tree.getLastSelectedPathComponent().toString().contentEquals(st2[i])) {
							cindex = i;
						}
					}
					
					if (tree.getLastSelectedPathComponent().toString().contentEquals("막대 그래프")) {
						chart1();
					}
					
					if (tree.getLastSelectedPathComponent().toString().contentEquals("꺽은선 그래프")) {
						chart2();
					}
					
				}
				
				revalidate();
				repaint();
				
			}
		});
		
	}

}
