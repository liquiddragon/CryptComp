package cryptcomp;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Wrapper for Apache Commons CLI library for command line usage.
 *
 * See
 * <a href="https://commons.apache.org/proper/commons-cli/introduction.html">Apache
 * Commons CLI</a>.
 */
public class CmdLineParser {

    /**
     * Command line options available for use.
     */
    public enum CCOptions {

        /**
         * Encrypt input.
         */
        ENC("enc"),
        /**
         * Decrypt input.
         */
        DEC("dec"),
        /**
         * Key used for en/decryption.
         */
        KEY("key"),
        /**
         * Input file or STDIN if missing.
         */
        INFILE("infile"),
        /**
         * Output file or STDOUT if missing.
         */
        OUTFILE("outfile");

        private final String optionName;

        private CCOptions(String description) {
            optionName = description;
        }

        /**
         * Command line option textual presentation.
         *
         * @return option textual presentation
         */
        public String getOption() {
            return optionName;
        }
    }

    /**
     * Supported en/decryption algorithms.
     */
    public enum CCCryptoAlgo {

        /**
         * Data encryption standard (DES).
         */
        DES("DES");

        private final String algoName;

        private CCCryptoAlgo(String name) {
            algoName = name;
        }

        /**
         * Crypto algorithms textual presentation.
         *
         * @return option textual presentation
         */
        public String getAlgo() {
            return algoName;
        }
    }

    private final Options options;
    private CommandLine cmdLine;

    /**
     * Creates a new command line parser.
     */
    public CmdLineParser() {
        options = new Options();

        addOptions();
    }

    /**
     * Add options for command line parse.
     */
    private void addOptions() {
        Option encrypterOpt = Option.builder("enc").hasArg().argName("DES").desc("encrypter to be used").build();
        Option decrypterOpt = Option.builder("dec").hasArg().argName("DES").desc("decrypter to be used").build();
        Option keyOpt = Option.builder("key").hasArg().argName("en/decryption key").desc("key").build();
        Option inFileOpt = Option.builder("infile").hasArg().argName("name").desc("filename").build();
        Option outFileOpt = Option.builder("outfile").hasArg().argName("name").desc("filename").build();

        options.addOption(encrypterOpt);
        options.addOption(decrypterOpt);
        options.addOption(keyOpt);
        options.addOption(inFileOpt);
        options.addOption(outFileOpt);
    }

    /**
     * Parse command line storing parameters internally.
     *
     * @param arguments command line
     * @return true, if parsing succeeded or false otherwise
     */
    public boolean parseCmdLine(String[] arguments) {
        boolean result = false;

        CommandLineParser parser = new DefaultParser();

        cmdLine = null;
        try {
            cmdLine = parser.parse(options, arguments);
        } catch (ParseException exp) {
            System.err.println("Parsing failed. Reason: " + exp.getMessage() + "\n");
            cmdLine = null;
        }

        if (cmdLine != null) {
            result = true;
        }

        return result;
    }

    /**
     * Show command line usage help.
     */
    public void showUsage() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("CyrptComp", options);
    }

    /**
     * Query to see if an option has been set.
     *
     * @param option name of the option
     * @return true if set, false otherwise
     */
    public boolean hasOption(String option) {
        return cmdLine.hasOption(option);
    }

    /**
     * Retrieves the array of values, if any, of an option.
     *
     * @param option name of the option
     * @return values of the argument if option is set, and has an argument,
     * otherwise null.
     */
    public String getOption(String option) {
        return cmdLine.getOptionValue(option);
    }
}
