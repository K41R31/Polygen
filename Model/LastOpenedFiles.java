package Polygen.Model;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class LastOpenedFiles {

    private ArrayList<String> lastOpened = new ArrayList<>();

    public LastOpenedFiles() {
        try {
            lastOpened = readFile();
        } catch (IOException e) { e.printStackTrace(); }
    }


    public void setOpenedFiles(String newFile) { try { writeFile(newFile); } catch (IOException e) { e.printStackTrace(); } }

    public ArrayList<String> getOpenedFiles() { return lastOpened; }


    /**
     * Writes the given String into "Data/lastOpened.txt"
     * only if the String isn't written already.
     */
    private void writeFile(String newFile) throws IOException { //Writes the String into "Data/lastOpened.txt"
        for (String search : lastOpened) {
            if (search.equals(newFile)) return; //Returns, if the String is written already
        }
        lastOpened.add(newFile);
        Charset utf8 = StandardCharsets.UTF_8;
            Files.write(Paths.get("Polygen/Data/lastOpened.txt"), lastOpened, utf8);
    }

    /**
     * Deletes the given String in the File
     * "Data/lastOpened.txt".
     */
    public void deleteStringInFile(String deleteString) {
        for (String lastOpened : lastOpened) {
            int index = lastOpened.indexOf(deleteString);
            if (index > -1) {
                System.out.println(index);
            }
        }
    }

    /**
     * Simply reads and returns the content of the File
     * "Data/lastOpened.txt".
     */
    private ArrayList<String> readFile() throws IOException {
        String line;
        ArrayList<String> result = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader("Polygen/Data/lastOpened.txt"));
            while((line = reader.readLine())!= null) {
                result.add(line);
            }
        return result;
    }
}
