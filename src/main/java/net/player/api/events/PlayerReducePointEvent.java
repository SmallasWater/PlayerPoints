package net.player.api.events;


import cn.nukkit.event.Cancellable;
import cn.nukkit.event.Event;
import cn.nukkit.event.HandlerList;


/**
 * @author 若水
 */
public class PlayerReducePointEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlers() {
        return handlers;
    }
    private Object player;
    private double point;
    public PlayerReducePointEvent(Object player, double point){
        this.player = player;
        this.point = point;
    }

    public Object getPlayer() {
        return player;
    }

    public double getPoint() {
        return point;
    }

    public void setPoint(double point) {
        this.point = point;
    }

}
