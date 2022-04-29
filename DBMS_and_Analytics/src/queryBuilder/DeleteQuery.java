package queryBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import fileUtilities.FileUtility;

public class DeleteQuery {

	public void deleteRowWithSingleWhere(String path, String tablename, Map<String, String> condition) {
		File file = new File(path + "\\" + tablename + ".txt");
		File tmpFile = new File(path + "\\" + "temp.txt");
		try {
			Scanner sc = new Scanner(file);
			FileWriter writer = new FileWriter(path + "\\" + "temp.txt", true);
			while (sc.hasNext()) {
				String line = sc.next();
				FileUtility util = new FileUtility();
				List<String> columns = util.getTableColumnsInOrder(path, tablename);
				String colToCheck = (String) condition.keySet().toArray()[0];
				for (String columnToCheck : condition.keySet()) {
					int position = 0;
					for(String col: columns) {
						if( colToCheck.equals(col)){
							position = columns.indexOf(col);
						}
					}
					if (columns.contains(columnToCheck)) {
						String value = condition.get(columnToCheck);
						String[] lineArr = line.split("\\|");
						if (!lineArr[position].equals(value)) {
							writer.append(line);
							writer.append(System.lineSeparator());
						}

					}
				}

			}
			writer.close();
			sc.close();
			boolean isDeleted = file.delete();
			if(isDeleted) {
				System.out.println("Deleted the file");
			}
			else {
				System.out.println("Could not delete the file");
			}
			File renamedFile = new File(path + "\\" + tablename + ".txt");
			boolean flag = tmpFile.renameTo(renamedFile);
			if(flag) {
				System.out.println("Record deleted successfuly");
			}
			else {
				System.out.println("Could not rename file. Please check temp.txt");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
