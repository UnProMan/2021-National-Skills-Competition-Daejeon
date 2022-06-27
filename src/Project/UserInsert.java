package Project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import Base.Base;

public class UserInsert extends JFrame implements Base{
	
	JPanel p1 = get(new JPanel(new BorderLayout(0, 10)), set(new EmptyBorder(10, 10, 10, 10)), set(150, 0));
	JPanel p2 = get(new JPanel(new BorderLayout(10, 10)), set(new EmptyBorder(5, 5, 5, 5)));
	JPanel p3 = get(new JPanel(new GridLayout(3, 1, 0, 10)));
	JPanel p4 = get(new JPanel(new GridLayout(3, 1, 0, 10)));
	JPanel p5 = get(new JPanel(new FlowLayout(FlowLayout.RIGHT)));
	
	JButton btn1 = get(new JButton("사진 등록"));
	JButton btn2 = get(new JButton("회원가입"));
	JButton btn3 = get(new JButton("취소"));
	
	JTextField txt1  = new JTextField();
	JTextField txt2  = new JTextField();
	JTextField txt3  = new JTextField();
	
	JLabel img = get(new JLabel(""), set(new LineBorder(Color.black)));
	
	ArrayList<ArrayList<String>> list = new ArrayList<>();
	
	File file = new File("");
	JFileChooser fc;
	
	public UserInsert() {
		
		SetFrame(this, "회원가입", DISPOSE_ON_CLOSE, 450, 250);
		design();
		action();
		setVisible(true);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				new Login();
			}
		});
		
	}

	@Override
	public void design() {
		
		add(p1, "West");
		add(p2);
		
		p1.add(img);
		p1.add(btn1, "South");
		
		p2.add(p3, "West");
		p2.add(p4);
		p2.add(p5, "South");
		
		p3.add(new JLabel("이름 :"));
		p3.add(new JLabel("ID :"));
		p3.add(new JLabel("PW :"));
		
		p4.add(txt1);
		p4.add(txt2);
		p4.add(txt3);
		
		p5.add(btn2);
		p5.add(btn3);
		
	}

	@Override
	public void action() {
		
		btn1.addActionListener(e->{
			
			fc = new JFileChooser();
			fc.setCurrentDirectory(new File(file("회원사진/")));
			
			int a = fc.showOpenDialog(this);
			
			if (a == 0) {
				
				file = fc.getSelectedFile();
				
				img.setIcon(new ImageIcon(new ImageIcon(file.toString()).getImage().getScaledInstance(140, 200, 4)));
				repaint();
				
			}
			
		});
		
		btn2.addActionListener(e->{
			
			if (txt1.getText().isBlank() || txt2.getText().isBlank() || txt3.getText().isBlank()) {
				err("빈칸이 있습니다.");
			}else {
				
				Query("select * from user where u_id = ?", list, txt2.getText());
				
				if (!list.isEmpty()) {
					err("이미 존재하는 아이디입니다.");
				}else if (Pattern.matches("^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[~!@#$%^&+=]).{4,}$", txt3.getText()) == false) {
					err("비밀번호를 확인해주세요.");
				}else if (file.toString().isBlank()) {
					err("사진을 등록해주세요.");
				}else {
					
					jop("회원가입이 완료되었습니다.");
					
					Update("insert into user values(null, ?,?,?,null)", txt1.getText(), txt2.getText(), txt3.getText());
					Query("select u_no from user order by u_no desc limit 1", list);
					
					Saveimg(file.toString(), list.get(0).get(0));
					
					try {
						ImageIO.write(ImageIO.read(file), "jpg", new File(file("회원사진/" + list.get(0).get(0) + ".jpg")));
					} catch (Exception e2) {
						System.out.println(e2);
					}
					
					dispose();
					new Login();
					
				}
				
			}
			
		});
		
		btn3.addActionListener(e->{
			dispose();
			new Login();
		});
		
	}

}
