package erdiagram;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashSet;
import java.util.Set;

public class ERD {

    public static boolean createERD(String databaseName) throws Exception{
        // Take current directory path
        String ROOT_PATH=System.getProperty("user.dir");
        String ERD_PATH=System.getProperty("user.dir");
        //Get the metadata file
        ROOT_PATH = ROOT_PATH.concat("/"+databaseName+"_metadata.txt");

        File metadataFile = new File(ROOT_PATH);

        //Check if databse exists with this database
        if (!metadataFile.exists()) {
            System.out.println(ROOT_PATH);
            System.out.println("Database with name: " + databaseName + " not found");
            return false;
        }

        //Check if database has table or not
        if(metadataFile.length() < 47)
        {
            System.out.println("No tables exist in database with name: " + databaseName);
            return false;
        }

        //Path of the ERD file
        ERD_PATH = ERD_PATH.concat("/"+databaseName+"_ERD.txt");

        File erdFile = new File(ERD_PATH);

        //If file doesn't exist create one
        if (erdFile.createNewFile()) {
            System.out.println("File created: " + erdFile.getName());
        }

        Formatter erdFormatter= new Formatter(new FileOutputStream(ERD_PATH));

        //read the file
        BufferedReader fileReader = new BufferedReader(new FileReader(metadataFile));
        String line;
        String tableName;
        String oldtableName = null;
        boolean tableStausflag = false;
        String[] columns;
        String[] subcolumns;
        line = fileReader.readLine();



        String headingSeparator =
                "------------------------------------------------------------------------------------------------------------------------------------------------------------";


        // Read each line and process that line
        while ((line = fileReader.readLine()) != null) {

            String foreignTable="-",foreignKey="-",primaryKey="-",foreignColumn="-",cardinality="-",size="-";
            //Split the line by "|" operator
            columns = line.split("\\|");
            tableName = columns[0];

            //Flag will be set if table changes
            if(!tableName.equals(oldtableName))
            {
                tableStausflag = true;
            }
            String columnName = columns[1];
            String columnType = columns[2];

            //if datatyape is varchar extract the size from it
            if(columnType.length()> 3)
            {
                subcolumns = columns[2].split("\\(");
                columnType= subcolumns[0];
                size = subcolumns[1].substring(0,subcolumns[1].length()-1);
            }

            // if new table comes Start from new line
            if(tableStausflag)
            {
                oldtableName = tableName;
                System.out.println("\n");
                erdFormatter.format("\n");
                String dName = "Schema: ".concat(databaseName);
                String tName = "Table: ".concat(tableName);
                System.out.format("%20s%30s\n", dName, tName);
                erdFormatter.format("%20s%30s\n", dName, tName);
                System.out.println(headingSeparator);
                erdFormatter.format(headingSeparator.concat("\n"));
                System.out.format("%20s%20s%15s%20s%20s%20s%20s%20s\n", "Column Name",
                        "Data type", "Size","Primary Key", "Foreign Key", "Foreign Column", "Foreign Table", "Cardinality");
                erdFormatter.format("%20s%20s%15s%20s%20s%20s%20s%20s\n", "Column Name",
                        "Data type", "Size","Primary Key", "Foreign Key", "Foreign Column", "Foreign Table", "Cardinality\n");
                System.out.println("\n");
                tableStausflag=false;
            }

            //Check for primary and foreign key
            if (columns.length > 3) {
                if (columns[3] == "") {
                    foreignTable = columns[5];
                    foreignColumn = columns[4];

                    //open table file to check for cardinality
                    String PATH=System.getProperty("user.dir")+"/"+tableName+".txt";
                    File foreignFile = new File(PATH);
                    BufferedReader readForeignFile = new BufferedReader(new FileReader(foreignFile));
                    ArrayList<String> values=new ArrayList<>();
                    String line1 = readForeignFile.readLine();
                    String[] tablecolumn=line1.split("\\|");
                    int ref = 0;

                    for(int i=0;i<tablecolumn.length;i++)
                    {

                        if(tablecolumn[i].equals(foreignColumn))
                        {
                            ref=i;
                        }
                    }

                    while ((line1 = readForeignFile.readLine()) != null){
                        tablecolumn=line1.split("\\|");
                        values.add(tablecolumn[ref]);
                    }

                    //Compare the size and assign the cardinality
                    Set<String> uniqueValues = new HashSet<String>(values);
                    if(uniqueValues.size()== values.size())
                    {
                        cardinality = "1:1";
                    }
                    else
                    {
                        cardinality = "1:N";
                    }
                    foreignKey = "FK";

                } else {
                    primaryKey = "PK";
                }
            }

            System.out.format("%20s%20s%15s%20s%20s%20s%20s%20s\n", columnName,
                    columnType, size,primaryKey, foreignKey, foreignColumn,
                    foreignTable, cardinality);
            erdFormatter.format("%20s%20s%15s%20s%20s%20s%20s%20s\n", columnName,
                    columnType, size,primaryKey, foreignKey, foreignColumn,
                    foreignTable, cardinality);
            System.out.println("\n");
            erdFormatter.format("\n");
        }
        erdFormatter.close();
        return true;
    }
}
