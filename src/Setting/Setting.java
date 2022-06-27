package Setting;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;

import Base.Base;

public class Setting implements Base{

	ArrayList<ArrayList<String>> list = new ArrayList<>();
	
	public Setting() {
		
		try {
			
			Connection c= DriverManager.getConnection("jdbc:mysql://localhost/?serverTimezone=UTC&allowLoadLocalInfile=true", "root", "1234");
			Statement s = c.createStatement();
			
			s.executeUpdate("drop database if exists 2021전국");
			s.executeUpdate("create database if not exists 2021전국");
			s.executeUpdate("use 2021전국");
			s.executeUpdate("create table user(u_no int primary key auto_increment, u_name varchar(10), u_id varchar(20), u_pw varchar(20), u_img blob)");
			s.executeUpdate("create table perform(p_no int primary key auto_increment, pf_no varchar(10), p_name varchar(20), p_place varchar(20), p_price int, p_actor varchar(20), p_date date)");
			s.executeUpdate("create table ticket(t_no int primary key auto_increment, u_no int, p_no int, t_seat varchar(50), t_discount varchar(50), foreign key(u_no) references user(u_no), foreign key(p_no) references perform(p_no))");
			s.executeUpdate("drop user if exists user@'localhost'");
			s.executeUpdate("create user if not exists user@'localhost' identified by '1234'");
			s.executeUpdate("grant select, update, insert, delete on 2021전국.* to user@'localhost'");
			s.executeUpdate("set global local_infile = 1");
			
			String st[] = "user, perform, ticket".split(", ");
			for (int i = 0; i < st.length; i++) {
				s.executeUpdate("load data local infile 'Datafiles/" + st[i] + ".txt' into table " + st[i] + " lines terminated by '\r\n' ignore 1 lines");
			}
			
			Query("SELECT * FROM 2021전국.user;", list);
			for (int i = 0; i < list.size(); i++) {
				Saveimg(file("회원사진/" + list.get(i).get(0) + ".jpg"), list.get(i).get(0));
			}
			
			jop("세팅 완료");
			
		} catch (Exception e) {
			System.out.println(e);
		}
		
	}

	@Override
	public void design() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		
	}
	public static void main(String[] args) {
		new Setting();
	}
}
