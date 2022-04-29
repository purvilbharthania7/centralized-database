package queryBuilder;

import java.io.File;

import metadataBuilder.Metadata;
import sqlDumpGeneration.SqlDumpGenerator;

public class CreateDatabase {
	public String createDatabase(String databaseName) {
		Metadata mdFile = new Metadata();
		
		String path = System.getProperty("user.dir") + "\\src\\DatabaseManagementSystem\\"+databaseName;
		System.out.println("Database Path: "+path);
		File file = new File(path);  
		boolean isCreated = file.mkdir();  
	      if(isCreated){  
	    	  mdFile.CreateMetadataFile(path, databaseName);
	    	  SqlDumpGenerator dumpFile = new SqlDumpGenerator();
	    	  dumpFile.createSqlDumpFile(path, databaseName);
	         System.out.println("Database is created successfully");  
	         return path;
	      }else{  
	         System.out.println("Error Found!");  
	         return null;
	      }  
		
	}
}
