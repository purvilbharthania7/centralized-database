package QueryValidation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryValidation {

	public HashMap<String,ArrayList<String>> getQueryData(String query){
                ArrayList <String> queryData = new ArrayList <String>();
				HashMap<String,ArrayList<String>> getQueryData = new HashMap<String,ArrayList<String>>();
				if (query.contains("select")){
				final String regex = "select\\s(.*)\\sfrom\\s(.*)\\swhere\\s(.*)([=|<|>|<=|>=])(.*);";
				

				final Pattern pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
				final Matcher matcher = pattern.matcher(query);

				while (matcher.find()) {
					

					for (int i = 1; i <= matcher.groupCount(); i++) {
						
                        queryData.add(matcher.group(i));
					}
					
				}
				getQueryData.put("Select",queryData);
				}
				else if (query.contains("update")){
					final String regex = "update\\s(.*)set(.*)[=](.*)\\swhere\\s(.*)([=|<|>|<=|>=])(.*);";
					
	
					final Pattern pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
					final Matcher matcher = pattern.matcher(query);
	
					while (matcher.find()) {
						System.out.println("Full match: " + matcher.group(0));
	
						for (int i = 1; i <= matcher.groupCount(); i++) {
							
                            queryData.add(matcher.group(i));
						}
					}
					getQueryData.put("Update",queryData);
				}
				else if(query.contains("delete")){
					final String regex = "delete\\sfrom\\s(.*)where\\s(.*)([=|<|>|<=|>=])(.*);";
				

					final Pattern pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
					final Matcher matcher = pattern.matcher(query);

					while (matcher.find()) {
						System.out.println("Full match: " + matcher.group(0));

					for (int i = 1; i <= matcher.groupCount(); i++) {
						
                        queryData.add(matcher.group(i));
					}

					}
					getQueryData.put("Delete",queryData);
				}
				else if(query.contains("create database")){
					final String regex = "create\\sdatabase\\s(.*);";
				

					final Pattern pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
					final Matcher matcher = pattern.matcher(query);

					while (matcher.find()) {
						System.out.println("Full match: " + matcher.group(0));

					for (int i = 1; i <= matcher.groupCount(); i++) {
						
                        queryData.add(matcher.group(i));
					}

					}
					getQueryData.put("create database",queryData);
				}
				else if(query.contains("drop table")){
					final String regex = "drop\\stable\\s(.*);";
				

					final Pattern pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
					final Matcher matcher = pattern.matcher(query);

					while (matcher.find()) {
						System.out.println("Full match: " + matcher.group(0));

					for (int i = 1; i <= matcher.groupCount(); i++) {
						
                        queryData.add(matcher.group(i));
					}

					}
					getQueryData.put("drop table",queryData);
				}
				else if(query.startsWith("use") || query.startsWith("USE") || query.startsWith("Use")){
					final String regex = "use\\s(.*);";
				

					final Pattern pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
					final Matcher matcher = pattern.matcher(query);

					while (matcher.find()) {
						System.out.println("Full match: " + matcher.group(0));

					for (int i = 1; i <= matcher.groupCount(); i++) {
						
                    	queryData.add(matcher.group(i));
					}

					}
					getQueryData.put("use",queryData);
				}
				else if (query.toUpperCase().contains("CREATE TABLE")){
					final String regex = "create table\\s*(.*)\\s*\\(+?";
					final Pattern pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
					final Matcher matcher = pattern.matcher(query);
					
					while(matcher.find()) {
						String[] table = matcher.group(1).split(" ");
						
						queryData.add(table[0]);   
						
					}
					if(query.toUpperCase().contains("PRIMARY") && query.toUpperCase().contains("FOREIGN")) {
						final String regex1 = "\\((.*)\\,primary key \\((.*)\\s*\\)\\,foreign key \\((.*)\\)\\sreferences\\s(.*)\\(";
						final Pattern pattern1 = Pattern.compile(regex1,Pattern.CASE_INSENSITIVE);
						final Matcher matcher1 = pattern1.matcher(query);
			
						while (matcher1.find()) {
							for (int i = 1; i <= matcher1.groupCount(); i++) {
								
			                 queryData.add(matcher1.group(i));
							}	
						}
						
						getQueryData.put("Create",queryData);
					}
					else if(query.toUpperCase().contains("PRIMARY")) {
						final String regexp = "\\((.*)\\,PRIMARY KEY \\((.*)\\)";
						final Pattern patternp = Pattern.compile(regexp,Pattern.CASE_INSENSITIVE);
						final Matcher matcherp = patternp.matcher(query);
			
						while (matcherp.find()) {
							for (int i = 1; i <= matcherp.groupCount(); i++) {
								
			                 queryData.add(matcherp.group(i).replace(")", ""));
							}
							
						}
						
						getQueryData.put("Create",queryData);
					}
					else if(query.toUpperCase().contains("FOREIGN KEY")) {
						final String regex1 = "\\((.*)\\,FOREIGN KEY \\((.*)\\)\\sreferences\\s(.*)\\(";
						final Pattern pattern1 = Pattern.compile(regex1,Pattern.CASE_INSENSITIVE);
						final Matcher matcher1 = pattern1.matcher(query);
			
						while (matcher1.find()) {
							for (int i = 1; i <= matcher1.groupCount(); i++) {
								
			                 queryData.add(matcher1.group(i));
							}
							
						}
						
						getQueryData.put("Create",queryData);
					}
					else {
						final String regex1 = "\\((.*)\\)";
						final Pattern pattern1 = Pattern.compile(regex1,Pattern.CASE_INSENSITIVE);
						final Matcher matcher1 = pattern1.matcher(query);
			
						while (matcher1.find()) {
							for (int i = 1; i <= matcher1.groupCount(); i++) {
								
			                 queryData.add(matcher1.group(i));
							}
							
						}
						
						getQueryData.put("Create",queryData);
					}
				}
				else if (query.toUpperCase().contains("INSERT")){
					final String regex = "insert into\\s*(.*)\\s*\\((.*?)\\)\\svalues\\s\\((.*)\\).*";
					final Pattern pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
					final Matcher matcher = pattern.matcher(query);

					while (matcher.find()) {
						for (int i = 1; i <= matcher.groupCount(); i++) {
							
		                 queryData.add(matcher.group(i));
						}
						
					}
					
					getQueryData.put("Insert",queryData);
					}                
				
                return getQueryData;

        
        
    }

}
