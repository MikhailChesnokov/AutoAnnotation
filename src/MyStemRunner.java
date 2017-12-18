import java.io.*;
import java.util.ArrayList;
import java.util.List;

class MyStemRunner {
    // The downloadable MyStem and docs are available on "https://tech.yandex.ru/mystem/"

    private final static String pathToMyStem = "mystem.exe";
    private final static String keys = "-cs";                        /* "-cs" turns on sentences splitter {\s} */
    private final static String pathToTextFile = "text_example.txt"; /* !!! The text file must be encoded in Unicode */

    static List<String> run() {
        ProcessBuilder myStemProcessBuilder = new ProcessBuilder(pathToMyStem, keys, pathToTextFile);

        Process myStemProcess = null;
        try {
            myStemProcess = myStemProcessBuilder.start();
        } catch (IOException e) {
            System.out.println("Cannot run MyStem: " + e.getMessage());
        }

        List<String> paragraphs = new ArrayList<>();
        try (BufferedReader outputReader = new BufferedReader(new InputStreamReader(myStemProcess.getInputStream()))) {
            String outputLine;
            while ((outputLine = outputReader.readLine()) != null) {
                if (!outputLine.isEmpty()) {
                    paragraphs.add(outputLine.trim());
                }
            }
        } catch (IOException e) {
            System.out.println("Cannot read MyStem output: " + e.getMessage());
        }

        try {
            myStemProcess.waitFor(); /* Wait for stop process*/
        } catch (InterruptedException e) {
            System.out.println("Cannot stop MyStem: " + e.getMessage());
        }

        return paragraphs;
    }
}