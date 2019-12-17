package net.player.task;

import cn.nukkit.scheduler.Task;
import net.player.PlayerPoints;

/**
 * @author 若水
 */
public class TimerTask extends Task {
    @Override
    public void onRun(int i) {
        for(String player: PlayerPoints.getInstance().timer.keySet()){
            int t = PlayerPoints.getInstance().timer.get(player);
            if(t > 0){
                PlayerPoints.getInstance().timer.put(player,t-1);
            }else{
                PlayerPoints.getInstance().timer.remove(player);
            }
        }
    }
}
