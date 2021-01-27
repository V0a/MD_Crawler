import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("unused")
public class MySQLConnector {

    private final String HOST;
    private final String DATABASE;
    private final String USER;
    private final String PASSWORD;
    private int PORT = 3306;

    @SuppressWarnings("FieldCanBeLocal")
    private final boolean reconnect = true;
    private final boolean mute = false;

    private Connection con;

    public MySQLConnector(String HOST, String DATABASE, String USER, String PASSWORD) {
        this.HOST = HOST;
        this.DATABASE = DATABASE;
        this.USER = USER;
        this.PASSWORD = PASSWORD;

        System.out.println("Connecting to Database");

        connect();
    }

    public MySQLConnector(String HOST, String DATABASE, String USER, String PASSWORD, boolean autoConnect, boolean thread) {
        this.HOST = HOST;
        this.DATABASE = DATABASE;
        this.USER = USER;
        this.PASSWORD = PASSWORD;

        System.out.println("[Information]: Connecting to Database");


        if(autoConnect && thread) {
            new Thread(MySQLConnector.this::connect).start();
        }
        else if(autoConnect) {
            connect();
        }
    }


    public void connect() {
        try {
            con = DriverManager.getConnection("jdbc:mysql://" + HOST + ":"+PORT+"/" + DATABASE + "?autoReconnect="+reconnect, USER, PASSWORD);

            if(!mute) {
                System.out.println("===========================");
                System.out.println("Connected to MySQL database");
                System.out.println("===========================");
            }

        }catch(SQLException ex) {
            ex.printStackTrace();
        }

    }


    public void cancelConnection() {
        try {
            if(con != null) {
                con.close();
            }
            else {
                if(!mute) {
                    System.out.println("[Information]: Can't disconnect if no connection available");
                }
            }
        }catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void update(String query) {
        try {
            java.sql.Statement st = con.createStatement();
            st.execute(query);
            st.close();

        }catch(SQLException ex) {
            connect();
            ex.printStackTrace();
        }
    }

    public ResultSet query(String query) {
        ResultSet rs = null;

        try {
            java.sql.Statement st = con.createStatement();
            rs = st.executeQuery(query);
        }catch(SQLException ex) {
            connect();
            ex.printStackTrace();
        }

        return rs;
    }

    public Object queryObject(String query) {
        ResultSet rs;

        try {
            java.sql.Statement st = con.createStatement();
            rs = st.executeQuery(query);
            return rs.getObject(1);
        }catch(SQLException ex) {
            connect();
            ex.printStackTrace();
        }
        return null;
    }

    public void setPort(int PORT){
        this.PORT = PORT;
    }

    public int getPort(){
        return PORT;
    }
}
