package queryBuilder;

import java.io.File;

public class DropTable {

	public void dropTable(String path, String tableName) {
		File file = new File(path+"\\"+tableName+".txt");
		boolean isDropped = file.delete();
		if(isDropped) {
			System.out.println("Table dropped");
		}else {
			System.out.println("Error while dropping table");
		}
	}
}
