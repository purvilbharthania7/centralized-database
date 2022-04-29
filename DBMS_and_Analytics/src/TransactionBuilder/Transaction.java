package TransactionBuilder;

import QueryValidation.QueryValidation;
import main.IdentifyQuery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Transaction {

   
    public HashMap<String, ArrayList<String>> checkLines(String transactionLines,HashMap<String, ArrayList<String>> returnStoreQueries ) throws Exception{
        QueryValidation queryValidation = new QueryValidation();

        HashMap<String,ArrayList<String>> getQueryData = new HashMap<String,ArrayList<String>>();
       
		getQueryData=queryValidation.getQueryData(transactionLines);
		if(getQueryData.isEmpty()) {
			throw new Exception("Error in query");
		}
        return getQueryData;



	}


	public void commitTransaction(String path, ArrayList<HashMap> getAllQueries, String query, String dbName) {
		IdentifyQuery identifyQuery = new IdentifyQuery();
		try {
			for (int i = 0; i < getAllQueries.size(); i++) {
				identifyQuery.identifyAndProcess(path, getAllQueries.get(i), query, dbName);
			}
		} catch (Exception e) {
			System.out.println("Invalid query in transaction");
		}

	}


}
