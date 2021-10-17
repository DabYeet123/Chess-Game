package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;




public class Utils {
	
	public static String loadFileAsString(String path) {
		StringBuilder builder = new StringBuilder();
		
		URL url = Utils.class.getResource(path);
		
		try {
			//BufferedReader br = new BufferedReader(new FileReader(path));
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
			String line;
			while((line = br.readLine())!= null)
				builder.append(line + "\n");
			
			br.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		return builder.toString();
		

	}
	
	

	
	public static int parseInt(String number) {
		try {
			return Integer.parseInt(number);
		}catch(NumberFormatException e) {
			e.printStackTrace();
			return 0;
		}
		
	}
	
}
