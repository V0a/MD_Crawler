import java.io.IOException;

public class MainSolo {

    public static void main(String[] args) throws IOException, InterruptedException {
        String[] url = {"https://mangadex.org/title/42731/a-false-confession",
                "https://mangadex.org/title/44256/a-fool-and-a-girl",
                "https://mangadex.org/title/47855/a-friend-who-i-went-to-the-same-middle-school-with-but-have-never-talked-to",
                "https://mangadex.org/title/40154/a-gamer-living-with-a-cat",
                "https://mangadex.org/title/33804/a-girl-falls-in-love-with-a-boy",
                "https://mangadex.org/title/21070/a-girl-in-the-clouds",
                "https://mangadex.org/title/41300/a-girl-like-a-pilgrim",
                "https://mangadex.org/title/45085/a-girl-like-alien",
                "https://mangadex.org/title/45078/a-girl-meets-sex-toys-akane-oguri-indulge-in-onanism",
                "https://mangadex.org/title/44164/a-girl-who-always-ends-up-having-sex-with-her-close-male-friend",
                "https://mangadex.org/title/42778/a-girl-who-confesses-with-rap-she-just-learned",
                "https://mangadex.org/title/43141/a-girl-who-thinks-i-m-the-support-role",
                "https://mangadex.org/title/20292/a-fish-came"};

        GetData getData = new GetData();
        WriteToDB writeToDB = new WriteToDB(getData);
        for (String string : url) {
            getData.newData(string);
            writeToDB.write();
        }



    }

}
