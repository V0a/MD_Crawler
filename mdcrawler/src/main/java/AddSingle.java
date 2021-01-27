public class AddSingle {

    public static void main(String[] args){
        GetData getData = new GetData();
        WriteToDB writeToDB = new WriteToDB(getData);

        getData.newData("https://mangadex.org/title/43476/yakuza-reincarnation\n");
        writeToDB.write();

    }
}

