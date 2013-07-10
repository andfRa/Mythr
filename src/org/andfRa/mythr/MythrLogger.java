package org.andfRa.mythr;

import java.util.EnumMap;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.libs.jline.Terminal;
import org.bukkit.craftbukkit.libs.jline.console.ConsoleReader;
import org.bukkit.craftbukkit.v1_6_R2.CraftServer;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.Ansi.Attribute;

public class MythrLogger{

	
	private static MythrLogger logger = null;
	
	private final ConsoleReader reader;
	
    private final Terminal terminal;
    
    private final Map<ChatColor, String> replacements = new EnumMap<ChatColor, String>(ChatColor.class);
    
    private final ChatColor[] colors = ChatColor.values();
    
    private Logger mlogger = Logger.getLogger("Mythr");
    
    
	/**
	 * Creates the logger
	 * 
	 */
	private MythrLogger() {
		
		this.reader = ((CraftServer) Mythr.plugin().getServer()).getReader();
        this.terminal = reader.getTerminal();
		
        replacements.put(ChatColor.BLACK, Ansi.ansi().fg(Ansi.Color.BLACK).toString());
        replacements.put(ChatColor.DARK_BLUE, Ansi.ansi().fg(Ansi.Color.BLUE).toString());
        replacements.put(ChatColor.DARK_GREEN, Ansi.ansi().fg(Ansi.Color.GREEN).toString());
        replacements.put(ChatColor.DARK_AQUA, Ansi.ansi().fg(Ansi.Color.CYAN).toString());
        replacements.put(ChatColor.DARK_RED, Ansi.ansi().fg(Ansi.Color.RED).toString());
        replacements.put(ChatColor.DARK_PURPLE, Ansi.ansi().fg(Ansi.Color.MAGENTA).toString());
        replacements.put(ChatColor.GOLD, Ansi.ansi().fg(Ansi.Color.YELLOW).bold().toString());
        replacements.put(ChatColor.GRAY, Ansi.ansi().fg(Ansi.Color.WHITE).toString());
        replacements.put(ChatColor.DARK_GRAY, Ansi.ansi().fg(Ansi.Color.BLACK).bold().toString());
        replacements.put(ChatColor.BLUE, Ansi.ansi().fg(Ansi.Color.BLUE).bold().toString());
        replacements.put(ChatColor.GREEN, Ansi.ansi().fg(Ansi.Color.GREEN).bold().toString());
        replacements.put(ChatColor.AQUA, Ansi.ansi().fg(Ansi.Color.CYAN).bold().toString());
        replacements.put(ChatColor.RED, Ansi.ansi().fg(Ansi.Color.RED).bold().toString());
        replacements.put(ChatColor.LIGHT_PURPLE, Ansi.ansi().fg(Ansi.Color.MAGENTA).bold().toString());
        replacements.put(ChatColor.YELLOW, Ansi.ansi().fg(Ansi.Color.YELLOW).bold().toString());
        replacements.put(ChatColor.WHITE, Ansi.ansi().fg(Ansi.Color.WHITE).bold().toString());
        replacements.put(ChatColor.MAGIC, Ansi.ansi().a(Attribute.BLINK_SLOW).toString());
        replacements.put(ChatColor.BOLD, Ansi.ansi().a(Attribute.UNDERLINE_DOUBLE).toString());
        replacements.put(ChatColor.STRIKETHROUGH, Ansi.ansi().a(Attribute.STRIKETHROUGH_ON).toString());
        replacements.put(ChatColor.UNDERLINE, Ansi.ansi().a(Attribute.UNDERLINE).toString());
        replacements.put(ChatColor.ITALIC, Ansi.ansi().a(Attribute.ITALIC).toString());
        replacements.put(ChatColor.RESET, Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.DEFAULT).toString());
        
	}

	
	/**
	 * Formats the message.
	 * 
	 * @param message message
	 * @return formated message
	 */
	public String format(String message)
	 {
		if(terminal.isAnsiSupported()) {
                
			String result = message;
               
			for (ChatColor color : colors) {
                   
				if (replacements.containsKey(color)) {
					result = result.replaceAll("(?i)" + color.toString(), replacements.get(color));
				} else {
					result = result.replaceAll("(?i)" + color.toString(), "");
				}
                
			}
                
            return result + Ansi.ansi().reset().toString();
                
        } else {
        	return ChatColor.stripColor(message);
        }
     }
	
	/**
	 * Severe message.
	 * 
	 * @param msg message
	 */
	public static void severe(String msg)
	 {
		logger.mlogger.severe(logger.format(ChatColor.RED + msg));
	 }
	
	/**
	 * Warning message.
	 * 
	 * @param msg message
	 */
	public static void warning(String msg)
	 {
		logger.mlogger.warning(logger.format(ChatColor.YELLOW + msg));
	 }
	
	/**
	 * Info message.
	 * 
	 * @param msg message
	 */
	public static void info(String msg)
	 {
		logger.mlogger.info(logger.format(msg));
	 }

	/**
	 * Message.
	 * 
	 * @param msg message
	 */
	public static void message(String msg)
	 {
		logger.mlogger.info(logger.format(msg));
	 }


    /**
     * Info message.
     * 
     * @param clazz class
     * @param message message
     */
    public static void info(Class<?> clazz, String message)
     {
    	info("{" + clazz.getSimpleName() + "} " + message);
     }
    
    /**
     * Severe message.
     * 
     * @param clazz class
     * @param message message
     */
	public static void severe(Class<?> clazz, String message)
	 {
    	severe("{" + clazz.getSimpleName() + "} " + message);
     }
    
    /**
     * Warning message.
     * 
     * @param clazz class
     * @param message message
     */
    public static void warning(Class<?> clazz, String message)
     {
    	warning("{" + clazz.getSimpleName() + "} " + message);
     }
    
	/**
     * Null field message.
     * 
     * @param clazz class
     * @param field field name
     */
	public static void nullField(Class<?> clazz, String field)
	 {
        severe("{" + clazz.getSimpleName() + "} " + "Field " + field + " failed to initialise.");
     }
    
	
	/**
	 * Loads the logger.
	 * 
	 */
	public static void load()
	 {
		logger = new MythrLogger();
	 }
	
	/**
	 * Unloads the logger.
	 * 
	 */
	public static void unload()
	 {
		logger = null;
	 }
	
	
}
