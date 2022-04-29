package sqlDumpGeneration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import QueryValidation.QueryValidation;
import fileUtilities.FileUtility;
import logs.Logger;
import main.ApplicationMenu;

public class SqlDumpGenerator {
	
	ApplicationMenu app = new ApplicationMenu();
	String user = app.getUserName();
	
	public void createSqlDumpFile(String path, String database) {
		try {
			File newFile = new File(path + "\\"+database+"_SqlQueries.txt");
			FileWriter fw = new FileWriter(newFile);
			fw.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean checkQueryExists(String path, String query, String database) {
		boolean flag = false;
		try {
			
			File dumpFile = new File(path + "\\"+database+"_SqlQueries.txt");
			Scanner dumpScan = new Scanner(dumpFile);
			while(dumpScan.hasNextLine()) {
				String line = dumpScan.nextLine();
				if(line.toLowerCase().equals(query.toLowerCase())) {
					flag = true;
				}
			}
			dumpScan.close();

		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	public void insertQueriesInDumpText(String path, String query, String database) {
		try {
			FileWriter fw = new FileWriter(path + "\\"+database+"_SqlQueries.txt",true);
			BufferedWriter dumpWriter = new BufferedWriter(fw);
			String write = query+"\n";
			dumpWriter.write(write);
			dumpWriter.close();
			fw.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void createDump(String path, String database) {
		QueryValidation queryValidation = new QueryValidation();
		HashMap<String,ArrayList<String>> getQueryData = new HashMap<String,ArrayList<String>>();
		ArrayList <String> queryData = new ArrayList <String>();
		String dump = "";
		Logger logger = new Logger();
	    LocalDateTime startTime;
	    LocalDateTime endTime;
	 try {
		 	FileWriter writer = new FileWriter(path + "\\" +database+ "_sqlDump.sql");
			BufferedWriter sqlDumpWriter = new BufferedWriter(writer);
			startTime = LocalDateTime.now();
			dump += "--\n-- SQL dump created for "+database+" at "+LocalDateTime.now()+"\n--\n\n\n";
			dump += "--------------------------------------------------\n\n";
		 	File metadataFile = new File(path + "\\"+database+"_SqlQueries.txt");
		 	writer.write(dump);
			sqlDumpWriter.close();
			writer.close();
		 	Scanner metadataFileReader = new Scanner(metadataFile);
		 	
			while(metadataFileReader.hasNextLine()) {
				
				String line = metadataFileReader.nextLine();
				if(line.toLowerCase().contains("create")) {
					getQueryData=queryValidation.getQueryData(line);
					queryData = getQueryData.get("Create");	
					generateDump(path, queryData.get(0),database, startTime);
				}
			}
			
	 }
	 catch(Exception e) {
			e.printStackTrace();
		}				 
	}
	
	public void generateDump(String path, String tableName, String database, LocalDateTime startTime) {
		File metadataFile = new File(path + "\\"+database+"_SqlQueries.txt");
		String dump ="";
		String insertDump = "";
		String updateDump = "";
		Logger logger = new Logger();
	    LocalDateTime endTime;
		try {
			FileWriter writer = new FileWriter(path + "\\" +database+ "_sqlDump.sql", true);
			BufferedWriter sqlDumpWriter = new BufferedWriter(writer);
			
			dump += "--\n-- Table structure for table `"+tableName+"`\n--\n\n\n";
		 	Scanner metadataFileReader = new Scanner(metadataFile);
			while(metadataFileReader.hasNextLine()) {
				String line = metadataFileReader.nextLine();
				if(line.toLowerCase().contains(tableName.toLowerCase())) {
					
					if(line.toLowerCase().contains("create table "+tableName.toLowerCase())) {
						String[] data = line.split(tableName,2);
						dump += data[0].toUpperCase()+"IF NOT EXISTS `"+tableName+"`\n";
						String[] columns = data[1].split(",");
						for(String col : columns) {
							if(col.endsWith(";")) {
								dump += col +"\n\n";
							}
							else dump += col +",\n";
						}
					}
					if(line.toLowerCase().contains("insert into "+tableName.toLowerCase())) {
						String[] data = line.toUpperCase().split("VALUES");
						insertDump += data[0] +"VALUE\n" +data[1]+"\n";	
					}
					
					if(line.toLowerCase().contains("update "+tableName.toLowerCase())) {
						updateDump += line+"\n\n\n";
					}
				}
				
			}
			dump += "--\n-- Dumping data for table `"+tableName+"`\n--\n\n";
			dump += insertDump+"\n\n";
			dump += "--\n-- Updates done on data for table `"+tableName+"`\n--\n\n";
			dump += updateDump;
			sqlDumpWriter.append(dump);
			endTime = LocalDateTime.now();
			long timeLapsed = Duration.between(startTime, endTime).toMillis();
			logger.setLoggingDetails("Export", database, LocalDateTime.now(),user,timeLapsed,"",false,"");
			sqlDumpWriter.close();
			writer.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
