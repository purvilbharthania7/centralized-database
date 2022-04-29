package metadataBuilder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import QueryValidation.QueryValidation;
import logs.Logger;
import main.ApplicationMenu;

public class Metadata {
	ApplicationMenu app = new ApplicationMenu();
	String user = app.getUserName();
	
	public void CreateMetadataFile(String path, String database) {
		try {
			String columnString = "Table|Column|Data type|Primary Key|Foreign key|Foreign table\n";
			File newFile = new File(path + "\\"+database+"_metadata.txt");
			FileWriter fw = new FileWriter(newFile);
			fw.write(columnString);
			fw.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void createMetadata(String query, String path, String database) {
		
		
		QueryValidation queryValidation = new QueryValidation();
		
        LocalDateTime startTime = LocalDateTime.now();
		HashMap<String,ArrayList<String>> getQueryData = new HashMap<String,ArrayList<String>>();
		ArrayList <String> queryData = new ArrayList <String>();
		getQueryData=queryValidation.getQueryData(query);
		String columnString = "";

		try {
			FileWriter metadataFile = new FileWriter(path + "\\"+database+"_metadata.txt",true);
			BufferedWriter metadataWriter = new BufferedWriter(metadataFile);
			queryData = getQueryData.get("Create");
			String primary = "";
			String foreign = "";
			String references = "";
			
			if(query.toUpperCase().contains("PRIMARY") && query.toUpperCase().contains("FOREIGN")) {
				primary = queryData.get(2);
				foreign = queryData.get(3);
				references = queryData.get(4);
			}
			else if(query.toUpperCase().contains("PRIMARY")) {
				primary = queryData.get(2);
			}
			else if(query.toUpperCase().contains("FOREIGN")) {
				foreign = queryData.get(2);
				references = queryData.get(3);
			}
			
			String[] columnsInfo = queryData.get(1).split(",");
			
			for(String splitColumnsInfo : columnsInfo) {
				//System.out.println(splitColumnsInfo);
				splitColumnsInfo = splitColumnsInfo.trim();
				String flagP = "";
				String flagF = "";
				String flagR = "";
				String[] column = splitColumnsInfo.split(" ");
				if(column[0].contains(primary)) {
					flagP = primary;
				}
				if(column[0].contains(foreign)) {
					flagF = foreign;
					flagR = references;
				}
				columnString += queryData.get(0)+"|"+column[0]+"|"+column[1]+"|"+flagP+"|"+flagF+"|"+flagR+"\n";
			}
			LocalDateTime endTime = LocalDateTime.now();
			long timeLapsed = Duration.between(startTime, endTime).toMillis();
			Logger logger = new Logger();
			logger.setLoggingDetails("metadata", database, LocalDateTime.now(), user,  timeLapsed,"",false,"");
			metadataWriter.write(columnString);
			metadataWriter.close();
			metadataFile.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void DropTableFromMetadata(String query, String path, String database) {
		QueryValidation queryValidation = new QueryValidation();
		HashMap<String,ArrayList<String>> getQueryData = new HashMap<String,ArrayList<String>>();
		Logger logger = new Logger();
        LocalDateTime startTime = LocalDateTime.now();
		ArrayList <String> queryData = new ArrayList <String>();
		getQueryData = queryValidation.getQueryData(query);
		queryData = getQueryData.get("drop table");
		String tableName = queryData.get(0).toLowerCase();
		File metadataFile = new File(path + "\\"+database+"_metadata.txt");
		File tmpFile = new File(path + "\\" + "temp.txt");
		try {
			FileWriter writer = new FileWriter(path + "\\" + "temp.txt", true);
			Scanner metadataFileReader = new Scanner(metadataFile);
			while(metadataFileReader.hasNextLine()) {
				String data = metadataFileReader.nextLine();
				if(data.toLowerCase().startsWith(tableName) == false) {
					writer.append(data+System.lineSeparator());
				}
			}
			System.out.println("Deleted data in metadata file successfully");
			LocalDateTime endTime = LocalDateTime.now();
			long timeLapsed = Duration.between(startTime, endTime).toMillis();
			logger.setLoggingDetails("metadata", database, LocalDateTime.now(),user,timeLapsed,"",false,"");
			writer.close();
			metadataFileReader.close();
			metadataFile.delete();
			File renamedFile = new File(path + "\\"+database+"_metadata.txt");
			tmpFile.renameTo(renamedFile);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
