import java.util.ArrayList;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class GetLinks {

    int page =1;
    Document document;


    public GetLinks() {}


    public ArrayList<String> getLinks() {
        try {
            ArrayList <String> stringList = new ArrayList<>();

            Thread.sleep(1000+new Random().nextInt(1000));
            document = Jsoup.connect("https://mangadex.org/titles/2/"+page).get();

            for (Element mangaEntry : document.select("div.manga-entry.col-lg-6.border-bottom.pl-0.my-1")) {
                stringList.add(mangaEntry.child(0).select("a").get(0).absUrl("href"));
            }
            page++;
            return stringList;
        } catch (Exception e) {
            e.printStackTrace();
            try{
                Thread.sleep(5000);
            }catch(Exception e2){
                e2.printStackTrace();
            }
            return getLinks();
        }
    }
}
