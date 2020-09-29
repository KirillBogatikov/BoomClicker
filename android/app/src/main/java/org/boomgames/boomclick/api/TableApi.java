package org.boomgames.boomclick.api;

import org.cuba.neofit.annotations.Get;
import org.cuba.neofit.annotations.Query;
import org.cuba.neofit.annotations.Service;
import org.cuba.neofit.NeoCall;

@Service("/table")
public interface TableApi {
    @Get("top")
    public NeoCall top();

    @Get("")
    public NeoCall list(@Query("offset") int offset, @Query("limit") int limit);

    @Get("count")
    public NeoCall count();
}
