/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.psexton.authzwriter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

/**
 *
 * @author PSexton
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new Main(args);
    }

    public Main(String[] args) {
        Option reader   = OptionBuilder.withArgName( "reader" )
                                .hasArgs()
                                .withDescription("give this user read privledges" )
                                .create( "reader" );
        Option writer   = OptionBuilder.withArgName( "writer" )
                                .hasArgs()
                                .withDescription("give this user read/write privledges" )
                                .create( "writer" );
        Option file   = OptionBuilder.withArgName( "file" )
                                .hasArgs()
                                .withDescription("path to output file" )
                                .create( "file" );
        Options options = new Options();
        options.addOption(reader);
        options.addOption(writer);
        
        CommandLineParser parser = new PosixParser();
        try {
            List<String> readers = new ArrayList<String>();
            List<String> writers = new ArrayList<String>();
            
            CommandLine line = parser.parse( options, args);
            if(line.hasOption("reader")) {
                // handle r case
                String[] usernames = line.getOptionValues( "reader");
                readers = Arrays.asList(usernames);
            }
            if(line.hasOption("writer")) {
                // handle w case
                String[] usernames = line.getOptionValues("writer");
                writers = Arrays.asList(usernames);
            }
            
            String fileBody = createFileBody(readers, writers);
            if(line.hasOption("file")) {
                String filePath = line.getOptionValue("file");
                writeToFile(fileBody, filePath);
            }
            else {
                System.out.println(fileBody);
            }
        } 
        catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
        
    }
    
    private String createFileBody(List<String> readers, List<String> writers) {
        /*
         * Structure of file should be:
         * 
         * [groups]
         * readers = user1, user2, user3
         * writers = user4, user5, user6
         * 
         * [/]
         * @readers = r
         * @writers = rw
         */
        
        final String NEWLINE = System.getProperty("line.separator");
        StringBuilder builder = new StringBuilder();
        
        // groups header
        builder.append("[groups]" + NEWLINE);
        // groups body
        builder.append("readers = " + makeCommaSeparatedList(readers) + NEWLINE);
        builder.append("writers = " + makeCommaSeparatedList(writers) + NEWLINE);
        // blank line spacer
        builder.append(NEWLINE);
        // path body
        builder.append("[/]" + NEWLINE);
        builder.append("@readers = r" + NEWLINE);
        builder.append("@writers = rw" + NEWLINE);
        
        return builder.toString();
    }
    
    /**
     * Given a String body and a file path, writes fileBody to filePath
     * @param fileBody
     * @param filePath 
     */
    private void writeToFile(String fileBody, String filePath) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
    /**
     * Given a List of Strings, returns a single string containing them comma and space separated
     * @param entries
     * @return 
     */
    private String makeCommaSeparatedList(List<String> entries) {
        int numEntries = entries.size();
        if(numEntries == 0)
            return "";
        else if(numEntries == 1)
            return entries.get(0);
        else {
            String returnVal = entries.get(0);
            for(int i = 1; i < numEntries; i++)
                returnVal = returnVal + ", " + entries.get(i);
            return returnVal;
        }
    }
    
}
