// JDBC Imports
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import oracle.jdbc.pool.OracleDataSource;
import oracle.jdbc.OracleConnection;
import java.sql.DatabaseMetaData;

import java.util.Scanner;

import javax.swing.*;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;

public class CustomerInterface extends JPanel{
	private OracleConnection connection;
	private JButton b1;
	public CustomerInterface(OracleConnection connection){
		this.connection = connection;
		b1 = new JButton("Hello");
		b1.setMnemonic(KeyEvent.VK_D);
    	b1.setActionCommand("disable");
		setLayout(new FlowLayout());
		add(b1);
	}

	public void run(){
		System.out.println("-- CustomerInterface --");
		this.login();
	}

	public void login(){
		Scanner in = new Scanner(System.in);
		System.out.println("Enter tax id:");
		String id = in.nextLine();
		System.out.println("Enter 4 digit PIN:");
		String pin = in.nextLine();
		Customer cust = Customer.login(id, pin, this.connection);
		if(cust == null){
			System.out.println("Verification failed... Are your id/PIN correct?");
		}else{
			System.out.println("User: " + cust.name + " logged in!");
		}
	}

	public void deposit(){

	}

	public void top_up(){

	}

	public void withdrawal(){

	}

	public void purchase(){

	}
}