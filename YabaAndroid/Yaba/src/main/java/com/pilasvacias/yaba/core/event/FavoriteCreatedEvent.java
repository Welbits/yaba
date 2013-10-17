package com.pilasvacias.yaba.core.event;

/**
 * Created by IzanRodrigo on 17/10/13.
 */
public class FavoriteCreatedEvent {
    private final String favorite;

    public FavoriteCreatedEvent(String favorite) {
        this.favorite = favorite;
    }

    public String getFavorite() {
        return favorite;
    }
}
