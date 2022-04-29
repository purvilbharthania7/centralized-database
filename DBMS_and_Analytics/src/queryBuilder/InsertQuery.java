package queryBuilder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

import fileUtilities.FileUtility;
import sqlDumpGeneration.SqlDumpGenerator;

public class InsertQuery {

	public void insertQueryProcessor(String path, String tablename, Map<String, String> valuesToInsert, String query, String dbName) {
		Scanner sc;
		FileWriter fw;
		try {
			sc = new Scanner(new File(path + "\\" + tablename + ".txt"));
			fw = new FileWriter(path + "\\" + tablename + ".txt", true);
			BufferedWriter writer = new BufferedWriter(fw);
			FileUtility util = new FileUtility();
			List<String> columnNames = util.getTableColumnsInOrder(path, tablename);
			List<String> finalValuesList = new ArrayList<String>(Collections.nCopies(columnNames.size(), null));
			for (String key : valuesToInsert.keySet()) {
				int indexOfColumn = columnNames.indexOf(key);
				if (finalValuesList.size() <= indexOfColumn) {
					finalValuesList.add(null);
				}
				finalValuesList.set(indexOfColumn, valuesToInsert.get(key).toString());
			}
			if(!tablename.equals("user_profile")) {
				SqlDumpGenerator insertQueryInDump = new SqlDumpGenerator();
				if(!insertQueryInDump.checkQueryExists(path,query,dbName)) {
					insertQueryInDump.insertQueriesInDumpText(path, query, dbName);
				}
			}
			System.out.println("Writing data to the database..");
			writer.write("\n");
			int i = 1;
			for (String value : finalValuesList) {
				value = value.replace("\'", "");
				if(i<finalValuesList.size()) {
					writer.write(value + "|");
				}
				else {
					writer.write(value);
				}
				i++;
			}
			sc.close();
			writer.close();
			System.out.println("Values inserted to table.");
		} catch (IOException | NoSuchElementException e) {
			e.printStackTrace();
		}
	}
}
