import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MainSolo {

    static BufferedReader br;
    static String temp;
    public static void main(String[] args){
        ArrayList<String> urls = new ArrayList<>();
        try {
            br = new BufferedReader(new FileReader("mdcrawler/src/main/resources/unadded.txt"));
            String line = br.readLine();

            while (line!=null){
                urls.add(br.readLine());
                line= br.readLine();
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        GetData getData = new GetData();
        WriteToDB writeToDB = new WriteToDB(getData);
        for (String string : urls) {
            getData.newData(string);
            writeToDB.write();
        }

    }
}
