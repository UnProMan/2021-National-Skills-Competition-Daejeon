package Project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Base.Base;

public class Pay extends JFrame implements Base{

	JPanel p1 = get(new JPanel(new BorderLayout()), set(new EmptyBorder(10, 10, 10, 10)));
	JPanel p2 = get(new JPanel(new GridLayout(3, 2, 0, 1)), set(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.gray)));
	JPanel p3 = get(new JPanel(new BorderLayout()), set(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.gray)));
	JPanel p4 = get(new JPanel(new GridLayout(0, 1, 0, 10)));
	JPanel p5 = get(new JPanel(new GridLayout(0, 2, 5, 10)));
	JPanel p6 = get(new JPanel(new FlowLayout(0)));
	
	JLabel lab1 = get(new JLabel("결제", 0), set(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.black)), set(350, 60), setb(20));
	JLabel lab2 = get(new JLabel(""), setf(Color.green));
	
	JButton btn1 = new JButton("본인 인증");
	JButton btn2 = new JButton("결제하기");
	JButton btn3 = new JButton("취소");
	
	ArrayList<String> data;
	
	String seat = "";
	String dis = "";
	
	public Pay(ArrayList<String> data) {
		
		this.data = data;
		
		setTitle("결제");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		design();
		action();
		pack();
		setLocationRelativeTo(null);
		setIconImage(new ImageIcon(file("오렌지.jpg")).getImage());
		setVisible(true);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				new Seat(data, 0);
			}
		});
		
	}

	@Override
	public void design() {
		
		lab1.setVerticalAlignment(JLabel.BOTTOM);
		
		add(lab1, "North");
		add(p1);
		
		p1.add(p2, "North");
		p1.add(p3);
		p1.add(p5, "South");
		
		p2.add(new JLabel("공연명"));
		p2.add(new JLabel(data.get(2), JLabel.RIGHT));
		p2.add(new JLabel("장소"));
		p2.add(new JLabel(data.get(3), JLabel.RIGHT));
		p2.add(new JLabel("날짜"));
		p2.add(new JLabel(data.get(6), JLabel.RIGHT));
		
		p3.add(new JLabel("좌석"), "West");
		p3.add(p4);
		
		int sum = 0;
		
		for (int i = 0; i < select.size(); i++) {
			
			p4.add(new JLabel( select.get(i).get(0) + " : " + df.format(intnum(select.get(i).get(1))) , JLabel.RIGHT));
			
			sum += intnum(select.get(i).get(1));
			seat = seat.isBlank() ? select.get(i).get(0) : seat + "," + select.get(i).get(0);
			dis = dis.isBlank() ? select.get(i).get(2) : dis + "," + select.get(i).get(2);
			
		}
		
		p5.add(new JLabel("총금액"));
		p5.add(new JLabel(df.format(sum), JLabel.RIGHT));
		
		p5.add(p6);
		p5.add(new JLabel(""));
		
		p5.add(btn2);
		p5.add(btn3);
		
		p6.add(btn1);
		p6.add(lab2);
		
	}

	@Override
	public void action() {
		
		btn1.addActionListener(e->{
			
			String a = JOptionPane.showInputDialog(null, "비밀번호를 입력해주세요.", "Input", JOptionPane.QUESTION_MESSAGE);
			
			if (a.contentEquals(member.get(0).get(3))) {
				jop("본인인증이 완료되었습니다.");
				btn1.setEnabled(false);
				lab2.setText("V");
			}else {
				err("비밀번호가 일치하지 않습니다.");
			}
			
		});
		
		btn2.addActionListener(e->{
			
			if (lab2.getText().isBlank()) {
				err("본인인증을 해주세요.");
			}else {
				
				jop("결제가 완료되었습니다.");
				
				Update("insert into ticket values(null, ?,?,?,?)", member.get(0).get(0), data.get(0), seat, dis);
				
				dispose();
				new Main();
				
			}
			
		});
		
		btn3.addActionListener(e->{
			dispose();
			new Seat(data, 0);
		});
		
	}

}
