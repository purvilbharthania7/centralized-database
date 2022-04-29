package main;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fileUtilities.FileUtility;
import logs.Logger;
import metadataBuilder.Metadata;
import queryBuilder.CreateDatabase;
import queryBuilder.CreateTable;
import queryBuilder.DeleteQuery;
import queryBuilder.DropTable;
import queryBuilder.InsertQuery;
import queryBuilder.SelectQueries;
import queryBuilder.UpdateQuery;
import sqlDumpGeneration.SqlDumpGenerator;

public class IdentifyQuery {
	public void identifyAndProcess(String path, HashMap<String, ArrayList<String>> queryMap, String query, String dbName) {
		LocalDateTime start, end;
		Logger logger = new Logger();
		
		Object[] queryArray = queryMap.keySet().toArray();
		String queryType = (String) queryArray[0];
		Object[] queryParamsList = queryMap.get(queryType).toArray();
		String tableName = "";
		String databaseName = "";
		switch (queryType) {
		case "Select":
			start= LocalDateTime.now();
			String particularCols = (String) queryParamsList[0];
			List<String> particularColsList = new ArrayList<String>();
			tableName = (String) queryParamsList[1];
			String condition = (String) queryParamsList[2] + queryParamsList[3] + queryParamsList[4];
			SelectQueries processQuery = new SelectQueries();
			if(particularCols.equals("*")) {
				processQuery.selectWhereClauseQueryProcessor(path, tableName, condition, particularColsList);
			}
			else {
				String[] cols = particularCols.split(", ");
				Collections.addAll(particularColsList, cols);
				processQuery.selectWhereClauseQueryProcessor(path, tableName, condition, particularColsList);
			}
			end=LocalDateTime.now();
			try {
				logger.setLoggingDetails("Select",dbName,LocalDateTime.now(),ApplicationMenu.getUserName(), Duration.between(start,end).toMillis(),tableName,false,query);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case "Update":
			start = LocalDateTime.now();
			tableName = (String) queryParamsList[0];
			tableName = tableName.strip();
			Map<String, String> keysToUpdate = new HashMap<String, String>();
			keysToUpdate.put(((String) queryParamsList[1]).strip(), (String) queryParamsList[2]);
			Map<String, String> conditionClause = new HashMap<String, String>();
			conditionClause.put((String) queryParamsList[3], (String) queryParamsList[5]);
			UpdateQuery updateQuery = new UpdateQuery();
			updateQuery.updateRecordsWithSingleWhere(path, tableName, conditionClause, keysToUpdate, query, dbName);

			end = LocalDateTime.now();
			try {
				logger.setLoggingDetails("Update",dbName,LocalDateTime.now(),ApplicationMenu.getUserName(), Duration.between(start,end).toMillis(),tableName,false,query);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
			case "Delete":
				start =LocalDateTime.now();
			tableName = (String) queryParamsList[0];
			tableName = tableName.strip();
			Map<String, String> conditionToDelete = new HashMap<String, String>();
			conditionToDelete.put((String) queryParamsList[1], (String) queryParamsList[3]);
			DeleteQuery deleteQuery = new DeleteQuery();
			deleteQuery.deleteRowWithSingleWhere(path, tableName, conditionToDelete);

			break;
		case "drop table":

			Metadata dropMd = new Metadata();
			dropMd.DropTableFromMetadata(query,path,dbName);
			start =LocalDateTime.now();

			tableName = (String) queryParamsList[0];
			DropTable dropTable = new DropTable();
			dropTable.dropTable(path, tableName);
			end = LocalDateTime.now();
			try {
				logger.setLoggingDetails("Drop",dbName,LocalDateTime.now(),ApplicationMenu.getUserName(), Duration.between(start,end).toMillis(),tableName,false,query);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case "create database":
			start =LocalDateTime.now();
			databaseName = (String) queryParamsList[0];
			CreateDatabase create = new CreateDatabase();
			create.createDatabase(databaseName);
			end = LocalDateTime.now();
			try {
				logger.setLoggingDetails("Create Database",dbName,LocalDateTime.now(),ApplicationMenu.getUserName(), Duration.between(start,end).toMillis(),tableName,false,query);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case "use":
			start =LocalDateTime.now();
			FileUtility f = new FileUtility();
			databaseName = (String) queryParamsList[0];
			path = f.setDatabaseDirectory(databaseName);
			end = LocalDateTime.now();
			try {
				logger.setLoggingDetails("Use",dbName,LocalDateTime.now(),ApplicationMenu.getUserName(), Duration.between(start,end).toMillis(),tableName,false,query);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case "Create":
			start =LocalDateTime.now();
			CreateTable createTable = new CreateTable();

			
			tableName = (String) queryParamsList[0];
			List<String> columns = new ArrayList<String>();
				String col = (String) queryParamsList[1];
				String[] colArrSplitByComma = col.split(", ");
				for(int j=0; j<colArrSplitByComma.length; j++) {
					String colName = colArrSplitByComma[j].split(" ")[0].strip();
					columns.add(colName);
				}

			createTable.createTable(tableName, path, columns, query, dbName);
			end = LocalDateTime.now();
			try {
				logger.setLoggingDetails("Create",dbName,LocalDateTime.now(),ApplicationMenu.getUserName(), Duration.between(start,end).toMillis(),tableName,false,query);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case "Insert":


			start = LocalDateTime.now();
			InsertQuery insert = new InsertQuery();
			tableName = (String) queryParamsList[0];
			tableName = tableName.strip();
			Map<String, String> valuesToInsert = new HashMap<String, String>();
			String[] tableCols = ((String) queryParamsList[1]).split(", ");
			String[] values = ((String) queryParamsList[2]).split(", ");
			try {
				for (int i = 0; i < tableCols.length; i++) {
					valuesToInsert.put(tableCols[i], values[i]);
				}
				insert.insertQueryProcessor(path, tableName, valuesToInsert, query, dbName);

			} catch (Exception e) {
				System.out.println("Columns do not match the values.");
				end = LocalDateTime.now();
				try {
					logger.setLoggingDetails("Insert",dbName,LocalDateTime.now(),ApplicationMenu.getUserName(), Duration.between(start,end).toMillis(),tableName,true,query);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			System.out.println(queryParamsList[2]);
			end=LocalDateTime.now();
			try {
				logger.setLoggingDetails("Insert",dbName,LocalDateTime.now(),ApplicationMenu.getUserName(), Duration.between(start,end).toMillis(),tableName,false,query);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		}
	}
}
