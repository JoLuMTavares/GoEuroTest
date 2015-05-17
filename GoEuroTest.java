/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package goeurotest;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import java.net.URL;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.json.JSONTokener;


/**
 *
 * @author User
 */
public class GoEuroTest {

    // The list to keep the information from the endpoint
    private static List<StadtInfo> SIList = new ArrayList();
    
    //CSV file header
    private static final String FILE_HEADER = "_id,name,type,latitude,longitude";
    
    //Delimiters used in CSV files
    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";

    
    
    
    // Function that reads the information contained in the buffer
    // Then it returns as a String
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
          sb.append((char) cp);
        }
        return sb.toString();
    }

   
    
    // This function reads the data from the endpoint with the given parameter
    public static void readURLData(String param) {
        
               
        try {
          
            // creating a url object
            String url = "http://www.goeuro.com/GoEuroAPI/rest/api/v2/position/suggest/en/"+param;
            
            // Oppening the stream from the url
            InputStream is = new URL(url).openStream();
            
            // wrapping the connection in a buffered reader
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            
            // Calling now the function above to read the content from the buffer
            String line = readAll(rd);
            
            JSONTokener jst = new JSONTokener(line);
            
            
            
            // Parameters to keep the data
            String _type = new String();
            int _id = 0;
            String name = new String();
            String type = new String();
            float latitude = 0;
            float longitude = 0;
            
            while (jst.more()) {
                String currStr = jst.nextTo("\\:");
                if (currStr.contains("_id")) {
                    jst.next(); // This skips unecessary charaters
                   _id = Integer.valueOf(jst.nextTo(","));               
                }
                else if (currStr.contains("name")) {
                    jst.next();
                    name = jst.nextTo(",").replace("\"", "");
                }
                else if (currStr.contains("type")) {
                    jst.next();
                    type = jst.nextTo(",").replace("\"", "");
                }
                else if (currStr.contains("geo_position")) {
                    jst.next();
                    String nextStr = jst.nextTo("\\:");
                    if (nextStr.contains("latitude")) {
                        jst.next();
                        latitude = Float.valueOf(jst.nextTo(","));
                    }
                }
                else if (currStr.contains("longitude")) {
                    jst.next();
                    longitude = Float.valueOf(jst.nextTo("\\}"));
                }
                 
                
                else if (currStr.contains("distance")) {
                    // Class to store all the needed parameters
                    StadtInfo sI = new StadtInfo(_id, name, type, latitude, longitude);
                    // Adding the new class to the array list
                    SIList.add(sI);
                    jst.nextTo(",");
                }  
                else {
                    // Also skiping the next comma, so the cicle
                    // doesn't block
                    jst.nextTo(","); 
                }
              
            }
        }
        catch(Exception e)
        {
          e.printStackTrace();
        }
    }
    
    
    // This function creates a new CSV file with the given
    // name by the user
    public static void createCSVFile(String param) {
        
        FileWriter fileWriter = null;
        
        try {
            fileWriter = new FileWriter(param);
            
            //Write the CSV file header
            fileWriter.append(FILE_HEADER.toString());
            
            //Adding a new line separator after the header
            fileWriter.append(NEW_LINE_SEPARATOR);

            // Now writing the Stadt information on the file
            for (StadtInfo stadtInfo : SIList) {
                fileWriter.append(String.valueOf(stadtInfo.getID()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(stadtInfo.getName());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(stadtInfo.getType());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(stadtInfo.getLat()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(stadtInfo.getLongit()));
                fileWriter.append(NEW_LINE_SEPARATOR);
            }
            
            System.out.println("CSV file was created successfully !!!");

        }  catch (Exception e) {
            System.out.println("Error in createCSVFile !!!");
            e.printStackTrace();
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
            }
        }

    }
    
    public static void main(String[] args) throws IOException {


        if (args.length == 0) {
            System.err.println("No arguments");
        } else {
            String fileName = args[0]+".csv";
            
            System.out.println("Loading the endpoint.");
            readURLData(args[0]);
            
            System.out.println("Creating the csv file.");
            createCSVFile(fileName);
            
        }
        
         
    }
}
