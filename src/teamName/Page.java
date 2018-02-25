package teamName;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import teamName.Tuple;

public class Page implements Serializable {

	/**
	 *	Having a fixed serialVersionUID to be able to Deserialize objects on any version of java
	 */
	private static final long serialVersionUID = 1L;
		
	private ArrayList<Tuple> rows;
	private int numOfRows;
	private String pageName;
	private boolean deleted;
	
	public boolean isDeleted() {
		return deleted;
	}
	public Page(String pageName) {
				
		// Supported Types:
		// java.lang.Integer, java.lang.String, java.lang.Double, 
		// java.lang.Boolean and java.util.Date
		
		this.pageName = pageName;
		this.rows = new ArrayList<Tuple>();
		this.numOfRows = rows.size();
						
	}
	public void deleteRow(Hashtable<String, Object> htblColNameValue){
		//TODO 
		deleted = false;
		for (Tuple tuple : rows) {
			if(tuple.colNameValue == htblColNameValue){
				rows.remove(tuple);
				deleted = true;
			}
		}
	}
	public void insertRow(Hashtable<String, Object> htblColNameValue) {
		
	    Tuple tuple = new Tuple(htblColNameValue);
	    rows.add(tuple);
	    this.numOfRows++;
	    
	}
			
	public int getNumOfRows() {
		return numOfRows;
	}

	public String getPageName() {
		return pageName;
	}
	
	public void serializePage(String tablePath) {
		
		//File tableDir = new File("../tableName/");
		//tablePath.mkdirs();
		
		try {
			
            FileOutputStream fos = new FileOutputStream(tablePath);
            ObjectOutputStream oos;
            oos = new ObjectOutputStream(fos);
            oos.writeObject(this);
            oos.close();
            fos.close();
	        
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static Page deserializePage(String pagePath) {
		
		Page page = null;
		
		try {
	        
            FileInputStream fis = new FileInputStream(pagePath);
            ObjectInputStream ois;
            ois = new ObjectInputStream(fis);
            page = (Page) ois.readObject();
            ois.close();
            fis.close();
            
        } catch (Exception e) {
            //TODO Auto-generated catch block
            e.printStackTrace();
        }
		
		return page;
	}

	public void printPage() {
		
		System.out.println("---Page--- " + pageName + " : " + numOfRows + " rows: ");
		for(Tuple row : rows) {
			row.printTuple();
		}
		
	}
	
	private Tuple findTuple(String strKey , String primaryKeyName) {
		Tuple reqTuple = null;
		for (Tuple tuple : rows) {
			if(tuple.colNameValue.get(primaryKeyName).equals(strKey)) {
				reqTuple = tuple;
				break;
			}
				
		}
		return reqTuple;
	}
	
	public void updateRow(String strKey, String primaryKeyName ,Hashtable<String, Object> htblColNameValue) {
		// TODO page updateRow
		Tuple tuple = findTuple(strKey, primaryKeyName);
		ArrayList<String> keys = (ArrayList<String>) htblColNameValue.keySet();
		
		if(tuple != null) {
			for (String key : keys) {  //updating each item in the hashtable of the tuple according to the provided hashtable
				tuple.colNameValue.remove(key);
				tuple.colNameValue.put(key, htblColNameValue.get(key));
			}
		}
		
		tuple.colNameValue.remove("TouchDate");
		tuple.colNameValue.put("TouchDate", new Date());		
					
	}


}