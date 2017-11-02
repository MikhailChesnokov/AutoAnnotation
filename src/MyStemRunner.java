import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

class MyStemRunner {
    // Downloadable MyStem and Docs is available on "https://tech.yandex.ru/mystem/"

    private static String pathToMyStem = "C:/Users/MIKHAIL/Desktop/mystem.exe";
    private static String keys = "-cs";                                         /* turns on sentences splitter {\s} */
    private static String pathToTextFile = "C:/Users/MIKHAIL/Desktop/text.txt"; /* !!! The text file must be encoded in Unicode */

    public static List<String> runMyStem() {
        ProcessBuilder myStemProcessBuilder = new ProcessBuilder(pathToMyStem,keys, pathToTextFile);

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
                paragraphs.add(outputLine.trim());
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