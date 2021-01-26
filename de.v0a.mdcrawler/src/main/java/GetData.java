import java.util.ArrayList;
import java.util.Random;

public class GetData {

    static ArrayList<String> rowDescription = new ArrayList<String>();

    int MangaCounter =287*40;
    int counter = 0;


    String url ="";
    String name ="";
    String imageLink ="";
    int originalLanguage=0;

    int mangaID = 0;
    ArrayList<String> altNames = new ArrayList<String>();
    ArrayList<String> authors = new ArrayList<String>();
    ArrayList<String> artists = new ArrayList<String>();
    int demographic =0;
    ArrayList<Integer> content = new ArrayList<Integer>();
    ArrayList<Integer> format = new ArrayList<Integer>();
    ArrayList<Integer> genre = new ArrayList<Integer>();
    ArrayList<Integer> theme = new ArrayList<Integer>();
    double rating = 0.00;
    int publicationStatus = 0;
    int views =0;
    int follows =0;
    int chapters =0;
    String description = "";
    ArrayList<Integer> relatedMangaID = new ArrayList<Integer>();

    ArrayList<String> tempList = new ArrayList<String>();
    String temp;

    public GetData() {}

    public void newData(String url) throws InterruptedException {
        resetVars();
        this.url=url;
        try {
            //Gets the code of the specified site
            org.jsoup.nodes.Document document;

            Thread.sleep(1000+new Random().nextInt(1000));
            document = Jsoup.connect(url).get();

            getData(document,url);
            printData();
            counter =0;
        } catch (Exception e) {
            System.out.println("Error: "+e.toString());
            Thread.sleep(3000);
            if (counter<5) {
                counter++;
                newData(url);
            }else{

            }
        }
    }

    private void getData(Document document, String url) {
        //Gets the attributes available
        rowDescription.addAll(document.select("div.col-lg-3.col-xl-2.strong").eachText());

        //Getting name of the manga
        name=document.select("span.mx-1").get(0).text().replaceAll("\"","''");

        //Getting link for the front image
        imageLink=document.select("img.rounded").outerHtml();
        imageLink=imageLink.substring(39,imageLink.length()-2);


        //Getting the ID of the original language of the manga
        temp=document.select("h6.card-header.d-flex.align-items-center.py-2 > [title]").outerHtml();
        originalLanguage=getLanguageID(temp.substring(42,temp.length()-9));

        //Goes through all rows to find all needed available data
        for (int i = 0; i < rowDescription.size(); i++) {
            switch (rowDescription.get(i)) {


                case "Title ID:":
                    mangaID = Integer.parseInt(document.select("div.col-lg-9.col-xl-10").get(i).text());
                    break;


                case "Alt name(s):":
                    altNames.addAll(document.select("div.col-lg-9.col-xl-10").get(i).children().get(0).children().eachText());
                    for (int j = 0; j < altNames.size(); j++) {
                        altNames.set(j,altNames.get(j).replaceAll("\"","''"));
                    }
                    break;


                case "Author:":
                    authors.addAll(Arrays.asList(document.select("div.col-lg-9.col-xl-10").get(i).text().split(", ")));
                    for (int j = 0; j < authors.size(); j++) {
                        authors.set(j,authors.get(j).replaceAll("\"","''"));
                    }
                    break;


                case "Artist:":
                    artists.addAll(Arrays.asList(document.select("div.col-lg-9.col-xl-10").get(i).text().split(", ")));
                    for (int j = 0; j < artists.size(); j++) {
                        artists.set(j,artists.get(j).replaceAll("\"","''"));
                    }
                    break;


                case "Demographic:":
                    demographic=getDemographicID(document.select("div.col-lg-9.col-xl-10").get(i).text());
                    break;


                case "Content:":
                    tempList.addAll(document.select("div.col-lg-9.col-xl-10").get(i).children().eachText());
                    for (int j = 0; j < tempList.size(); j++) {
                        content.add(getContentID(tempList.get(j)));
                    }
                    tempList=new ArrayList<String>();
                    break;

                case "Format:":
                    tempList.addAll(document.select("div.col-lg-9.col-xl-10").get(i).children().eachText());
                    for (int j = 0; j < tempList.size(); j++) {
                        format.add(getFormatID(tempList.get(j)));
                    }
                    tempList=new ArrayList<String>();
                    break;


                case "Genre:":
                    tempList.addAll(document.select("div.col-lg-9.col-xl-10").get(i).children().eachText());
                    for (int j = 0; j < tempList.size(); j++) {
                        genre.add(getGenreID(tempList.get(j)));
                    }
                    tempList=new ArrayList<String>();
                    break;


                case "Theme:":
                    tempList.addAll(document.select("div.col-lg-9.col-xl-10").get(i).children().eachText());
                    for (int j = 0; j < tempList.size(); j++) {
                        theme.add(getThemeID(tempList.get(j)));
                    }
                    tempList=new ArrayList<String>();
                    break;


                case "Rating:":
                    rating=Double.parseDouble(document.select("div.col-lg-9.col-xl-10").get(i).child(0).child(0).text());
                    break;


                case "Pub. status:":
                    temp=document.select("div.col-lg-9.col-xl-10").get(i).text();
                    publicationStatus=getPublicationStatusID(temp);
                    break;


                case "Stats:":
                    views=Integer.parseInt(document.select("div.col-lg-9.col-xl-10").get(i).child(0).child(0).text().replace(",",""));
                    follows=Integer.parseInt(document.select("div.col-lg-9.col-xl-10").get(i).child(0).child(1).text().replace(",",""));
                    chapters=Integer.parseInt(document.select("div.col-lg-9.col-xl-10").get(i).child(0).child(2).text().replace(",",""));
                    break;


                case "Description:":
                    description=document.select("div.col-lg-9.col-xl-10").get(i).text().replaceAll("\"","''");
                    break;


                case "Related:":
                    tempList.addAll(document.select("div.col-lg-9.col-xl-10").get(i).child(0).children().select("a").eachAttr("href"));
                    for (String link : tempList) {
                        relatedMangaID.add(Integer.parseInt(link.split("/")[2]));
                    }
                    tempList=new ArrayList<String>();
                    break;


                default:
                    break;
            }
        }
    }

    private int getLanguageID(String originalLanguage) {
        switch (originalLanguage) {
            case "Japanese":
                return 1;
            case "English":
                return 2;
            case "Polish":
                return 3;
            case "Russian":
                return 4;
            case "German":
                return 5;
            case "French":
                return 6;
            case "Vietnamese":
                return 7;
            case "Chinese (Simp)":
                return 8;
            case "Indonesian":
                return 9;
            case "Korean":
                return 10;
            case "Spanish (LATAM)":
                return 11;
            case "Thai":
                return 12;
            case "Filipino":
                return 13;
            case "Chinese (Trad)":
                return 14;
            default:
                return 0;
        }
    }
    private int getDemographicID(String demographicString) {
        switch (demographicString) {
            case "Shounen":
                return 1;
            case "Shoujo":
                return 2;
            case "Seinen":
                return 3;
            case "Josei":
                return 4;
            default:
                return 0;
        }
    }
    private int getPublicationStatusID(String publicationStatusString) {
        switch (publicationStatusString) {
            case "Ongoing":
                return 1;
            case "Completed":
                return 2;
            case "Cancelled":
                return 3;
            case "Hiatus":
                return 4;
            default:
                return 0;
        }
    }
    private int getContentID(String contentString) {
        switch (contentString) {
            case "Ecchi":
                return 1;
            case "Gore":
                return 2;
            case "Sexual Violence":
                return 3;
            case "Smut":
                return 4;
            default:
                return 0;
        }
    }
    private int getFormatID(String formatString) {
        switch (formatString) {
            case "4-Koma":
                return 1;
            case "Adaptation":
                return 2;
            case "Anthology":
                return 3;
            case "Award Winning":
                return 4;
            case "Doujinshi":
                return 5;
            case "Fan Colored":
                return 6;
            case "Full Color":
                return 7;
            case "Long Strip":
                return 8;
            case "Official Colored":
                return 9;
            case "Oneshot":
                return 10;
            case "User Created":
                return 11;
            case "Web Comic":
                return 12;
            default:
                return 0;
        }
    }
    private int getGenreID(String genreString) {
        switch (genreString) {
            case "Action":
                return 1;
            case "Adventure":
                return 2;
            case "Comedy":
                return 3;
            case "Crime":
                return 4;
            case "Drama":
                return 5;
            case "Fantasy":
                return 6;
            case "Historical":
                return 7;
            case "Horror":
                return 8;
            case "Isekai":
                return 9;
            case "Magical Girls":
                return 10;
            case "Mecha":
                return 11;
            case "Medical":
                return 12;
            case "Mystery":
                return 13;
            case "Philosophical":
                return 14;
            case "Psychological":
                return 15;
            case "Romance":
                return 16;
            case "Sci-Fi":
                return 17;
            case "Shoujo Ai":
                return 18;
            case "Shounen Ai":
                return 19;
            case "Slice of Life":
                return 20;
            case "Sports":
                return 21;
            case "Superhero":
                return 22;
            case "Thriller":
                return 23;
            case "Tragedy":
                return 24;
            case "Wuxia":
                return 25;
            case "Yaoi":
                return 26;
            case "Yuri":
                return 27;
            default:
                return 0;
        }
    }
    private int getThemeID(String themeString) {
        switch (themeString) {
            case "Aliens":
                return 1;
            case "Animals":
                return 2;
            case "Cooking":
                return 3;
            case "Crossdressing":
                return 4;
            case "Delinquents":
                return 5;
            case "Demons":
                return 6;
            case "Genderswap":
                return 7;
            case "Ghosts":
                return 8;
            case "Gyaru":
                return 9;
            case "Harem":
                return 10;
            case "Incest":
                return 11;
            case "Loli":
                return 12;
            case "Mafia":
                return 13;
            case "Magic":
                return 14;
            case "Martial Arts":
                return 15;
            case "Military":
                return 16;
            case "Monster Girls":
                return 17;
            case "Monsters":
                return 18;
            case "Music":
                return 19;
            case "Ninja":
                return 20;
            case "Office Workers":
                return 21;
            case "Police":
                return 22;
            case "Post-Apocalyptic":
                return 23;
            case "Reincarnation":
                return 24;
            case "Reverse Harem":
                return 25;
            case "Samurai":
                return 26;
            case "School Life":
                return 27;
            case "Shota":
                return 28;
            case "Supernatural":
                return 29;
            case "Survival":
                return 30;
            case "Time Travel":
                return 31;
            case "Traditional Games":
                return 32;
            case "Vampires":
                return 33;
            case "Video Games":
                return 34;
            case "Virtual Reality":
                return 35;
            case "Zombies":
                return 36;
            default:
                return 0;
        }
    }

    private void resetVars() {
        rowDescription = new ArrayList<String>();

        name ="";
        imageLink ="";
        originalLanguage=0;

        mangaID = 0;
        altNames = new ArrayList<String>();
        authors = new ArrayList<String>();
        artists = new ArrayList<String>();
        demographic =0;
        content = new ArrayList<Integer>();
        format = new ArrayList<Integer>();
        genre = new ArrayList<Integer>();
        theme = new ArrayList<Integer>();
        rating = 0.00;
        publicationStatus = 0;
        views =0;
        follows =0;
        chapters =0;
        description = "";
        relatedMangaID = new ArrayList<Integer>();

        temp="";
        tempList=new ArrayList<String>();
    }

    public String getLink() {
        return url;
    }
    public String getName() {
        return name;
    }
    public String getImageLink() {
        return imageLink;
    }
    public int getOriginalLanguage() {
        return originalLanguage;
    }
    public int getMangaID() {
        return mangaID;
    }
    public ArrayList<String> getAltNames() {
        return altNames;
    }
    public ArrayList<String> getAuthors() {
        return authors;
    }
    public ArrayList<String> getArtists() {
        return artists;
    }
    public int getDemographic() {
        return demographic;
    }
    public ArrayList<Integer> getContent() {
        return content;
    }
    public ArrayList<Integer> getFormat() {
        return format;
    }
    public ArrayList<Integer> getGenre() {
        return genre;
    }
    public ArrayList<Integer> getTheme() {
        return theme;
    }
    public double getRating() {
        return rating;
    }
    public int getPublicationStatus() {
        return publicationStatus;
    }
    public int getViews() {
        return views;
    }
    public int getFollows() {
        return follows;
    }
    public int getChapters() {
        return chapters;
    }
    public String getDescription() {
        return description;
    }
    public ArrayList<Integer> getRelatedMangaID() {
        return relatedMangaID;
    }

    private void printData() {
        System.out.println("Name:               "+name);
        for (String s : altNames) {System.out.println("Alt Name:           "+s);}
        System.out.println("Title ID:           "+mangaID);
        for(String s : authors)   {System.out.println("Author:             "+s);}
        for(String s : artists)   {System.out.println("Artist:             "+s);}
        System.out.println("Rating:             "+rating);
        System.out.println("Demographic:        "+demographic);
        for (int i : content)     {System.out.println("Content:            "+i);}
        for (int i : format)      {System.out.println("Format:             "+i);}
        for (int i : genre)       {System.out.println("Genre:              "+i);}
        for (int i : theme)       {System.out.println("Theme:              "+i);}
        System.out.println("Views:              "+views);
        System.out.println("Follows:            "+follows);
        System.out.println("Chapters:           "+chapters);
        System.out.println("Description:        "+description);
        System.out.println("Image Link:         "+imageLink);
        System.out.println("Original Language:  "+originalLanguage);
        System.out.println("Publication Status: "+publicationStatus);
        for (int i : relatedMangaID) {System.out.println("Related Manga ID:   "+i);}
        System.out.println("Link:               "+url);
        System.out.println("");
        System.out.println("");
        MangaCounter++;
        System.out.println("MANGA NR:           "+MangaCounter);
        System.out.println("");
        System.out.println("");

    }
}
