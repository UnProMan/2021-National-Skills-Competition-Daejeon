package Base;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

public interface Base {
	
	public void design();
	public void action();
	
	ArrayList<ArrayList<String>> member= new ArrayList<>();
	ArrayList<ArrayList<String>> select = new ArrayList<>();
	
	DecimalFormat df = new DecimalFormat("#,##0");
	
	default void SetFrame(JFrame f, String title, int ex, int x, int y) {
		f.setTitle(title);
		f.setDefaultCloseOperation(ex);
		f.setSize(x, y);
		f.setLocationRelativeTo(null);
		f.setIconImage(new ImageIcon(file("오렌지.jpg")).getImage());
	}
	
	default <Any> Any get(JComponent comp, Set...sets) {
		
		for (Set set : sets) {
			set.set(comp);
		}
		
		return (Any) comp;
		
	}
	
	default Set set(boolean tf) {
		return c->c.setEnabled(tf);
	}
	
	default Set set(Border border) {
		return c->c.setBorder(border);
	}
	
	default Set set(int x, int y) {
		return c->c.setPreferredSize(new Dimension(x, y));
	}
	
	default Set setf(Color color) {
		return c->c.setForeground(color);
	}
	
	default Set setb(Color color) {
		return c->c.setBackground(color);
	}
	
	default Set setp(int font) {
		return c->c.setFont(new Font("맑은 고딕", Font.PLAIN, font));
	}
	
	default Set setb(int font) {
		return c->c.setFont(new Font("맑은 고딕", Font.BOLD, font));
	}
	
	default void jop(String txt) {
		JOptionPane.showMessageDialog(null, txt, "정보", JOptionPane.INFORMATION_MESSAGE);
	}
	
	default void err(String txt) {
		JOptionPane.showMessageDialog(null, txt, "경고", JOptionPane.ERROR_MESSAGE);
	}
	
	default String file(String file) {
		return "Datafiles/" + file;
	}
	
	default Integer intnum(String txt) {
		return Integer.parseInt(txt);
	}
	
	default JLabel Dbimg(String no, int x, int y, Set...sets ) {
		
		JLabel comp = new JLabel(new ImageIcon(getimg(no).getImage().getScaledInstance(x, y, 4)));
		
		for (Set set : sets) {
			set.set(comp);
		}
		
		return comp;
		
	}
	
	default JLabel getimg(String file, int x, int y, Set...sets ) {
		
		JLabel comp = new JLabel(new ImageIcon(new ImageIcon(file).getImage().getScaledInstance(x, y, 4)));
		
		for (Set set : sets) {
			set.set(comp);
		}
		
		return comp;
		
	}
	
	default void Query(String sql, ArrayList<ArrayList<String>> list, String...v) {
		
		try {
			
			Connection c= DriverManager.getConnection("jdbc:mysql://localhost/2021전국?serverTimezone=UTC&allowLoadLocalInfile=true", "root", "1234");
			PreparedStatement s = c.prepareStatement(sql);
			
			for (int i = 0; i < v.length; i++) {
				s.setString(i + 1, v[i]);
			}
			
			list.clear();
			ResultSet rs = s.executeQuery();
			ResultSetMetaData rsm = rs.getMetaData();
			
			while (rs.next()) {
				ArrayList row = new ArrayList();
				for (int i = 1; i <= rsm.getColumnCount(); i++) {
					row.add(rs.getString(i));
				}
				list.add(row);
			}
			
			s.close();
			c.close();
			
		} catch (Exception e) {
			System.out.println(e);
		}
		
	}
 	
	default void Update(String sql, String...v) {
		
		try {
			
			Connection c= DriverManager.getConnection("jdbc:mysql://localhost/2021전국?serverTimezone=UTC&allowLoadLocalInfile=true", "root", "1234");
			PreparedStatement s = c.prepareStatement(sql);
			
			for (int i = 0; i < v.length; i++) {
				s.setString(i + 1, v[i]);
			}
			
			s.executeUpdate();
			s.close();
			c.close();
			
		} catch (Exception e) {
			System.out.println(e);
		}
		
	}
	
	default void SetData(String sql, ArrayList<ArrayList<String>> list, DefaultTableModel model, int start, int fin, String...v) {
		
		Query(sql, list, v);
		
		model.setNumRows(0);
		
		for (int i = 0; i < list.size(); i++) {
			Vector row = new Vector();
			for (int j = start; j < fin; j++) {
				row.add(list.get(i).get(j));
			}
			model.addRow(row);
		}
		
	}
	
	default ImageIcon getimg(String no) {
		
		try {
			
			Connection c= DriverManager.getConnection("jdbc:mysql://localhost/2021전국?serverTimezone=UTC&allowLoadLocalInfile=true", "root", "1234");
			PreparedStatement s = c.prepareStatement("select u_img from user where u_no = ?");
			
			s.setString(1, no);
			ResultSet rs = s.executeQuery();
			
			rs.next();
			
			var img = rs.getBinaryStream(1);
			
			return new ImageIcon(img.readAllBytes());
			
		} catch (Exception e) {
			System.out.println(e);
		}
		
		return null;
		
	}
	
	default void Saveimg(String path, String no) {
		
		try {
			
			Connection c= DriverManager.getConnection("jdbc:mysql://localhost/2021전국?serverTimezone=UTC&allowLoadLocalInfile=true", "root", "1234");
			PreparedStatement s = c.prepareStatement("update user set u_img = ? where u_no = ?");
			
			s.setBytes(1, new FileInputStream(new File(path)).readAllBytes());
			s.setString(2, no);
			
			s.executeUpdate();
			s.close();
			c.close();
			
		} catch (Exception e) {
			System.out.println(e);
		}
		
	}
	
}
