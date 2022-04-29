package analytics;

import fileUtilities.FileUtility;
import logs.Logger;
import main.ApplicationMenu;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Analytics {
    private static final String UPDATE = "UPDATE";
    private static final String INSERT = "INSERT";
    private static final String DELETE = "DELETE";
    private static final String CREATE = "CREATE";

    public void readFileAndPerformAnalytics() throws IOException, ParseException {
        int i;
        FileUtility fileUtil = new FileUtility();
        Map<String, Integer> updateMap = new HashMap<>();
        Map<String, Integer> insertMap = new HashMap<>();
        Map<String, Integer> deleteMap = new HashMap<>();
        Map<String, Integer> createMap = new HashMap<>();
        int queryCount = 0, operationCount = 0;
        Logger logger = new Logger();
        String input, databaseName = null, operation = null, userName;

        FileReader reader = new FileReader(logger.getPathToBeWrittenTo());
        JSONParser parser = new JSONParser();
        JSONArray jsonArray = new JSONArray();
        jsonArray = (JSONArray) parser.parse(reader);

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter query");
        input = sc.nextLine();
        System.out.println("Logged in user");
        userName = ApplicationMenu.getUserName();
        System.out.println(userName);
        String[] splitInputQuery = input.split(" ");
        databaseName = splitInputQuery[2].replace(";", "").toUpperCase();
        for (i = 0; i < splitInputQuery.length; i++) {
            if (UPDATE.contains(splitInputQuery[i].toUpperCase())) {
                operation = splitInputQuery[i];
            }
            if (INSERT.contains(splitInputQuery[i].toUpperCase())) {
                operation = splitInputQuery[i];
            }
            if (DELETE.contains(splitInputQuery[i].toUpperCase())) {
                operation = splitInputQuery[i];
            }
            if (CREATE.contains(splitInputQuery[i].toUpperCase())) {
                operation = splitInputQuery[i];
            }
        }

        System.out.println("STARTING ANALYTICS");
        for (i = 0; i < jsonArray.size(); i++) {
            if (jsonArray.get(i).toString().contains(userName)) {
                if (null == operation && ((JSONObject) jsonArray.get(i)).get("Database Name").toString().equalsIgnoreCase(databaseName)) {
                    queryCount++;
                } else {
                    if (jsonArray.get(i).toString().toUpperCase().contains(databaseName) && null != operation) {
                        JSONObject jsonObjectToStoreData = (JSONObject) jsonArray.get(i);
                        String actionPerformed = jsonObjectToStoreData.get("Action").toString();
                        String tableName = jsonObjectToStoreData.get("Table Name").toString();
                        Integer count;
                        if (actionPerformed.equalsIgnoreCase(UPDATE)) {
                            count = updateMap.get(tableName);
                            if (updateMap.containsKey(tableName)) {
                                updateMap.put(tableName, count + 1);
                            } else updateMap.put(tableName, 1);
                        }
                        if (actionPerformed.equalsIgnoreCase(INSERT)) {
                            count = insertMap.get(tableName);
                            if (insertMap.containsKey(tableName)) {
                                insertMap.put(tableName, count + 1);
                            } else insertMap.put(tableName, 1);
                        }

                        if (actionPerformed.equalsIgnoreCase(DELETE)) {
                            count = deleteMap.get(tableName);
                            if (deleteMap.containsKey(tableName)) {
                                deleteMap.put(tableName, count + 1);
                            } else deleteMap.put(tableName, 1);
                        }
                        if (actionPerformed.equalsIgnoreCase(CREATE)) {
                            count = createMap.get(tableName);
                            if (createMap.containsKey(tableName)) {
                                createMap.put(tableName, count + 1);
                            } else createMap.put(tableName, 1);
                        }
                    }
                }
            }
        }
        String finalOperation = operation;
        File file = new File("./src/analytics/analytics.txt");
        FileWriter fileWriter = new FileWriter(file, true);
        if (null != finalOperation) {

            if (finalOperation.equalsIgnoreCase("Update")) {
                updateMap.entrySet().forEach(update -> {
                    System.out.println(update.getValue() + " " + finalOperation + " operations on " + update.getKey()+" submitted by "+userName);
                    try {
                        fileWriter.write(update.getValue() + " " + finalOperation + " operations on " + update.getKey()+" submitted by "+userName + "\n");
                        fileWriter.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            } else if (finalOperation.equalsIgnoreCase("Insert")) {
                insertMap.entrySet().forEach(insert -> {
                    System.out.println(insert.getValue() + " " + finalOperation + " operations on " + insert.getKey()+" submitted by "+userName);

                    try {
                        fileWriter.write(insert.getValue() + " " + finalOperation + " operations on " + insert.getKey() +" submitted by "+userName+ "\n");
                        fileWriter.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            } else if (finalOperation.equalsIgnoreCase("Delete")) {
                deleteMap.entrySet().forEach(delete -> {
                    System.out.println(delete.getValue() + " " + finalOperation + " operations on " + delete.getKey()+" submitted by "+userName);
                    try {
                        fileWriter.write(delete.getValue() + " " + finalOperation + " operations on " + delete.getKey()+" submitted by "+userName + "\n");
                        fileWriter.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

            }
            if (finalOperation.equalsIgnoreCase("Create")) {
                createMap.entrySet().forEach(create -> {
                    System.out.println(create.getValue() + " " + finalOperation + " operations on " + create.getKey()+" submitted by "+userName);
                    try {
                        fileWriter.write(create.getValue() + " " + finalOperation + " operations on " + create.getKey() +" submitted by "+userName +"\n");
                        fileWriter.flush();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
            fileWriter.close();
        }
        if (queryCount > 0) {
            System.out.println(queryCount + " queries submitted on " + databaseName);
            fileWriter.write(queryCount + " queries submitted on " + databaseName + "\n");
            fileWriter.flush();

        }


    }

}

