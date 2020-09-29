package org.boomgames.boomclicker;

public class UserError {
    private boolean nickValid;
    private boolean passwordValid;
    private boolean nickBusy;
    
    public boolean isNickValid() {
        return nickValid;
    }
    
    public boolean isPasswordValid() {
        return passwordValid;
    }
    
    public boolean isNickBusy() {
        return nickBusy;
    }
    
    public void setNickValid(boolean nickValid) {
        this.nickValid = nickValid;
    }
    
    public void setPasswordValid(boolean passwordValid) {
        this.passwordValid = passwordValid;
    }
    
    public void setNickBusy(boolean nickBusy) {
        this.nickBusy = nickBusy;
    }
    
}
