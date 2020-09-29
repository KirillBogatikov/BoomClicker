package org.boomgames.boomclicker;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.joker.query.OrderByQuery.SortingOrder;
import org.joker.query.crud.SelectQuery;
import org.joker.session.Session;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TableController {
    private Database database = Database.getInstance();
    
    @GetMapping("/table/top")
    public ResponseEntity<List<User>> top() {
        try(Session session = database.openSession()) {
            SelectQuery query = new SelectQuery(User.class, database.dialect());
            query.columns("id", "nick", "score")
                 .orderBy().orderBy("score", SortingOrder.DESC);
            query.limit(10);
            
            ResultSet result = session.newStatement().executeQuery(query.build());
            if(!result.next()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            
            List<User> users = new ArrayList<>();
            do {
                User user = new User();
                user.setId(result.getLong("id"));
                user.setNick(result.getString("nick"));
                user.setScore(result.getLong("score"));
                users.add(user);
            } while(result.next());
            
            return ResponseEntity.ok(users);
        } catch(Throwable t) {
            t.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/table")
    public ResponseEntity<List<User>> list(@RequestParam int offset, @RequestParam int limit) {
        try(Session session = database.openSession()) {
            SelectQuery query = new SelectQuery(User.class, database.dialect());
            query.columns("id", "nick", "score")
                 .orderBy().orderBy("score", SortingOrder.DESC);
            query.offset(offset).limit(limit);
            
            ResultSet result = session.newStatement().executeQuery(query.build());
            if(!result.next()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            
            List<User> users = new ArrayList<>();
            do {
                User user = new User();
                user.setId(result.getLong("id"));
                user.setNick(result.getString("nick"));
                user.setScore(result.getLong("score"));
                users.add(user);
            } while(result.next());
            
            return ResponseEntity.ok(users);
        } catch(Throwable t) {
            t.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/table/count")
    public ResponseEntity<Integer> countUsers() {
        try(Session session = database.openSession()) {
            SelectQuery query = new SelectQuery(User.class, database.dialect());
            query.count("nick");
            
            ResultSet result = session.newStatement().executeQuery(query.build());
            if(!result.next()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            
            return ResponseEntity.ok(result.getInt(1));
        } catch(Throwable t) {
            t.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
