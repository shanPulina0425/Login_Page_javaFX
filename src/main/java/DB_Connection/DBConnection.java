package DB_Connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

    public class DBConnection {

        private static DBConnection instance;
        private Connection connection;

        private DBConnection() {

            String url="jdbc:mysql://localhost:3306/login_details";
            String user="root";
            String password="81805500";

            try {
                connection= DriverManager.getConnection(url,user,password);
            } catch (SQLException e) {
                e.printStackTrace();
            }




        }
        public static DBConnection getInstance(){

            if(instance ==null){
                instance=new DBConnection();

            }
            return instance;

        }

        public Connection getConnection(){
            return connection;
        }



    }


