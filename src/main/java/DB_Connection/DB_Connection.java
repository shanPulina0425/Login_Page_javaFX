package DB_Connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

    public class DB_Connection {

        private static DB_Connection instance;
        private Connection connection;

        private DB_Connection() {

            String url="jdbc:mysql://localhost:3306/login_details";
            String user="root";
            String password="81805500";

            try {
                connection= DriverManager.getConnection(url,user,password);
            } catch (SQLException e) {
                e.printStackTrace();
            }




        }
        public static DB_Connection getInstance(){

            if(instance ==null){
                instance=new DB_Connection();

            }
            return instance;

        }

        public Connection getConnection(){
            return connection;
        }



    }


