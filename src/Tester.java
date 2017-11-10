//package ad1.ss16.pa;

import java.io.File;
import java.util.*;

/**
 * This class includes the {@link #main main()}-method for the program
 * and {@link #printDebug(String)} to print debug messages
 * <p>
 * <b>IMPORTANT:</b> Do not change this class. All changes made to this class
 * are removed after the submission of the solution. Thereby, the submitted solution
 * can fail during the tests.
 * </p>
 */
public class Tester {
    /**
     * Name of file with test instances. Is <code>
     * null</code>, if read from {@link System#in}.
     */
    private static String fileName = null;

    /**
     * Adapted path
     */
    private static String choppedFileName;

    /**
     * Test flag for output during test run
     */
    private static boolean test = false;

    /**
     * Debug flag for additional debug messages
     */
    private static boolean debug = false;

    /**
     * Prints message <code>msg</code> and exits program.
     *
     * @param msg the printed message.
     */
    private static void bailOut(String msg) {
        System.out.println();
        System.err.println((test ? choppedFileName + ": " : "") + "ERR " + msg);
        System.exit(1);
    }

    /**
     * Generates a chopped String representation of the file name.
     */
    private static void chopFileName() {
        if (fileName == null) {
            choppedFileName = "System.in";
            return;
        }
        int i = fileName.lastIndexOf(File.separatorChar);
        if (i > 0) {
            i = fileName.lastIndexOf(File.separatorChar, i - 1);
        }
        if (i == -1) {
            i = 0;
        }
        choppedFileName = ((i > 0) ? "..." : "") + fileName.substring(i);
    }

    /**
     * Prints a debugging message. If the program is started with <code>-d</code>
     * a message <code>msg</code> and the file name of the test instance
     * is printed. Otherwise nothing is printed.
     *
     * @param msg output text.
     */
    public static void printDebug(String msg) {
        if (!debug) {
            return;
        }
        System.out.println(choppedFileName + ": DBG " + msg);
    }

    /**
     * Opens the input file and returns an object of type {@link Scanner} for reading from the file.
     * If no file name is given, input is read from {@link System#in}.
     *
     * @return {@link Scanner} reading from input file.
     */
    private static Scanner openInputFile() {
        if (fileName != null) {
            try {
                return new Scanner(new File(fileName));
            } catch (Exception e) {
                bailOut("could not open \"" + fileName + "\" for reading");
            }
        }
        return new Scanner(System.in);
    }

    /**
     * Interprets parameters for the program  and returns
     * a {@link Scanner} reading from test file.
     *
     * @param args command line parameters
     * @return {@link Scanner} reading from test file.
     */

    private static Scanner processArgs(String[] args) {
        for (String a : args) {
            if (a.equals("-t")) {
                test = true;
            } else if (a.equals("-d")) {
                debug = test = true;
            } else if (fileName == null) {
                fileName = a;
            }
        }
        return openInputFile();
    }

    /**
     * The constructor is private to hide it from JavaDoc.
     */
    private Tester() {
    }

    /**
     * Checks structural aspects of the network
     *
     * @param network network to check
     * @param parts values to compare
     * @param lineNumber line number in test file
     */
    private static void checkStructure(Network network, String[] parts, int lineNumber) {
        long startTime = System.nanoTime();
        int expected, calculated;
        boolean gcycle, icycle;
        expected = Integer.valueOf(parts[0]);
        calculated = network.numberOfConnections();
        if (expected != calculated) {
            bailOut("Error in input file at line number " + lineNumber + ": Number of connections is not correct (" + expected + " expected, " + calculated + " calculated)!");
        }
        expected = Integer.valueOf(parts[1]);
        calculated = network.numberOfComponents();
        if (expected != calculated) {
            bailOut("Error in input file at line number " + lineNumber + ": Number of components is not correct (" + expected + " expected, " + calculated + " calculated)!");
        }
        gcycle = Boolean.valueOf(parts[2]);
        icycle = network.hasCycle();
        if (gcycle != icycle) {
            bailOut("Error in input file at line number " + lineNumber + ": Error in cycle detection (" + gcycle + " expected, " + icycle + " calculated)!");
        }
        printDebug((System.nanoTime() - startTime) * 1.0 / 1000000000 + " seconds for structure check");
    }

    /**
     * Checks distance implementation between pair of nodes
     *
     * @param network network to check
     * @param parts triples of the form (x,y,z) where z ist the distance between x and y
     * @param lineNumber line number in test file
     */
    private static void checkDistance(Network network, String[] parts, int lineNumber) {
        long startTime = System.nanoTime();
        String[] nums;
        int start, end, expected, calculated;
        for(int i=0; i < parts.length; i++) {
            nums = parts[i].substring(1,parts[i].length()-1).split(",");
            start = Integer.valueOf(nums[0]);
            end = Integer.valueOf(nums[1]);
            if(start < 0 || start >= network.numberOfNodes() || end < 0 || end >= network.numberOfNodes()) {
                throw new IllegalArgumentException("Parameters for distance calculation are out of range");
            }
            expected = Integer.valueOf(nums[2]);
            calculated = network.minimalNumberOfConnections(start, end);
            if(expected != calculated) {
                bailOut("Error in input file at line number " + lineNumber + ": Distance between " + start + " and " + end + " is not correct (" + expected + " expected, " + calculated + " calculated)!");
            }
        }
        printDebug((System.nanoTime() - startTime) * 1.0 / 1000000000 + " seconds for distance check");
    }


    /**
     * Checks the implementation for finding all critical nodes
     *
     * @param network network to check
     * @param parts expected critical nodes
     * @param lineNumber line number in test file
     */
    private static void checkCritical(Network network, String[] parts, int lineNumber) {
        long startTime = System.nanoTime();
        int expected;
        List<Integer> nodes = network.criticalNodes();
        if (nodes.size() != parts.length) {
            bailOut("Error in input file at line number " + lineNumber + ": Number of critical nodes is not correct (" + parts.length + " expected, " + nodes.size() + " calculated)!");
        }
        for(int i=0; i < parts.length; i++) {
            expected = Integer.valueOf(parts[i]);
            if(!nodes.contains(expected)){
                bailOut("Error in input file at line number " + lineNumber + ": Critical node " + expected + " not found!");
            }
        }
        printDebug((System.nanoTime() - startTime) * 1.0 / 1000000000 + " seconds for critical nodes check");
    }

    public static void main(String[] args) {
        Scanner s = processArgs(args);
        chopFileName();
        try {
            SecurityManager sm = new ADS1SecurityManager();
            System.setSecurityManager(sm);
        } catch (SecurityException e) {
            bailOut("Error: could not set security manager: " + e);
        }
        try {
            Network network = null;
            boolean growing = true;
            String help, first;
            String[] parts;
            int node, lineNumber = 1;
            long startTime = System.nanoTime();
            while(s.hasNextLine()) {
                help = s.nextLine();
                if (help.length() == 0) {
                    continue;
                }
                parts = help.toLowerCase().split("\\s+");
                first = parts[0];
                parts = Arrays.copyOfRange(parts, 1, parts.length);
                if (lineNumber == 1) {
                    if (first.equals("nodes")) {
                        network = new Network(Integer.valueOf(parts[0]));
                    } else {
                        bailOut("Error in input file at line number " + lineNumber + ": Check syntax of first line!");
                    }
                } else {
                    switch (first) {
                        case "structure":
                            checkStructure(network, parts, lineNumber);
                            break;
                        case "distance":
                            checkDistance(network, parts, lineNumber);
                            break;
                        case "critical":
                            checkCritical(network, parts, lineNumber);
                        case "add":
                            growing = true;
                            break;
                        case "delete":
                            growing = false;
                            break;
                        default:
                            node = Integer.valueOf(first);
                            if(parts.length == 0) {
                                if (growing) {
                                    network.addAllConnections(node);
                                } else {
                                    network.deleteAllConnections(node);
                                }
                            } else {
                                for (int i = 0; i < parts.length; i++) {
                                    if (growing) {
                                        network.addConnection(node, Integer.valueOf(parts[i]));
                                    } else {
                                        network.deleteConnection(node, Integer.valueOf(parts[i]));
                                    }
                                }
                            }
                    }
                }
                lineNumber++;
            }
            printDebug((System.nanoTime() - startTime) * 1.0 / 1000000000 + " seconds to complete whole test");
            StringBuffer msg = new StringBuffer(test ? choppedFileName + ": " : "");
            msg.append("OK");
            System.out.println("");
            System.out.println(msg.toString());
        } catch (SecurityException se) {
            bailOut("Method call not allowed: \"" + se.toString() + "\"");
        } catch (NumberFormatException e) {
            bailOut("Wrong input format: \"" + e.toString() + "\"");
        } catch (Exception e) {
            e.printStackTrace();
            bailOut("Caught exception \"" + e.toString() + "\"");
        }
    }
}
