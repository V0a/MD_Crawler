public class AddSingleManga {

    static String urlToAdd="https://mangadex.org/title/43476/yakuza-reincarnation";

    static String host="";
    static String database="";
    static String user="";
    static String password="";

    //Dumps for links of mangas that couldn't be read
    static String errorFilePath="mdcrawler/src/main/resources/unadded.txt";

    //Dump of all added mangas
    static String nameDumpFilepath="mdcrawler/src/main/resources/latest.txt";

    public static void main(String[] args){
        GetData getData = new GetData(errorFilePath,nameDumpFilepath);
        WriteToDB writeToDB = new WriteToDB(getData,host,database,user,password);

        getData.newData(urlToAdd);
        writeToDB.write();

    }
}
