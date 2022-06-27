package Project;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import Base.Base;

public class Login extends JFrame implements Base{
	
	public static String id = "";
	
	JPanel p1 = get(new JPanel(new BorderLayout(10, 0)), set(new EmptyBorder(10, 10, 10, 10)));
	JPanel p2 = get(new JPanel(new BorderLayout()), set(new EmptyBorder(0, 10, 10, 10)));
	JPanel p3 = get(new JPanel(new GridLayout(2, 1, 0, 10)), set(60, 0));
	JPanel p4 = get(new JPanel(new GridLayout(2, 1, 0, 10)));
	
	JTextField txt1 = new JTextField();
	JTextField txt2 = new JTextField();
	
	JButton btn1 = get(new JButton("로그인"));
	JButton btn2 = get(new JButton("회원가입"), set(90, 25));
	
	JCheckBox ck1 = new JCheckBox("아이디 저장");
	
	JLabel lab1 = get(new JLabel("Orange Ticket", 0));
	
	public Login() {
		
		SetFrame(this, "로그인", DISPOSE_ON_CLOSE, 350, 200);
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
		
		lab1.setFont(new Font("", Font.BOLD + Font.ITALIC, 30));
		
		add(lab1, "North");
		add(p1);
		add(p2, "South");
		
		p1.add(p3, "West");
		p1.add(p4);
		p1.add(btn1, "East");
		
		p2.add(ck1);
		p2.add(btn2, "East");
		
		p3.add(new JLabel("ID"));
		p3.add(new JLabel("PW"));
		
		p4.add(txt1);
		p4.add(txt2);
		
		if (!id.isBlank()) {
			ck1.setSelected(true);
			txt1.setText(id);
		}
		
	}

	@Override
	public void action() {
		
		btn1.addActionListener(e->{
			
			if (txt1.getText().isBlank() || txt2.getText().isBlank()) {
				err("빈칸이 존재합니다.");
			}else {
				
				Query("select * from user where u_id = ? and u_pw = ?", member, txt1.getText(), txt2.getText());
				
				if (member.isEmpty()) {
					err("ID 또는 PW가 일치하지 않습니다.");
				}else {
					
					jop(member.get(0).get(1) + "님 환영합니다.");
					
					if (ck1.isSelected()) {
						id = txt1.getText();
					}else {
						id = "";
					}
					
					dispose();
					new Main();
					
				}
				
			}
			
		});
		
		btn2.addActionListener(e->{
			dispose();
			new UserInsert();
		});
		
	}

}
