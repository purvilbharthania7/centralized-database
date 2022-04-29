package queryBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class SelectQueries {

	public void selectWhereClauseQueryProcessor(String path, String table, String condition,
			List<String> particularCols) {

		System.out.println("\n \nShowing results for select query ... \n");
		Scanner sc = null, scBuffer = null;
		String[] conditionArray = condition.split("=");
		String column = conditionArray[0];
		String value = conditionArray[1];
		try {
			sc = new Scanner(new File(path + "\\" + table + ".txt"));
			scBuffer = new Scanner(new File(path + "\\" + table + ".txt"));
			String line = sc.nextLine();
			String[] colArray = line.split("\\|");
			int requiredColId = 0;
			List<Integer> selectedColIndexList = new ArrayList<Integer>();
			for (int i = 0; i < colArray.length; i++) {
				if (column.equalsIgnoreCase(colArray[i])) {
					requiredColId = i;
				}
				if (!particularCols.isEmpty()) {
					if (particularCols.contains(colArray[i])) {
						selectedColIndexList.add(i);
					}
				}
			}
			// printing columns
			if (!selectedColIndexList.isEmpty()) {
				for (int index : selectedColIndexList) {
					for (int c = 0; c < colArray.length; c++) {
						if (index == c) {
							System.out.print(colArray[c] + " |\t");
						}
					}
				}
			} else {
				for (int c = 0; c < colArray.length; c++) {
					System.out.print(colArray[c] + " |\t");
				}
			}
			System.out.println("\n--------------------------------------------------------");
			// printing only selected rows
			System.out.println("\n");
			scBuffer.nextLine();
			while (scBuffer.hasNext()) {
				String row = scBuffer.nextLine();
				String[] rowArray = row.split("\\|");
				if (value.equals(rowArray[requiredColId])) {
					if (!selectedColIndexList.isEmpty()) {
						for (int index : selectedColIndexList) {
							for (int c = 0; c < rowArray.length; c++) {
								if (index == c) {
									System.out.print(rowArray[c] + " |\t");
								}

							}

						}
					} else {
						for (int c = 0; c < rowArray.length; c++) {
							System.out.print(rowArray[c] + " |\t");
						}
					}

				}
				System.out.println();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			scBuffer.close();
		} catch (NoSuchElementException e) {
			e.printStackTrace();
		} finally {
			sc.close();
		}
	}

}
