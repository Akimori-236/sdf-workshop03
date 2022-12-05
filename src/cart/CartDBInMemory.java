package cart;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class CartDBInMemory {

    // hashmap to keep track of the carts
    public HashMap<String, ArrayList<String>> userMap = new HashMap<>();

    public CartDBInMemory(String baseFolder) {
        this.loadDataFromFiles(baseFolder);
    }

    public void loadDataFromFiles(String baseFolder) {
        // base folder as file
        File f = new File(baseFolder);
        // make list of files from inside the baseFolder
        File[] filteredFiles = f.listFiles(new FilenameFilter() {

            // filter the files that end with '.db'
            @Override
            public boolean accept(File dir, String filename) {
                return filename.endsWith(".db");
            } // find files with the .db suffix/fileType
        });

        // no files found, exit function
        if (filteredFiles.length <= 0) {
            return;
        }

        // read each file and make dictionary/hashmap
        // structure => {"username/filename" : [fileData]}
        for (File file : filteredFiles) {
            // get username from filename
            String username = file.getName().replace(".db", ""); // retrieving only the filename
            // read content of file
            this.userMap.put(username, ReadFile(file));
        }
    }

    // READER
    public ArrayList<String> ReadFile(File f) {
        ArrayList<String> dataList = new ArrayList<>();
        try {
            BufferedReader bfr = new BufferedReader(new FileReader(f)); // pass fr into bfr
            String line;
            while ((line = bfr.readLine()) != null) {
                line = line.trim();
                dataList.add(line);
                // reading each line and adding to the array
            }
            bfr.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataList;
    }
}
