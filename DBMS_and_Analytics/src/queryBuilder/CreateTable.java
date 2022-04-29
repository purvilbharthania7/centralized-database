package queryBuilder;

import logs.Logger;
import metadataBuilder.Metadata;
import sqlDumpGeneration.SqlDumpGenerator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class CreateTable {

    public void createTable(String tableName, String path, List<String> columns, String query, String dbName) {
        File table = new File(path + "\\" + tableName + ".txt");
//        Logger logger = new Logger();
        LocalDateTime startTime = LocalDateTime.now();
        try {
            FileWriter fw = new FileWriter(table);
            String columnString = "";
            for (int i = 0; i < columns.size(); i++) {
                if (i == columns.size() - 1) {
                    columnString += columns.get(i);
                } else {
                    columnString += columns.get(i) + "|";
                }
            }
            if(!query.toLowerCase().contains("commit")) {
            	SqlDumpGenerator createQueryInDump = new SqlDumpGenerator();
    			if(!createQueryInDump.checkQueryExists(path,query,dbName)) {
    				createQueryInDump.insertQueriesInDumpText(path, query, dbName);
    				Metadata createMd = new Metadata();
    				createMd.createMetadata(query,path,dbName);
    			}
            }
            
            fw.write(columnString);
            System.out.println("Table created.");
            fw.close();
            LocalDateTime endTime = LocalDateTime.now();
            long timeLapsed = Duration.between(startTime, endTime).toMillis();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
