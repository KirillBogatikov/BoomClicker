package org.boomgames.boomclick.api;

import org.boomgames.boomclicker.User;
import org.cuba.neofit.NeoCall;
import org.cuba.neofit.annotations.Body;
import org.cuba.neofit.annotations.Get;
import org.cuba.neofit.annotations.Path;
import org.cuba.neofit.annotations.Post;
import org.cuba.neofit.annotations.Put;
import org.cuba.neofit.annotations.Query;
import org.cuba.neofit.annotations.Service;

@Service("/user")
public interface UserApi {
    @Get("id/{id}")
    public NeoCall get(@Path("id") Long id);

    @Get("login/{nick}")
    public NeoCall login(@Path("nick") String nick, @Query("password") String password);

    @Post("")
    public NeoCall signup(@Body User user);

    @Put("")
    public NeoCall update(@Body User user);
}
