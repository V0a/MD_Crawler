import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Main {

    static ArrayList<String> linkList = new ArrayList<>();
    static String host="";
    static String database="";
    static String user="";
    static String password="";

    //Dumps for links of mangas that couldn't be read
    static String errorFilePath="mdcrawler/src/main/resources/unadded.txt";
    static String errorFilePath2="mdcrawler/src/main/resources/unaddedtwice.txt";

    //Dump of all added mangas
    static String nameDumpFilepath="mdcrawler/src/main/resources/latest.txt";

    static String linkGetUrlPart="https://mangadex.org/titles/2/";
    //Full URL is for example https://mangadex.org/titles/2/5/

    public static void main(String[] args) {

        GetLinks getLink = new GetLinks(linkGetUrlPart);
        GetData getData = new GetData(errorFilePath,nameDumpFilepath);
        WriteToDB writeToDB = new WriteToDB(getData,host,database,user,password);
        do {
            linkList=getLink.getLinks();
            for (String s : linkList) {
                getData.newData(s);
                writeToDB.write();
            }
        }while(!linkList.isEmpty());

        getData.closeErrorWriter();

        BufferedReader br;

        ArrayList<String> urls = new ArrayList<>();
        try {
            br = new BufferedReader(new FileReader(errorFilePath));
            String line = br.readLine();
                while (line!=null){
                    urls.add(br.readLine());
                    line= br.readLine();
                }
                br.close();
            }catch(Exception e){
                e.printStackTrace();
            }
            getData = new GetData(errorFilePath2,nameDumpFilepath);
            writeToDB = new WriteToDB(getData,host,database,user,password);
            for (String string : urls) {
                getData.newData(string);
                writeToDB.write();
            }
        writeToDB.cancelConnection();
    }
}
