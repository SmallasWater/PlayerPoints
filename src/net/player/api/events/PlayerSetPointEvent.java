package net.player.api.events;

import cn.nukkit.event.Cancellable;
import cn.nukkit.event.Event;
import cn.nukkit.event.HandlerList;

import java.util.UUID;

/**
 * @author 若水
 */
public class PlayerSetPointEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlers() {
        return handlers;
    }
    private UUID player;
    private double point;
    public PlayerSetPointEvent(UUID player, double point){
        this.player = player;
        this.point = point;
    }

    public UUID getPlayer() {
        return player;
    }

    public double getPoint() {
        return point;
    }

    public void setPoint(double point) {
        this.point = point;
    }

}
