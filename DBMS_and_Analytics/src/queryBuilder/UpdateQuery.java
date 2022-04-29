package queryBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import fileUtilities.FileUtility;
import sqlDumpGeneration.SqlDumpGenerator;

public class UpdateQuery {

	public void updateRecordsWithSingleWhere(String path, String tablename, Map<String, String> condition,
			Map<String, String> keyValueToUpdate, String query, String dbName) {
		try {
			File file = new File(path + "\\" + tablename + ".txt");
			Scanner sc = new Scanner(file);
			StringBuffer buffer = new StringBuffer();
			FileUtility util = new FileUtility();
			List<String> columns = util.getTableColumnsInOrder(path, tablename);
			boolean found = false;
			while (sc.hasNextLine()) {
				buffer.append(sc.nextLine() + System.lineSeparator());
			}
			String fileContents = buffer.toString();
			sc.close();
			Scanner scTemp = new Scanner(file);
			for (String keyToUpdate : condition.keySet()) {
				int position = 0;
				int j = 0;
				for (String col : columns) {
					if (col.equals((keyValueToUpdate.keySet().toArray())[0])) {
						position = j;
						break;
					}
					j++;
				}
				while (scTemp.hasNextLine()) {
					String line = scTemp.next();
					if (columns.contains(keyToUpdate)) {
						String existingConditionValue = condition.get(keyToUpdate);

						if (line.contains(existingConditionValue)) {
							String[] lineArray = line.split("\\|");
							for (int i = 0; i < lineArray.length; i++) {
								if (lineArray[i].equals(existingConditionValue)) {
									found = true;
									String valueToChange = keyValueToUpdate
											.get((keyValueToUpdate.keySet().toArray())[0]);
									String lineToChange = line.replace(lineArray[position], valueToChange);
//									fileContents = fileContents.toLowerCase().replaceFirst(line, lineToChange);
									fileContents = fileContents.replaceFirst(Pattern.quote(line), Matcher.quoteReplacement(lineToChange));
									fileContents = fileContents.strip();
									break;
								}
							}

						}
					}
				}

			}
			if (found == true) {
				FileWriter writer = new FileWriter(path + "\\" + tablename + ".txt");
				SqlDumpGenerator updateQueryInDump = new SqlDumpGenerator();
				if(!updateQueryInDump.checkQueryExists(path,query,dbName)) {
					updateQueryInDump.insertQueriesInDumpText(path, query, dbName);
				}
				writer.append(fileContents);
				writer.flush();

				System.out.println("Record updated. ");
			}

			else {
				System.out.println("Could not find a matching record. Please check your SQL statement");
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchElementException e) {
			e.printStackTrace();
		}
	}

}
