import java.sql.ResultSet;
import java.sql.SQLException;

public class WriteToDB {
    MySQLConnector mySQL;
    GetData data;

    public WriteToDB(GetData data) {
        this.data=data;
        mySQL = new MySQLConnector("127.0.0.1", "mangadatanew", "Mangauser", "12345678");
    }

    public void write() {

        try {
            mySQL.update("INSERT IGNORE INTO manga (MangaID, MangaName, Rating, Views, Follows, TotalChapters, "
                    +    "Link, Description, ImageLink, LanguageID, DemographicID, PublicationStatusID)"+
                    " VALUES ("+
                    data.getMangaID()+","+
                    "\""+data.getName()+"\""+","+
                    data.getRating()+","+
                    data.getViews()+","+
                    data.getFollows()+","+
                    data.getChapters()+","+
                    "\""+data.getLink()+"\""+","+
                    "\""+data.getDescription()+"\""+","+
                    "\""+data.getImageLink()+"\""+","+
                    data.getOriginalLanguage()+","+
                    data.getDemographic()+","+
                    data.getPublicationStatus()+
                    ");");
        } catch (Exception e) {System.out.println(data.getName()+" had an Error");}

        try {
            for (String s : data.getAltNames()) {
                mySQL.update("INSERT IGNORE INTO alternativename (AlternativeName, MangaID) VALUES (\""+s+"\","+data.mangaID+");");
            }
        } catch (Exception e) {System.out.println(data.getName()+" alt names had an Error");}

        try {
            for (int i : data.getRelatedMangaID()) {
                mySQL.update("INSERT IGNORE INTO relatedmanga (RelatedMangaID, MangaID) VALUES ("+i+","+data.mangaID+");");
            }
        } catch (Exception e) {System.out.println(data.getName()+" related manga had an Error");}

        try {
            for (int i : data.getContent()) {
                mySQL.update("INSERT IGNORE INTO mangacontent (ContentID, MangaID) VALUES ("+i+","+data.mangaID+");");
            }
        } catch (Exception e) {System.out.println(data.getName()+" content had an Error");}

        try {
            for (int i : data.getFormat()) {
                mySQL.update("INSERT IGNORE INTO mangaformat (FormatID, MangaID) VALUES ("+i+","+data.mangaID+");");
            }
        } catch (Exception e) {System.out.println(data.getName()+" format had an Error");}

        try {
            for (int i : data.getGenre()) {
                mySQL.update("INSERT IGNORE INTO mangagenre (GenreID, MangaID) VALUES ("+i+","+data.mangaID+");");
            }
        } catch (Exception e) {System.out.println(data.getName()+" genre had an Error");}

        try {
            for (int i : data.getTheme()) {
                mySQL.update("INSERT IGNORE INTO mangatheme (ThemeID, MangaID) VALUES ("+i+","+data.mangaID+");");
            }
        } catch (Exception e) {System.out.println(data.getName()+" theme had an Error");}

        try {
            for (String s : data.getAuthors()) {
                if (!mySQL.query("SELECT AuthorName FROM author WHERE AuthorName = \""+s+"\";").next()) {
                    mySQL.update("INSERT IGNORE INTO author (AuthorName) VALUES (\""+s+"\");");
                }
                mySQL.update("INSERT IGNORE INTO mangaauthor (AuthorID, MangaID) VALUES ("+getFirstValue(mySQL.query("SELECT AuthorID FROM author WHERE AuthorName = \""+s+"\";"))+","+data.mangaID+");");
            }
        } catch (Exception e) {System.out.println(data.getName()+" author had an Error");}

        try {
            for (String s : data.getArtists()) {
                if (!mySQL.query("SELECT ArtistName FROM artist WHERE ArtistName = \""+s+"\";").next()) {
                    mySQL.update("INSERT IGNORE INTO artist (ArtistName) VALUES (\""+s+"\");");
                }
                mySQL.update("INSERT IGNORE INTO mangaartist (ArtistID, MangaID) VALUES ("+getFirstValue(mySQL.query("SELECT ArtistID FROM artist WHERE ArtistName = \""+s+"\";"))+","+data.mangaID+");");
            }
        } catch (Exception e) {System.out.println(data.getName()+" artist had an Error");}
    }

    private String getFirstValue (ResultSet set) throws SQLException {
        set.next();
        return set.getString(1);
    }

}