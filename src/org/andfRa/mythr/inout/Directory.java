package org.andfRa.mythr.inout;

import java.io.File;

public enum Directory {
	
	CONFIG_DEFAULTS("config/", ""),
	MYTHR_JAR("plugins/", "Mythr.jar"),
	
	ATTRIBUTE_CONFIG("plugins/Mythr/config/", "attributes.json"),
	SKILL_CONFIG("plugins/Mythr/config/", "skills.json"),
	ECONOMY_CONFIG("plugins/Mythr/config/", "economy.json"),
	
	PLAYER_DATA("plugins/Mythr/players/", FileIO.NAME_SUBS + ".json"),
	
	WIKI("plugins/Mythr/wiki/", FileIO.NAME_SUBS + ".txt");
	
	/** File extension. */
	public static final String FILE_EXTENTENSION = ".json";
	
	/** String added to deleted files directories. */
	public static final String DELETED_DIRECTORY_ADD = "deleted/";
	
	
	// CONSTRUCTION:
	private String dir;
	private String filename;

	private Directory(String dir, String filename)
	 {
		this.dir = dir.replace("/", File.separator);
		this.filename = filename;
	 }

	
	// GETTERS:
	/**
	 * Gets file directory.
	 * 
	 * @return directory
	 */
	public String getDirectory()
	 { return dir; }
	
	/**
	 * Gets the directory for deleted files.
	 * 
	 * @return directory for deleted files
	 */
	public String getDeletedDirectory()
	 { return dir + DELETED_DIRECTORY_ADD.replace("/", File.separator); }
	
	/**
	 * Gets the file name.
	 * 
	 * @return file name
	 */
	public String getFilename()
	 { return filename; }
	
}
