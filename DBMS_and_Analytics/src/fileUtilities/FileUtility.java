package fileUtilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;



public class FileUtility {
   static String path;

    //Set directory for using database
    public String setDatabaseDirectory(String databaseName) {

        File file = new File(".");
        System.out.println("Current Working Directory: " + System.getProperty("user.dir"));
        System.setProperty("user.dir", System.getProperty("user.dir") + "\\src\\DatabaseManagementSystem\\" + databaseName);
        System.out.println("New Current Working Directory: " + System.getProperty("user.dir"));
        path = System.getProperty("user.dir");

        return path;
    }

    public String getDatabaseDirectory() {
        return path;
    }

    public List<String> getTableColumnsInOrder(String path, String tablename) {
        Scanner sc = null;
        String[] colArray = null;

		try {
			sc = new Scanner(new File(path+"\\"+tablename+".txt"));
			String row = sc.nextLine();
			colArray = row.split("\\|");
			return Arrays.asList(colArray);
		}
		catch (FileNotFoundException | NoSuchElementException e) {
			e.printStackTrace();
			return Arrays.asList(colArray);
		}
		finally {
			sc.close();
		}

	}

}
