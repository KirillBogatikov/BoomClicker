package org.boomgames.boomclicker;

import java.sql.ResultSet;

import org.joker.query.crud.SelectQuery;
import org.joker.session.Session;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private Database database = Database.getInstance();
    
    @GetMapping("/user/id/{id}")
    public ResponseEntity<User> getById(@PathVariable("id") Long id) {
        try(Session session = database.openSession()) {
            SelectQuery query = new SelectQuery(User.class, database.dialect());
            query.columns("nick", "score").where().equals("id", id);
            ResultSet result = session.newStatement().executeQuery(query.build());
            if(!result.next()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            
            User user = new User();
            user.setId(id);
            user.setNick(result.getString("nick"));
            user.setScore(result.getLong("score"));
            
            return ResponseEntity.ok(user);
        } catch(Throwable t) {
            t.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/user/nick/{nick}")
    public ResponseEntity<User> getByNick(@PathVariable("nick") String nick) {
        try(Session session = database.openSession()) {
            SelectQuery query = new SelectQuery(User.class, database.dialect());
            query.columns("score").where().equals("nick", nick);
            ResultSet result = session.newStatement().executeQuery(query.build());
            if(!result.next()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            
            User user = new User();
            user.setNick(nick);
            user.setScore(result.getLong("score"));
            
            return ResponseEntity.ok(user);
        } catch(Throwable t) {
            t.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/user/login/{nick}")
    public ResponseEntity<User> login(@PathVariable("nick") String nick, @RequestParam("password") String password) {
        try(Session session = database.openSession()) {
            SelectQuery query = new SelectQuery(User.class, database.dialect());
            query.columns("id", "password", "score").where().equals("nick", nick);
            ResultSet result = session.newStatement().executeQuery(query.build());
            if(!result.next()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            
            password = Hash.of(password);
            
            if(!password.equals(result.getString("password"))) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            
            User user = new User();
            user.setId(result.getLong("id"));
            user.setNick(nick);
            user.setScore(result.getLong("score"));
            
            return ResponseEntity.ok(user);
        } catch(Throwable t) {
            t.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping("/user")
    public ResponseEntity<Object> post(@RequestBody User user) {
        UserError userError = new UserError();
        if(user.getNick() == null || user.getNick().isEmpty()) {
            userError.setNickValid(false);
            return new ResponseEntity<>(userError, HttpStatus.BAD_REQUEST);
        }
        if(user.getPassword() == null || user.getPassword().length() < User.MIN_PASSWORD_LENGTH) {
            userError.setPasswordValid(false);
            return new ResponseEntity<>(userError, HttpStatus.BAD_REQUEST);
        }
        
        try(Session session = database.openSession()) {
            SelectQuery query = new SelectQuery(User.class, database.dialect());
            query.column("id").where().equals("nick", user.getNick());
            ResultSet result = session.newStatement().executeQuery(query.build());
            
            if(result.next()) {
                userError.setNickBusy(true);
                return new ResponseEntity<>(userError, HttpStatus.BAD_REQUEST);
            }
                        
            user.setScore(0);
            user.setPassword(Hash.of(user.getPassword()));
            session.create(user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch(Throwable t) {
            t.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PutMapping("/user")
    public ResponseEntity<Object> put(@RequestBody User user) {
        try(Session session = database.openSession()) {
            SelectQuery query = new SelectQuery(User.class, database.dialect());
            query.column("password").where().equals("nick", user.getNick());
            ResultSet result = session.newStatement().executeQuery(query.build());
            if(!result.next()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            
            String column = result.getString("password");
            user.setPassword(Hash.of(user.getPassword()));
            
            if(!column.equals(user.getPassword())) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            
            session.save(user);
            user.setPassword(null);
            return ResponseEntity.ok(user);
        } catch(Throwable t) {
            t.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
