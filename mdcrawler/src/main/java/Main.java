import java.io.IOException;
import java.util.ArrayList;

public class Main {

    static ArrayList<String> linkList = new ArrayList<>();

    public static void main(String[] args) {


        GetLinks getLink = new GetLinks();
        GetData getData = new GetData();
        WriteToDB writeToDB = new WriteToDB(getData);
        do {
            linkList=getLink.getLinks();
            for (String s : linkList) {
                getData.newData(s);
                writeToDB.write();
            }
        }while(!linkList.isEmpty());
    }
}
