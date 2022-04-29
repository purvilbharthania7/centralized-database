package main;

import QueryValidation.QueryValidation;
import TransactionBuilder.Transaction;
import User.Login;
import User.UserRegistration;
import analytics.Analytics;
import erdiagram.ERD;
import fileUtilities.FileUtility;
import metadataBuilder.Metadata;
import org.json.simple.parser.ParseException;
import queryBuilder.CreateDatabase;
import queryBuilder.DropTable;
import queryBuilder.InsertQuery;
import sqlDumpGeneration.SqlDumpGenerator;
import User.Login;
import User.UserRegistration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class ApplicationMenu {

	int menuId;

    static String userName;
    static String dbName;


	public boolean userScreen() {
		UserRegistration userRegistration = new UserRegistration();
		boolean isLoggedin = false;
		Scanner sc = new Scanner(System.in);
		FileUtility fileUtility = new FileUtility();
		String path = System.getProperty("user.dir") + "/src/DatabaseManagementSystem/Login";
		System.out.println("Login path:" + path);
		boolean toContinue = false;
		do {
			System.out.println("Please select an option from below:\n 1. Registration\n 2. Login");
			String selection = sc.nextLine();
			HashMap<String, String> userDetails = new HashMap<String, String>();
			switch (selection) {
			case "1":
				System.out.println("Please enter your user name");
				userName = sc.nextLine();
				userDetails.put("username", userName);
				System.out.println("Please enter your password");
				String userPassword = sc.nextLine();
				String hashedPassword = UserRegistration.encryptPassword(userPassword);
				userDetails.put("password", hashedPassword);
				System.out.println("Please enter your security answer");
				System.out.println("What is your school name?");
				userDetails.put("question1", "WhatIsYourSchoolName?");
				String userSecurityAnswer1 = sc.nextLine();
				userDetails.put("answer1", userSecurityAnswer1);
				InsertQuery insertQuery = new InsertQuery();
				insertQuery.insertQueryProcessor(path, "user_profile", userDetails, "", "");
				System.out.println("You have been registered successfuly. Please login to proceed.");
				System.out.println("Do you want to continue?");
				String input = sc.nextLine();
				if (input.equalsIgnoreCase("y")) {
					toContinue = true;
				}
				break;
			case "2":
				System.out.println("Enter your username: ");
				userName = sc.nextLine();
				System.out.println("Enter your password: ");
				String password = sc.nextLine();
				System.out.println("What is your school name?");
				String securityAnswer = sc.nextLine();
				isLoggedin = new Login().login(path, userName, password, securityAnswer);
				if (isLoggedin) {
					System.out.println("You have been logged in successfully.");
				} else {
					System.out.println("Incorrect username or password.");
				}
				break;
			default:
				System.out.println("Invalid option entered. Terminating.");
				break;
			}
		} while (toContinue && !isLoggedin);
		return isLoggedin;
	}

	public void setSelectedMenu(int menuId) {
		this.menuId = menuId;
	}

	public void displayMenu() {
		System.out.println(
				"Please select one from the below options: \n 1. Write Queries \n 2. Export\n 3. Data Model\n 4. Analytics");
	}

	public void useOrCreateDB() {
		Scanner sc = new Scanner(System.in);
		FileUtility f = new FileUtility();
		String databaseName = "";
		dbName = "";
		String path = "";
		String queryType = "";
		QueryValidation queryValidation = new QueryValidation();
		boolean toContinue = false;
		do {
			System.out.println("Please select a database by using Use <databasename>; create a new one.");
			String dbNameUseQuery = sc.nextLine();

			// Get the user query and it's components in a Map of ArrayList
			HashMap<String, ArrayList<String>> getUseQueryData = new HashMap<String, ArrayList<String>>();
			getUseQueryData = queryValidation.getQueryData(dbNameUseQuery);

			// Create an array of type of queries i.e select, update, delete etc.
			Object[] queryArray = getUseQueryData.keySet().toArray();

			// Get the type of query
			queryType = (String) queryArray[0];

			// Get parameters of query type
			Object[] queryParamsList = getUseQueryData.get(queryType).toArray();

			if (queryParamsList.length != 0) {
				dbName = (String) queryParamsList[0];
			}
			// set database path based on user input.

			switch (queryType) {
			case "create database":
				databaseName = (String) queryParamsList[0];
				CreateDatabase create = new CreateDatabase();
				create.createDatabase(databaseName);
				System.out.println("Do you want to continue? y/n?");
				String input = sc.nextLine();
				if (input.equalsIgnoreCase("y")) {
					toContinue = true;
				}
				break;
			case "use":
				databaseName = (String) queryParamsList[0];
				path = f.setDatabaseDirectory(databaseName);
				break;
			default:
				System.out.println("Invalid query entered.");
				break;
			}
		} while (!toContinue && !queryType.equals("use"));
	}

	public void routeToSelection() throws Exception {
		Scanner sc = new Scanner(System.in);
		FileUtility f = new FileUtility();
		String databaseName = "";
		 dbName = "";
		String queryType = "";
		String path = "";
		QueryValidation queryValidation = new QueryValidation();

		// user must use database or create database before performing any operation.
		boolean toContinue = false;
		do {
			System.out.println("Please select a database by using Use <databasename>; create a new one.");
			String dbNameUseQuery = sc.nextLine();

			// Get the user query and it's components in a Map of ArrayList
			HashMap<String, ArrayList<String>> getUseQueryData = new HashMap<String, ArrayList<String>>();
			getUseQueryData = queryValidation.getQueryData(dbNameUseQuery);

			// Create an array of type of queries i.e select, update, delete etc.
			Object[] queryArray = getUseQueryData.keySet().toArray();

			// Get the type of query
			queryType = (String) queryArray[0];

			// Get parameters of query type
			Object[] queryParamsList = getUseQueryData.get(queryType).toArray();

			if (queryParamsList.length != 0) {
				dbName = (String) queryParamsList[0];
			}
			// set database path based on user input.

			switch (queryType) {
			case "create database":
				databaseName = (String) queryParamsList[0];
				CreateDatabase create = new CreateDatabase();
				create.createDatabase(databaseName);
				System.out.println("Do you want to continue? y/n?");
				String input = sc.nextLine();
				if (input.equalsIgnoreCase("y")) {
					toContinue = true;
				}
				break;
			case "use":
				databaseName = (String) queryParamsList[0];
				path = f.setDatabaseDirectory(databaseName);
				break;
			default:
				System.out.println("Invalid query entered.");
				break;
			}
		} while (toContinue && queryType.equals("create database"));

		do {
			// display menu and take user input.
			displayMenu();
			menuId = Integer.parseInt(sc.nextLine());

			switch (this.menuId) {
			case 1:

				System.out.println("Please enter a SQL query.");
				String query = sc.nextLine();
				if (query.equalsIgnoreCase("start transaction;")) {
					processTransactionFromInput(path, query, dbName);
				}
				HashMap<String, ArrayList<String>> getQueryData = new HashMap<String, ArrayList<String>>();
				getQueryData = queryValidation.getQueryData(query);
				if (getQueryData.size() == 0) {
					if (query.equalsIgnoreCase("start transaction;")) {
						break;
					}
					System.out.println("Error in query");
					break;
				} else {
					IdentifyQuery identifyQuery = new IdentifyQuery();
					identifyQuery.identifyAndProcess(path, getQueryData, query, dbName);
				}
				break;

			case 2:
				SqlDumpGenerator sg = new SqlDumpGenerator();
				 sg.createDump(path,dbName);
				 System.out.println("Successfully created sql dump for "+dbName);
				break;

			case 3:

				boolean status;
				try {
					status = ERD.createERD(dbName);
					if(status)
					{
						System.out.println("ERD generation passed.");
					}
					else
					{
						System.out.println("ERD generation failed");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				
				break;
				case 4:
					Analytics analytics = new Analytics();
					try {
						analytics.readFileAndPerformAnalytics();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ParseException e) {
						e.printStackTrace();
					}
					break;

				default:

				System.out.println("Please enter a valid option");
				this.displayMenu();

			}
			System.out.println("Do you want to continue? Press y for yes and n for no.");

		} while (sc.nextLine().equalsIgnoreCase("y"));
	}

	public void processTransactionFromInput(String path, String query, String dbName) throws Exception {
		HashMap<String, ArrayList<String>> tempStoreQuery = new HashMap<String, ArrayList<String>>();
		Integer transactionEndFlag = 0;
		List<String> queries = new ArrayList<String>();
		Scanner sc = new Scanner(System.in);
		ArrayList<HashMap> passQuery = new ArrayList<HashMap>();
		if (query.equals("start transaction;")) {
			Transaction transaction = new Transaction();
			do {
				query = sc.nextLine();
				 queries.add(query);
				if (query.equals("commit;")) {
					transaction.commitTransaction(path, passQuery, query, dbName);
					for(String a:queries) {
						if(a.toLowerCase().contains("create")) {
							SqlDumpGenerator createQueryInDump = new SqlDumpGenerator();
							if(!createQueryInDump.checkQueryExists(path,a,dbName)) {
								createQueryInDump.insertQueriesInDumpText(path, a, dbName);
								Metadata createMd = new Metadata();
								createMd.createMetadata(a,path,dbName);
							}
						}
						else if (a.toLowerCase().contains("insert") || a.toLowerCase().contains("update")) {
							SqlDumpGenerator createQueryInDump = new SqlDumpGenerator();
							if(!createQueryInDump.checkQueryExists(path,a,dbName)) {
								createQueryInDump.insertQueriesInDumpText(path, a, dbName);
								
							}
						}
					}
					transactionEndFlag = 1;
				} else if (query.equals("rollback;")) {
					passQuery.clear();
					transactionEndFlag = 1;

				} else {
					tempStoreQuery = transaction.checkLines(query, tempStoreQuery);
					if (tempStoreQuery.isEmpty()) {
						throw new Exception("Error in query");
					}
					if (!tempStoreQuery.isEmpty()) {
						passQuery.add(tempStoreQuery);
					}
					else {
						System.out.println("Error in query");
						break;
					}
				}

				if (tempStoreQuery.size() == 0 && transactionEndFlag == 0) {
					System.out.println("Error in query");
					break;
				}

			} while (transactionEndFlag != 1);
		}
	}

	public static String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public static String getDbName() {
		return dbName;
	}

	public static void setDbName(String dbName) {
		ApplicationMenu.dbName = dbName;

	}
}
