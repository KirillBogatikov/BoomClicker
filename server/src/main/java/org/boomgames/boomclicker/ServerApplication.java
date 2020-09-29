package org.boomgames.boomclicker;

import org.joker.session.Session;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@SpringBootApplication
public class ServerApplication {

    public static void main(String[] args) {
        String url = System.getenv("DATABASE_URL");
        String user, password;
        
        int dogIndex = url.indexOf("@");
        String[] parts = url.substring(0, dogIndex).split(":");
        user = parts[1].substring(2);
        password = parts[2];
        url = url.substring(dogIndex + 1);
                
        Database database = Database.newInstance(url, user, password);
        try(Session session = database.openSession()) {
            session.create(User.class, false);
            SpringApplication.run(ServerApplication.class, args);
        } catch(Throwable t) {
            t.printStackTrace();
        }        
    }

}
