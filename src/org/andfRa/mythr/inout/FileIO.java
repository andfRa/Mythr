package org.andfRa.mythr.inout;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import org.andfRa.mythr.MythrLogger;
import org.bukkit.craftbukkit.libs.com.google.gson.Gson;
import org.bukkit.craftbukkit.libs.com.google.gson.GsonBuilder;

public class FileIO {

	/** Substitution string for name. */
	public static String NAME_SUBS = "&name&";

	/** Comment start indicator. */
	public static String COMMENT_START = "#";

	/** Line separator. */
	public static String LINE_SEPARATOR = System.getProperty("line.separator");
	
	
	/**
	 * Writes a data file.
	 * 
	 * @param dir directory
	 * @param name name
	 * @param obj object
	 * @throws IOException when write fails
	 */
	public static void writeData(Directory dir, String name, Object obj) throws IOException
	 {
		// Gson:
		GsonBuilder gsonBuilder= new GsonBuilder();
		Gson gson = gsonBuilder.create();
		String objStr = gson.toJson(obj);
		
		if(objStr == null || objStr.equals("null")){
			MythrLogger.severe(FileIO.class, "writer received null data");
			return;
		}
		
		// Directory:
		File directory = new File(dir.getDirectory());
		if(!directory.exists()){
			directory.mkdirs();
			MythrLogger.info("Creating " + directory + " directory.");
		}
		
		// File:
		File filedir = new File(dir.getDirectory() + dir.getFilename().replace(NAME_SUBS, name));
		if(!filedir.exists()){
			filedir.createNewFile();
			MythrLogger.info("Creating " + filedir + " file.");
		}
        
		// Write:
		BufferedWriter out = new BufferedWriter(new FileWriter(filedir));
		out.write(objStr);
		out.close();
	 }

	/**
	 * Reads a data file.
	 * 
	 * @param dir directory
	 * @param name name
	 * @param type object type
	 * @return object
	 * @throws IOException when read fails
	 */
	public static <T> T readData(Directory dir, String name, Class<T> type) throws IOException
	 {
		// Gson:
        GsonBuilder gsonBuilder= new GsonBuilder();
		Gson gson = gsonBuilder.create();

		// Directory:
		File directory = new File(dir.getDirectory());
		if(!directory.exists()){
			directory.mkdirs();
			MythrLogger.info("Creating " + directory + " directory.");
		}
		
		// File:
		File filedir = new File(dir.getDirectory() + dir.getFilename().replace(NAME_SUBS, name));
		if(!filedir.exists()){
			filedir.createNewFile();
			MythrLogger.info("Creating " + filedir + " file.");
		}
		
		// Read:
		BufferedReader input =  new BufferedReader(new FileReader(filedir));
		StringBuffer contents = new StringBuffer();
		String line = null;
        while ((line = input.readLine()) != null){
        	contents.append(line);
        	contents.append(LINE_SEPARATOR);
        }
        input.close();
        
        // Fix null and empty:
        if(contents.length() == 0 || contents.toString().equals("null")){
        	MythrLogger.severe(FileIO.class, "read received null or empty data from " + filedir);
        	contents = new StringBuffer("{ }");
        }
          
        return gson.fromJson(contents.toString(), type);
	 }

	/**
	 * Reads a configuration file.
	 * 
	 * @param dir directory
	 * @param type object type
	 * @return object
	 * @throws IOException when read fails
	 */
	public static <T> T readConfig(Directory dir, Class<T> type) throws IOException
	 {
		// Gson:
        GsonBuilder gsonBuilder= new GsonBuilder();
		Gson gson = gsonBuilder.create();

		// Directory:
		File directory = new File(dir.getDirectory());
		if(!directory.exists()){
			directory.mkdirs();
			MythrLogger.info("Creating " + directory + " directory.");
		}
		
		// File:
		File filedir = new File(dir.getDirectory() + dir.getFilename());
		if(!filedir.exists()){
			filedir.createNewFile();
			MythrLogger.info("Creating " + filedir + " file.");
		}
		
		// Read:
		BufferedReader input =  new BufferedReader(new FileReader(filedir));
		StringBuffer contents = new StringBuffer();
		String line = null;
        while ((line = input.readLine()) != null){
        	if(checkComment(line)) continue;
        	contents.append(line);
        	contents.append(LINE_SEPARATOR);
        }
        input.close();
        
        // Fix null and empty:
        if(contents.length() == 0 || contents.toString().equals("null")){
        	MythrLogger.severe(FileIO.class, "read received null or empty config from " + filedir);
        	contents = new StringBuffer("{ }");
        }
        
        return gson.fromJson(contents.toString(), type);
	 }

	/**
	 * Checks if the file exists.
	 * 
	 * @param dir file directory
	 * @param name file name
	 * @return true if exists
	 */
	public static boolean exists(Directory dir, String name)
	 {
		return new File(dir.getDirectory() + dir.getFilename().replace(NAME_SUBS, name)).exists();
	 }

	/**
	 * Checks if the file exists.
	 * 
	 * @param name file name
	 * @return true if exists
	 */
	public static boolean exists(Directory dir)
	 {
		return new File(dir.getDirectory() + dir.getFilename()).exists();
	 }

	/**
	 * Unpacks default config from the jar.
	 * 
	 * @param configDir config directory
	 * @throws IOException when unpacking fails
	 */
	public static void unpackConfig(Directory configDir) throws IOException
	 {
		// Mythr.jar:
		Directory dir = Directory.MYTHR_JAR;
		File home = new File(System.getProperty("user.dir") + File.separator + dir.getDirectory() + dir.getFilename());
		JarFile jar = new JarFile(home);
	
		// Config file:
		String entryPath = (Directory.CONFIG_DEFAULTS.getDirectory() + configDir.getFilename()).replace(File.separator, "/");
		ZipEntry entry = jar.getEntry(entryPath);
		
		// Config not in Saga.jar:
		if(entry == null){
			jar.close();
			throw new IOException(entryPath + " not found in Saga.jar");
		}
		
		File efile = new File(configDir.getDirectory() + configDir.getFilename());
		
		// Create file:
		if(!efile.getParentFile().exists()){
			MythrLogger.info("Creating " + efile.getParentFile() + " directory.");
			efile.getParentFile().mkdirs();
		}
		if(!efile.exists()){
			MythrLogger.info("Creating " + efile + " file.");
			efile.createNewFile();
		}
	
		// Unpack and write:
		InputStream in = new BufferedInputStream(jar.getInputStream(entry));
		OutputStream out = new BufferedOutputStream(new FileOutputStream(efile));
		
		byte[] buffer = new byte[2048];
		
		while(true){
			int nBytes = in.read(buffer);
			if (nBytes <= 0) break;
			out.write(buffer, 0, nBytes);
		}
		
		out.flush();
		out.close();
		in.close();
		jar.close();
	 }

	/**
	 * Checks if the line is a comment.
	 * 
	 * @param line line
	 * @return true if a comment
	 */
	private static boolean checkComment(String line)
	 {

		for (int i = 0; i < line.length(); i++) {
			if(line.charAt(i) == '\t' || line.charAt(i) == ' ') continue;
			if(line.startsWith(COMMENT_START, i)) return true;
			else return false;
		}
		
		return false;
	 }
	
	
}
