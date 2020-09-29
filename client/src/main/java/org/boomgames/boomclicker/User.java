package org.boomgames.boomclicker;

import org.joker.annotations.Column;
import org.joker.annotations.PrimaryKey;
import org.joker.annotations.Table;

@Table("users")
public class User {
    public static final int MIN_PASSWORD_LENGTH = 8;
    
    @Column
    @PrimaryKey(generator = Object.class)
    private Long id;
    
    @Column
    private String nick;
    
    @Column
    private String password;
    
    @Column
    private long score;
    
    public Long getId() {
        return id;
    }
    
    public String getNick() {
        return nick;
    }

    public String getPassword() {
        return password;
    }
    
    public long getScore() {
        return score;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public void setNick(String nick) {
        this.nick = nick;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public void setScore(long score) {
        this.score = score;
    }
}
