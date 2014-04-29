package ufrgs.maslab.gsom.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class WriteFile {
	
	private BufferedWriter bw = null;
	
	private static WriteFile instance = null;
	
	private WriteFile(){
		
	}
	
	public void write(String content)
	{
		try {
			  
			instance.bw.write(content);			
  
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static WriteFile getInstance()
	{
		if(instance == null)
		{
			instance = new WriteFile();
		}
		return instance;
	}
	
	
	public void openFile(String filename){
		
		try {
			 			
 
			File file = new File(System.getProperty("user.dir")+"/log/"+filename.toString()+".csv");
 
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
 
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			
 
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		//return instance;
				
	}
	
	public void nLine(){
		try {
			instance.bw.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void closeFile()
	{
				try {
					instance.bw.close();
					instance = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
	}

}
