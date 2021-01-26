import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import org.jsoup.*;
import org.jsoup.nodes.Document;

public class GetData {

    static ArrayList<String> rowDescription = new ArrayList<>();

    int MangaCounter = 0;
    int roundCounter = 0;
    BufferedWriter bwerr;
    BufferedWriter bwlatest;

    public GetData(){
        try{
            bwerr=new BufferedWriter(new FileWriter("resources/unadded.txt",true));
            bwlatest=new BufferedWriter(new FileWriter("resources/latest.txt",false));

        }catch(Exception e){
            e.printStackTrace();
        }

    }

    String url ="";
    String name ="";
    String imageLink ="";
    int originalLanguage=0;

    int mangaID = 0;
    ArrayList<String> altNames = new ArrayList<>();
    ArrayList<String> authors = new ArrayList<>();
    ArrayList<String> artists = new ArrayList<>();
    int demographic =0;
    ArrayList<Integer> content = new ArrayList<>();
    ArrayList<Integer> format = new ArrayList<>();
    ArrayList<Integer> genre = new ArrayList<>();
    ArrayList<Integer> theme = new ArrayList<>();
    double rating = 0.00;
    int publicationStatus = 0;
    int views =0;
    int follows =0;
    int chapters =0;
    String description = "";
    ArrayList<Integer> relatedMangaID = new ArrayList<>();

    ArrayList<String> tempList = new ArrayList<>();
    String temp;

    public void newData(String url) {
        resetVars();
        this.url=url;
        try {
            //Gets the code of the specified site
            org.jsoup.nodes.Document document;

            Thread.sleep(1000+new Random().nextInt(1000));
            document = Jsoup.connect(url).get();

            getData(document,url);
            printData();
            bwlatest.append("Latest Manga: ").append(getName()).append(" ID:").append(String.valueOf(getMangaID()));
            bwlatest.flush();
            roundCounter =0;
        } catch (Exception e) {
            System.out.println("Error: "+e.toString());
            try{
                Thread.sleep(3000);
            }catch(Exception e2){
                e2.printStackTrace();
            }
            if (roundCounter<5) {
                roundCounter++;
                newData(url);
            }else{
                try{
                    bwerr.newLine();
                    bwerr.append(url);
                    bwerr.flush();
                }catch(Exception e2){
                    e2.printStackTrace();
                }
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
                    for (String s : tempList) {
                        content.add(getContentID(s));
                    }
                    tempList=new ArrayList<>();
                    break;

                case "Format:":
                    tempList.addAll(document.select("div.col-lg-9.col-xl-10").get(i).children().eachText());
                    for (String s : tempList) {
                        format.add(getFormatID(s));
                    }
                    tempList=new ArrayList<>();
                    break;


                case "Genre:":
                    tempList.addAll(document.select("div.col-lg-9.col-xl-10").get(i).children().eachText());
                    for (String s : tempList) {
                        genre.add(getGenreID(s));
                    }
                    tempList=new ArrayList<>();
                    break;


                case "Theme:":
                    tempList.addAll(document.select("div.col-lg-9.col-xl-10").get(i).children().eachText());
                    for (String s : tempList) {
                        theme.add(getThemeID(s));
                    }
                    tempList=new ArrayList<>();
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
                    tempList=new ArrayList<>();
                    break;


                default:
                    break;
            }
        }
    }

    private int getLanguageID(String originalLanguage) {
        return switch (originalLanguage) {
            case "Japanese" -> 1;
            case "English" -> 2;
            case "Polish" -> 3;
            case "Russian" -> 4;
            case "German" -> 5;
            case "French" -> 6;
            case "Vietnamese" -> 7;
            case "Chinese (Simp)" -> 8;
            case "Indonesian" -> 9;
            case "Korean" -> 10;
            case "Spanish (LATAM)" -> 11;
            case "Thai" -> 12;
            case "Filipino" -> 13;
            case "Chinese (Trad)" -> 14;
            default -> 0;
        };
    }
    private int getDemographicID(String demographicString) {
        return switch (demographicString) {
            case "Shounen" -> 1;
            case "Shoujo" -> 2;
            case "Seinen" -> 3;
            case "Josei" -> 4;
            default -> 0;
        };
    }
    private int getPublicationStatusID(String publicationStatusString) {
        return switch (publicationStatusString) {
            case "Ongoing" -> 1;
            case "Completed" -> 2;
            case "Cancelled" -> 3;
            case "Hiatus" -> 4;
            default -> 0;
        };
    }
    private int getContentID(String contentString) {
        return switch (contentString) {
            case "Ecchi" -> 1;
            case "Gore" -> 2;
            case "Sexual Violence" -> 3;
            case "Smut" -> 4;
            default -> 0;
        };
    }
    private int getFormatID(String formatString) {
        return switch (formatString) {
            case "4-Koma" -> 1;
            case "Adaptation" -> 2;
            case "Anthology" -> 3;
            case "Award Winning" -> 4;
            case "Doujinshi" -> 5;
            case "Fan Colored" -> 6;
            case "Full Color" -> 7;
            case "Long Strip" -> 8;
            case "Official Colored" -> 9;
            case "Oneshot" -> 10;
            case "User Created" -> 11;
            case "Web Comic" -> 12;
            default -> 0;
        };
    }
    private int getGenreID(String genreString) {
        return switch (genreString) {
            case "Action" -> 1;
            case "Adventure" -> 2;
            case "Comedy" -> 3;
            case "Crime" -> 4;
            case "Drama" -> 5;
            case "Fantasy" -> 6;
            case "Historical" -> 7;
            case "Horror" -> 8;
            case "Isekai" -> 9;
            case "Magical Girls" -> 10;
            case "Mecha" -> 11;
            case "Medical" -> 12;
            case "Mystery" -> 13;
            case "Philosophical" -> 14;
            case "Psychological" -> 15;
            case "Romance" -> 16;
            case "Sci-Fi" -> 17;
            case "Shoujo Ai" -> 18;
            case "Shounen Ai" -> 19;
            case "Slice of Life" -> 20;
            case "Sports" -> 21;
            case "Superhero" -> 22;
            case "Thriller" -> 23;
            case "Tragedy" -> 24;
            case "Wuxia" -> 25;
            case "Yaoi" -> 26;
            case "Yuri" -> 27;
            default -> 0;
        };
    }
    private int getThemeID(String themeString) {
        return switch (themeString) {
            case "Aliens" -> 1;
            case "Animals" -> 2;
            case "Cooking" -> 3;
            case "Crossdressing" -> 4;
            case "Delinquents" -> 5;
            case "Demons" -> 6;
            case "Genderswap" -> 7;
            case "Ghosts" -> 8;
            case "Gyaru" -> 9;
            case "Harem" -> 10;
            case "Incest" -> 11;
            case "Loli" -> 12;
            case "Mafia" -> 13;
            case "Magic" -> 14;
            case "Martial Arts" -> 15;
            case "Military" -> 16;
            case "Monster Girls" -> 17;
            case "Monsters" -> 18;
            case "Music" -> 19;
            case "Ninja" -> 20;
            case "Office Workers" -> 21;
            case "Police" -> 22;
            case "Post-Apocalyptic" -> 23;
            case "Reincarnation" -> 24;
            case "Reverse Harem" -> 25;
            case "Samurai" -> 26;
            case "School Life" -> 27;
            case "Shota" -> 28;
            case "Supernatural" -> 29;
            case "Survival" -> 30;
            case "Time Travel" -> 31;
            case "Traditional Games" -> 32;
            case "Vampires" -> 33;
            case "Video Games" -> 34;
            case "Virtual Reality" -> 35;
            case "Zombies" -> 36;
            default -> 0;
        };
    }

    private void resetVars() {
        rowDescription = new ArrayList<>();

        name ="";
        imageLink ="";
        originalLanguage=0;

        mangaID = 0;
        altNames = new ArrayList<>();
        authors = new ArrayList<>();
        artists = new ArrayList<>();
        demographic =0;
        content = new ArrayList<>();
        format = new ArrayList<>();
        genre = new ArrayList<>();
        theme = new ArrayList<>();
        rating = 0.00;
        publicationStatus = 0;
        views =0;
        follows =0;
        chapters =0;
        description = "";
        relatedMangaID = new ArrayList<>();

        temp="";
        tempList=new ArrayList<>();
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
        System.out.println();
        System.out.println();
        MangaCounter++;
        System.out.println("MANGA NR:           "+MangaCounter);
        System.out.println();
        System.out.println();

    }

}