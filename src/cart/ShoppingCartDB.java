package cart;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ShoppingCartDB {

    // CONSTANTS
    public static final String LOGIN = "login";
    public static final String ADD = "add";
    public static final String LIST = "list";
    public static final String SAVE = "save";
    public static final String USERS = "users";
    public static final String EXIT = "exit";

    public static final List<String> VALID_COMMANDS = Arrays.asList(
            LOGIN, ADD, SAVE, LIST, EXIT, USERS);
    // asList() creates and populates list in 1 line

    // VARIABLES
    private CartDBInMemory db;
    private String currentUser;
    private String baseFolder;

    // CONSTRUCTORS
    public ShoppingCartDB() {
        this.baseFolder = "db";
        // setting 'db' as default base folder
        this.setup();
        this.db = new CartDBInMemory(this.baseFolder);
        // creating the cart hashmap, loading all the saved data
    }

    // constructor with custom base folder name
    public ShoppingCartDB(String baseFolder) {
        this.baseFolder = baseFolder;
        this.setup();
        this.db = new CartDBInMemory(this.baseFolder);
        // creating the cart hashmap, loading all the saved data
    }

    // ENSURE BASE FOLDER EXISTS
    public void setup() {
        Path p = Paths.get(this.baseFolder);
        if (Files.isDirectory(p)) {
            // directory exists
        } else {
            try {
                Files.createDirectory(p);
            } catch (IOException e) {
                System.err.println("Unable to create directory. Error ->" + e.getMessage());
            }
        }
    }

    public void startShell() {
        System.out.println("Welcome\nPlease login");
        Scanner sc = new Scanner(System.in);
        String line;
        boolean stop = false;

        // String command = sc.next(); // gives first word until whitespace, can use to
        // check command as command is the first word
        // String inputs = sc.nextLine(); // gives whole line, but as we have run
        // next(), it will give whatever is after the first word.

        while (!stop) {
            line = sc.nextLine();
            line = line.trim();
            System.out.println("input-> " + line);

            // EXIT CASE
            if (line.equalsIgnoreCase(EXIT)) {
                System.out.println("Exiting.");
                stop = true;
            }

            // Validate command
            if (!this.ValidateInput(line)) {
                System.out.println("Invalid input.");
            } else {
                // valid command
                System.out.println("Processing -> " + line);
                this.ProcessInput(line);
            }
        }
        sc.close();
    }

    // Command Validation (first word of input)
    public boolean ValidateInput(String input) {
        String[] parts = input.split(" ");
        String command = parts[0].trim();

        // Scanner lsc = new Scanner(); // ALTERNATIVELY
        // String command = lsc.next().trim();

        // is command in the list of valid commands?
        return VALID_COMMANDS.contains(command);
    }

    // Process command
    public void ProcessInput(String input) {
        Scanner sc = new Scanner(input);
        String command = sc.next().trim(); // first word of input

        switch (command) {
            case LOGIN:
                String username = sc.nextLine().trim();
                // nextLine() gets 2nd word of input onwards as we executed next() before
                this.LoginAction(username);
                System.out.println("Current logged in user -> " + this.currentUser);
                break;

            case LIST:
                this.listAction();
                break;

            case ADD:
                String[] items = sc.nextLine().trim().split(",");
                // nextLine() gets 2nd word of input onwards as we executed next() before
                this.addAction(items);
                break;

            case SAVE:
                this.saveAction();
                break;

            case USERS:
                this.usersAction();
                break;

            default:
                break;
        }
        sc.close();
    }

    // Command: login <username>
    // checks if any of the keys in hashmap == username,
    // if not, creates new entry in hashmap
    // also flags currentUser
    public void LoginAction(String username) {
        if (!this.db.userMap.containsKey(username)) {
            this.db.userMap.put(username, new ArrayList<String>());
        }
        this.currentUser = username;
    }

    // Command: add <item1>, <item2>
    // add items into currentUser's ArrayList in the hashmap
    public void addAction(String[] items) {
        for (String item : items) {
            // this "db"object's "userMap"variable
            this.db.userMap.get(this.currentUser).add(item.trim());
            // .get() gets the corresponding value(ArrayList) of the key(currentUser)
            // then .add() ads to the gotten ArrayList
        }
    }

    // Command: list
    // Print each item from the ArrayList of currentUser in hashmap
    public void listAction() {
        for (String item : this.db.userMap.get(this.currentUser)) {
            System.out.println("Item -> " + item);
        }
        System.out.println("=====End Of List=====");
    }

    // Command: users
    // prints filenames of .db files in baseFolder
    public void usersAction() {
        File f = new File(this.baseFolder);
        File[] ListOfFiles = f.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return filename.endsWith(".db");
            }
        });
        if (ListOfFiles.length == 0) {
            System.out.println("No users found.");
        }
        for (File file : ListOfFiles) {
            String username = file.getName().replace(".db", ""); // remove the .db
            System.out.println("=====List of Users=====");
            System.out.println("User-> " + username);
        }
        System.out.println("=====End Of List=====");
        ;
    }

    // Command: save
    // Puts contents of currentUser's ArrayList to a file "baseFolder/username.db"
    public void saveAction() {
        // get data
        ArrayList<String> items = this.db.userMap.get(this.currentUser);
        System.out.println("Saved items -> " + items);
        // prepare filepath
        // String upperDirectory = "src/sdf-workshop03/src/cart";
        String outputPath = String.format("%s/%s.db", 
        this.baseFolder, this.currentUser);
        writeFile(outputPath, items);
    }

    public static void writeFile(String filename, ArrayList<String> items) {
        FileWriter fw;
        try {
            fw = new FileWriter(filename, false);
            BufferedWriter bfw = new BufferedWriter(fw);
            for (String item : items) {
                bfw.write(item + "\n");
            }
            bfw.flush();
            bfw.close();
        } catch (IOException e) {
            System.err.println("Can't write to file.");
        }
    }
}
// save data in 1 file? convenient JSON
// save each user in their separate files, to avoid loading the WHOLE DB