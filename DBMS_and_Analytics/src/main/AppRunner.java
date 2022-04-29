package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import TransactionBuilder.Transaction;
import fileUtilities.*;
import metadataBuilder.Metadata;
import queryBuilder.*;

public class AppRunner {

	public static void main(String[] args) throws Exception {
		// Transaction transaction = new Transaction();
		// transaction.checkTransaction("Start Transaction;");
		// transaction.checkLines("Select * from table_name where key=value;");
		// transaction.checkLines("create database db1;");

		// transaction.checkLines("CREATE TABLE customers (id int(10),name
		// varchar(50),city varchar(50),PRIMARY KEY (id )); ");
		// transaction.checkLines("INSERT INTO Customers (CustomerName, ContactName,
		// Address, City, PostalCode, Country) VALUES ('Cardinal', 'Tom B. Erichsen',
		// 'Skagen 21', 'Stavanger', '4006', 'Norway');");
		// transaction.checkLines( "Update1customers set name='bob', city='london' where
		// id=10;");
		// transaction.checkLines("delete from customers where id=101;");
		// transaction.checkLines("drop table customers;");
		ApplicationMenu applicationMenu = new ApplicationMenu();
		boolean isLoggedin = applicationMenu.userScreen();
		if (isLoggedin) {
//			applicationMenu.setSelectedMenu(1);
//			applicationMenu.useOrCreateDB();
//			applicationMenu.displayMenu();
			applicationMenu.routeToSelection();
		}else {
			System.out.println("Sorry, you entered wrong details. Application terminated.");
		}
		// transaction.checkLines("Update table_name SET column=value where
		// key=value;");
		// FileUtility f = new FileUtility();
		// String path = f.setDatabaseDirectory("Database1");
		// transaction.checkLines("INSERT INTO Customers (CustomerName, ContactName,
		// Address, City, PostalCode, Country) VALUES ('Cardinal', 'Tom B. Erichsen',
		// 'Skagen 21', 'Stavanger', '4006', 'Norway');");
		// transaction.checkLines( "Update1customers set name='bob', city='london' where
		// id=10;");
		// transaction.checkLines("delete from customers where id=101;");
		// transaction.checkLines("drop table customers;");
		// ApplicationMenu applicationMenu= new ApplicationMenu();
		// applicationMenu.setSelectedMenu(1);
		// applicationMenu.displayMenu();
		// applicationMenu.routeToSelection();

		// transaction.checkLines("Update table_name SET column=value where
		// key=value;");


		// SelectQueries select = new SelectQueries();
		// select.selectWhereClauseQueryProcessor(path, "users", "password=Dal@123");
		// InsertQuery insert = new InsertQuery();
		// Map<String, String> valuesToInsert = new HashMap<String, String>();
		// valuesToInsert.put("username", "adminuser");
		// valuesToInsert.put("password", "admin");
		// insert.insertQueryProcessor(path, "users", valuesToInsert);
		// UpdateQuery update = new UpdateQuery();
		// Map<String, String> condition = new HashMap<String, String>();
		// condition.put("password", "Dal@123");
		// Map<String, String> valuesToUpdate = new HashMap<String, String>();
		// valuesToUpdate.put("password", "ruchi@123");
		// update.updateRecordsWithSingleWhere(path, "users", condition,
		// valuesToUpdate);
		// DeleteQuery delete = new DeleteQuery();
		// delete.deleteRowWithSingleWhere(path, "users", valuesToUpdate);
		// CreateDatabase createDB = new CreateDatabase();
		// String path = createDB.createDatabase("TestDB");
		// CreateTable createTable = new CreateTable();
		// List<String> listColumns = new ArrayList<String>();
		// listColumns.add("id");
		// listColumns.add("courseName");
		// listColumns.add("faculty");
		// createTable.createTable("test", path, listColumns);
		// DropTable deleteTable = new DropTable();
		// deleteTable.dropTable(path, "users");

	}

}
