package net.player.api.events;

import cn.nukkit.event.Cancellable;
import cn.nukkit.event.Event;
import cn.nukkit.event.HandlerList;

public class PlayerPayTargetEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlers() {
        return handlers;
    }

    private Object player, target;
    private double point;

    public PlayerPayTargetEvent(Object player, Object target, double point) {
        this.player = player;
        this.point = point;
        this.target = target;

    }

    public double getPoint() {
        return point;
    }

    public void setPoint(double point) {
        this.point = point;
    }

    public Object getPlayer() {
        return player;
    }

    public Object getTarget() {
        return target;
    }
}
