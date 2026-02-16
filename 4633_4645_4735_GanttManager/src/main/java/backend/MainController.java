package backend;
import dom2app.SimpleTableModel;
import java.util.*;
import java.io.*;

public class MainController implements IMainController{
	
	private final String[] ColumnNames = {"TaskId" , "TaskText", "MamaId","Start" , "End" , "Cost" };

	public String[] getColumnNames() {
		return ColumnNames;
	}
	
	List<String[]> Data = new ArrayList<String[]>();
	List<String[]> sortedData  = new ArrayList<>();
	
	public SimpleTableModel load(String fileName, String delimiter){
		
		try {Data.removeAll(Data);}
        catch(Exception e) {
            System.out.println("Something went wrong");
        }
		
		File tsvFile = new File(fileName);
        
		//initializing a new ArrayList out of Arraylists
        try (BufferedReader TSVReader = new BufferedReader(new FileReader(tsvFile))){
            String line = null;
            while((line = TSVReader.readLine()) != null){
                String[] strArray = {"-","-","-","-","-","-"};
                String[] lineItems = line.split(delimiter);
                for(int i = 0; i< lineItems.length ; i++){
                   strArray[i] = lineItems[i];
                }
                Data.add(strArray);
            }
       }
       catch(Exception e){
           System.out.println("Something went wrong");
       }
        
	    //Calculate Start,End and Cost for complex tasks
	    for(String[] task : Data){
		   int start = Integer.MAX_VALUE;
		   int end = 0;
		   if(task[3].equals("-")){
			   int cost = 0;
			   for(String[] x : Data) {
				   if(x[2].equals(task[0])){
					   if(Integer.parseInt(x[3]) < start){
						   start = Integer.parseInt(x[3]);
					   }
					   if(Integer.parseInt(x[4]) > end){
						   end = Integer.parseInt(x[4]);
					   }
					   cost += Integer.parseInt(x[5]);
				   }
			   }
			   task[3] = Integer.toString(start);
			   task[4] = Integer.toString(end);
			   task[5] = Integer.toString(cost);
		   }
	    }        
        
        //SORTING
        List<String[]> topLevel  = new ArrayList<>();
		List<String[]> lowLevel  = new ArrayList<>();
		//List<String[]> sortedData  = new ArrayList<>();
		
		//add top level tasks to topLevel and low level tasks to lowLevel
		for(String[] task : Data){
			if(task[2].equals("0")){
				topLevel.add(task);
			}
			else {
				lowLevel.add(task);
			}
		}
		
		//sort top level tasks
		int size1 = topLevel.size();
		for(int i = 0; i < size1-1; i++){
			for(int j = 0; j < size1-i-1; j++){
				if(Integer.parseInt(topLevel.get(j)[3]) > Integer.parseInt(topLevel.get(j+1)[3])){
					Collections.swap(topLevel,j,j+1);
				}
				else if(Integer.parseInt(topLevel.get(j)[3]) == Integer.parseInt(topLevel.get(j+1)[3])){
					if(Integer.parseInt(topLevel.get(j)[0]) > Integer.parseInt(topLevel.get(j+1)[0])){
						Collections.swap(topLevel,j,j+1);
					}
				}
			}
		}
		
		//Sort low level tasks
		int size = lowLevel.size();
		for(int i = 0; i < size-1; i++){
			for(int j = 0; j < size-i-1; j++){
				if(Integer.parseInt(lowLevel.get(j)[3]) > Integer.parseInt(lowLevel.get(j+1)[3])){
					Collections.swap(lowLevel,j,j+1);
				}
				else if(Integer.parseInt(lowLevel.get(j)[3]) == Integer.parseInt(lowLevel.get(j+1)[3])){
					if(Integer.parseInt(lowLevel.get(j)[0]) > Integer.parseInt(lowLevel.get(j+1)[0])){
						Collections.swap(lowLevel,j,j+1);
					}
				}
			}
		}
		
		//add the sorted tasks of topLovel and lowLevel to sortedTasks in the right order
		//for each top level task add that an its correspondent lowLevel tasks
		for(String[] topTask: topLevel){
			sortedData.add(topTask);
			for(String[] lowTask: lowLevel){
				if(Integer.parseInt(lowTask[2]) == Integer.parseInt(topTask[0])){
					sortedData.add(lowTask);
				}
			}
		}
		
		SimpleTableModel result = new SimpleTableModel("Loaded tsv", "Gantt", ColumnNames,sortedData);
		return result;
		
	}
	 
	
	
	public SimpleTableModel getTasksByPrefix(String prefix){
		
		List<String[]> Dataz = new ArrayList<String[]>();
		for(String[] i: sortedData) {
			if(i[1].substring(0,prefix.length()).equals(prefix.substring(0,1).toUpperCase() + prefix.substring(1))){
				Dataz.add(i);
			}
		}
		if(Dataz.isEmpty()){
			// return a SimpleTableModel that only contains a "No tasks found with that Prefix" string
	        String[] msg = {"No tasks found with that Prefix.","-","-","-","-","-"};
	        Dataz.add(msg);
	    }
		SimpleTableModel result = new SimpleTableModel("filter for "+prefix, "Gantt", ColumnNames,Dataz);
		return result;
	}
	

	public  SimpleTableModel getTaskById(int id){ 
		String id1 = Integer.toString(id);
		
        List<String[]> Data1 = new ArrayList<String[]>();
        
        for(String[] task : sortedData){
            if(task[0].equals(id1))
            {
            	Data1.add(task);
            }
        }
        if(Data1.isEmpty())
        {
            // return a SimpleTableModel that only contains a "No task found with that ID." string
            String[] msg = {"No task found with that ID.","-","-","-","-","-"};
            Data1.add(msg);
        }
        SimpleTableModel table = new SimpleTableModel("Filter for: "+id1, "Gantt", ColumnNames, Data1);
        return table;
       
    }
	
	
	public SimpleTableModel getTopLevelTasks() {
		
		List<String[]> Dataz = new ArrayList<String[]>();
		for(String[] i: sortedData) {
			if(Integer.parseInt(i[2]) == 0) {
				Dataz.add(i);
			}
		}
		
		if(Dataz.isEmpty())
        {
           // return a SimpleTableModel that only contains a "No Top Level Tasks found" string
           String[] msg = {"No Top Level Tasks found","-","-","-","-","-"};
           Dataz.add(msg);
        }
		
		SimpleTableModel result = new SimpleTableModel("Top Level Tasks", "Gantt", ColumnNames,Dataz);
		return result;
	}
	
	public int createReport(String path, ReportType type) {
		ReportType TEXT,MD,HTML;
		
		TEXT = ReportType.TEXT;
		MD = ReportType.MD;
		HTML = ReportType.HTML;
		String filename = "";
		
		// finding filename given
		for(int i = 0; i < path.length(); i++ ) {
			int num = Character.compare(path.charAt(path.length()-1-i), '\\');
			if (num == 0){
				filename = filename + path.substring(path.length()-i, path.length());
				break;
			}
		}
		
		
		
		// adding file extension if not given
		if(type == MD) {
			if(filename.length() < 3 || !(".MD").equals((filename).substring(filename.length()-3).toUpperCase())) {
				path = path + ".md";
			}
		}
		else if(type == HTML) {
			if(filename.length() < 5 || !(".HTML").equals((filename).substring(filename.length()-5).toUpperCase())) {
				path = path + ".html";
			}
		}
		else {
			if(filename.length() < 4 || !(".TXT").equals((filename).substring(filename.length()-4).toUpperCase())) {
				path = path + ".txt";
			}
		}
		
		File out = new File(path);
		
		try {
			out.createNewFile();
			FileWriter fwriter = new FileWriter (path);
			
			if(type == TEXT) {
				
				for(String i: ColumnNames) {
					fwriter.write(i+"\t");
				}
				fwriter.write(System.lineSeparator());
				
				for(String[] i: sortedData) {
					for(String j: i) {
						fwriter.write(j+"\t");
					}
					fwriter.write(System.lineSeparator());
				}
			}
			
			else if(type == MD) {
				
				for(String i: ColumnNames) {
					fwriter.write("*"+i+"* ");
				}
				fwriter.write(System.lineSeparator());
				
				for(String[] i: sortedData) {
					if(i[2].equals("0")) {
							for(String j: i) {
								fwriter.write("**"+j+"** ");
							}
					}
					else {
						for(String j: i) {
							fwriter.write(j+" ");
						}
					}
					fwriter.write(System.lineSeparator());
				}
			}

			else if(type == HTML) {
				fwriter.write("<!DOCTYPE html>" + System.lineSeparator()
						+ "<html>" + System.lineSeparator()
						+ "<head>" + System.lineSeparator()
						+ "<meta http-equiv=\"Content-Type\" content\"text/html; charset=windows-1253\">" + System.lineSeparator()
						+ "<title>Gantt Project Data</title>" + System.lineSeparator()
						+ "</head>"+ System.lineSeparator()
						+"<body>" + System.lineSeparator()
						+ System.lineSeparator() + "<table>" + System.lineSeparator());
				
				fwriter.write("<tr>" + System.lineSeparator());
				
				for(String i: ColumnNames) {
					fwriter.write("<td>"+i+"</td> ");
				}
				fwriter.write("</tr>" + System.lineSeparator() + System.lineSeparator());
				
				for(String[] i: sortedData) {
					fwriter.write("<tr>" + System.lineSeparator());
					for(String j: i) {
						fwriter.write("<td>"+j+"</td> ");
					}
					fwriter.write("</tr>" + System.lineSeparator() + System.lineSeparator());
				}
				
				fwriter.write("</table></body>"+ System.lineSeparator() +"</html>");
			}
			
			fwriter.close();
		}
		catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		}
		
		return -1;
	}
}