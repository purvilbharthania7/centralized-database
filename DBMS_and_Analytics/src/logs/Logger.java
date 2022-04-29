package logs;

import fileUtilities.FileUtility;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.time.LocalDateTime;

public class Logger {

    String action;
    String databaseName;
    LocalDateTime timestamp;
    String user;
    String output;
    long timeLapsed;
    String tableName;
    FileUtility fileUtility = new FileUtility();
    String filePath = "./src/SystemLogs/logs.json";
    JSONArray jsonArray = new JSONArray();
    JSONObject jsonObject = new JSONObject();
    JSONParser jsonParser = new JSONParser();


    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public long getTimeLapsed() {
        return timeLapsed;
    }

    public void setTimeLapsed(long timeLapsed) {
        this.timeLapsed = timeLapsed;
    }

    public void setLoggingDetails(String action, String databaseName, LocalDateTime timestamp, String user, long timeLapsed,String tableName,boolean crashed,String query) throws IOException {
        jsonObject.put("Action", action);
        jsonObject.put("Database Name", databaseName);
        jsonObject.put("Timestamp", String.valueOf(timestamp));
        jsonObject.put("User", user);
        jsonObject.put("Time taken in milliseconds", String.valueOf(timeLapsed));
        jsonObject.put("Table Name",tableName);
        jsonObject.put("Crashed",String.valueOf(crashed));
        jsonObject.put("Query",query);
        writeToFile();
    }

    public void writeToFile() throws IOException {
        File file = new File(filePath);
        if (file.exists()) {
            InputStream inputStream = new FileInputStream(file);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            JSONParser parser = new JSONParser();
            try {
                jsonArray = (JSONArray) parser.parse(bufferedReader);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        jsonArray.add(jsonObject);
        writeJsonFile(jsonArray);
    }

    private void writeJsonFile(JSONArray jsonArray) throws IOException {
        File file = new File(filePath);
        try (FileWriter fw = new FileWriter(file, false)) {
            fw.write(jsonArray.toJSONString());
        }
    }

    public String getPathToBeWrittenTo() {
        return filePath;
    }

    public void setPathToBeWrittenTo(String filePath) {
        this.filePath = filePath;
    }
}