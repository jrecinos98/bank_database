package cs174a;


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

public class CustomerInterface {
	private OracleConnection connection;

	public CustomerInterface(OracleConnection connection){
		this.connection = connection;
	}

	public void run(){
		System.out.println("-- CustomerInterface --");
		String resp = Utilities.prompt(
			"1) to create a customer\n" +
			"2) to login as existing customer\n" +
			"3) to update PIN\n" +
			"4) to delete a customer\n" +
			"5) to create database tables\n" + 
			"6) to destroy database tables\n" +
			"7) to deposit money in an account\n" +
			"8) to withdraw money from an account \n" +
			"9) to top up pocket account\n"
		);
		if(resp.equals("1")){
			this.create_cust();
		}else if(resp.equals("2")){
			this.login();
		}else if(resp.equals("3")){
			this.change_pin();
		}else if(resp.equals("4")){
			this.delete_cust();
		}else if(resp.equals("5")){
			this.create_tables();
		}else if(resp.equals("6")){
			this.destroy_tables();
		}else if(resp.equals("7")){
			this.deposit();
		}else if(resp.equals("8")){
			this.withdrawal();
		}else if(resp.equals("9")){
			this.top_up();
		}
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

	public void create_cust(){
		String tin = Utilities.prompt("Enter c_id:");
		String name = Utilities.prompt("Enter c_name:");
		String address = Utilities.prompt("Enter address:");
		Customer cust = Customer.create_customer(tin, name, address, this.connection);
		if(cust == null){
			System.out.println("Creation failed... ");
		}else{
			System.out.println("User: " + cust.name + " created!");
		}
	}

	public void change_pin(){
		String id = Utilities.prompt("Enter c_id:");
		String old = Utilities.prompt("Enter old pin:");
		String _new = Utilities.prompt("Enter new pin:");
		if(Customer.update_pin(id, old, _new, this.connection)){
			System.out.println("PIN updated!");
			Customer cust = Customer.get_cust_by_id(id, this.connection);
			if(cust != null){
				System.out.println("Successfully set pin to " + cust.encrypted_pin);
				return;
			}
		}
		System.out.println("Failed to set pin");
	}

	public void delete_cust(){
		String id = Utilities.prompt("Enter c_id:");
		if(Customer.del_cust_by_id(id, this.connection)){
			System.out.println("Successfully removed customer!");
		}
	}

	public void create_tables(){
		if(DBSystem.execute_queries_from_file("./scripts/create_db.sql", this.connection)){
			System.out.println("Successfully created tables");
		}else{
			System.out.println("Error creating tables");
		}
	}

	public void destroy_tables(){
		if(DBSystem.execute_queries_from_file("./scripts/destroy_db.sql", this.connection)){
			System.out.println("Successfully destroyed tables");
		}else{
			System.out.println("Error destroying tables");
		}
	}

	public void deposit(){
		String to_acct = Utilities.prompt("Enter a_id:");
		String cust_id = Utilities.prompt("Enter c_id:");
		String date = Bank.get_date();
		Transaction.TransactionType type = Transaction.TransactionType.DEPOSIT;
		double amount = Double.parseDouble(Utilities.prompt("Enter amount:"));

		boolean success = Transaction.deposit(to_acct, cust_id, date, type, amount, connection);
		if(!success){
			System.err.println("Deposit failed");
		}else{
			System.out.println("Deposit success!");
		}
	}

	public void top_up(){
		String to_acct = Utilities.prompt("Enter pocket id:");
		String from_acct = Utilities.prompt("Enter link id:");
		String cust_id = Utilities.prompt("Enter c_id:");
		String date = Bank.get_date();
		Transaction.TransactionType type = Transaction.TransactionType.TOP_UP;
		double amount = Double.parseDouble(Utilities.prompt("Enter amount:"));

		Transaction transaction = Transaction.top_up(to_acct, from_acct, date, amount, cust_id, connection);
		if(transaction == null){
			System.err.println("Top-Up failure");
		}else{
			System.out.println("Top-Up success!");
		}
	}

	public void withdrawal(){
		String from_acct = Utilities.prompt("Enter a_id:");
		String cust_id = Utilities.prompt("Enter c_id:");
		String date = Bank.get_date();
		Transaction.TransactionType type = Transaction.TransactionType.WITHDRAWAL;
		double amount = Double.parseDouble(Utilities.prompt("Enter amount:"));

		boolean success = Transaction.withdraw(from_acct, cust_id, date, type, amount, connection);
		if(!success){
			System.err.println("Withdrawal failed");
		}else{
			System.out.println("Withdrawal success!");
		}
	}

	public void purchase(){

	}
}