package net.player;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.network.protocol.ModalFormResponsePacket;
import cn.nukkit.utils.Config;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.player.api.Point;
import net.player.api.events.PlayerAddPointEvent;
import net.player.api.events.PlayerReducePointEvent;
import net.player.api.events.PlayerSetPointEvent;
import net.player.window.CreateWindow;

import java.util.UUID;

/**
 * @author ZXR
 */
public class PointListener implements Listener {
    private static final String POINT = "%point%";

    private static final String POINT_NAME = "%name%";


    @EventHandler
    public void onAddPoint(PlayerAddPointEvent event){
        if(event.isCancelled()){
            return;
        }
        UUID uuid = event.getPlayer();
        Config config = PlayerPoints.getInstance().getPointConfig();
        double old = config.getDouble(event.getPlayer().toString());
        Player player = Server.getInstance().getOfflinePlayer(uuid).getPlayer();
        if(old + event.getPoint() > PlayerPoints.getInstance().getMaxPoint()){
            event.setCancelled();
            if(player != null){
                player.sendMessage(PlayerPoints.getInstance().getLanguage().getString("player.point.add.max"));
            }
            return;
        }
        if(player != null){
            player.sendMessage(PlayerPoints.getInstance().getLanguage().getString("player.point.add")
                    .replace(POINT,String.format("%.2f",event.getPoint())).replace(POINT_NAME,
                            PlayerPoints.getInstance().getPointName()));
        }

        config.set(event.getPlayer().toString(),event.getPoint() + old);
        config.save();

    }


    private static final String NULL = "null";
    @EventHandler
    public void getUI(DataPacketReceiveEvent event){
        String data;
        ModalFormResponsePacket ui;
        Player player = event.getPlayer();
        if((event.getPacket() instanceof ModalFormResponsePacket)) {
            ui = (ModalFormResponsePacket) event.getPacket();
            data = ui.data.trim();
            int fromId = ui.formId;
            switch(fromId) {
                case CreateWindow.MENU:
                    if(NULL.equals(data)){
                        return;
                    }
                    if(Integer.parseInt(data) == 0){
                        CreateWindow.sendPay(player);
                        return;
                    }else if(Integer.parseInt(data) == 1){
                        if(PlayerPoints.getInstance().timer.containsKey(player.getName())){
                            player.sendMessage(PlayerPoints.getInstance().getLanguage().getString("player.point.broadcast.cold")
                                    .replace("%time%",PlayerPoints.getInstance().timer.get(player.getName())+""));
                        }else{
                            PlayerPoints.getInstance().timer.put(player.getName(),PlayerPoints.getInstance().getTime());
                            PlayerPoints.getInstance().getServer().broadcastMessage(PlayerPoints.getInstance().getLanguage().getString("player.point.broadcast")
                                    .replace("%target%",player.getName())
                                    .replace(POINT, String.format("%.2f",Point.myPoint(player)))
                                    .replace(POINT_NAME,PlayerPoints.getInstance().getPointName()));
                        }
                        return;
                    }else if(Integer.parseInt(data) == 2){
                        CreateWindow.sendLead(player);
                        return;

                    }else if(Integer.parseInt(data) == 3){
                        player.sendMessage(PlayerPoints.getInstance().getLanguage().getString("player.point.me")
                                .replace("%point%",Point.myPoint(player)+"")
                                .replace("%name%",Point.getPointName()));

                    }
                    break;
                case CreateWindow.PAY:
                    if(NULL.equals(data)){
                        return;
                    }
                    Object[] datas = decodeData(data);
                    if(datas == null || datas.length < 1){
                        return;
                    }
                    String target = (String) datas[0];
                    String p = (String) datas[1];
                    if(Point.isRightNumberPoint(p)){
                        int i = Point.playerPayTargetPoint(player.getUniqueId(),Point.getUUIDByPlayerName(target),Double.parseDouble(p));
                        if(i == 0){
                            player.sendMessage("§c您的"+Point.getPointName()+"并不够哦");
                        }else if(i == -1){
                            player.sendMessage("§c未找到玩家 "+target+" 相关数据");
                        }else{
                            player.sendMessage(PlayerPoints.getInstance().getLanguage().getString("player.point.pay")
                                    .replace("%target%",target)
                                    .replace("%point%",Double.parseDouble(p)+"")
                                    .replace("%name%",Point.getPointName()));
                        }
                    }else{
                        player.sendMessage("§c请输入正确的数值 ");
                    }
                    break;
                    default:break;


            }
        }
    }
    private static Object[] decodeData(String data){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.fromJson(data, new TypeToken<Object[]>(){}.getType());
    }

    @EventHandler
    public void onReducePoint(PlayerReducePointEvent event){
        if(event.isCancelled()){
            return;
        }
        UUID uuid = event.getPlayer();
        Player player = Server.getInstance().getOfflinePlayer(uuid).getPlayer();
        if(player != null){
            player.sendMessage(PlayerPoints.getInstance().getLanguage().getString("player.point.remove")
                    .replace(POINT,String.format("%.2f",event.getPoint())).replace(POINT_NAME,
                            PlayerPoints.getInstance().getPointName()));
        }
        Config config = PlayerPoints.getInstance().getPointConfig();
        double old = config.getDouble(event.getPlayer().toString());
        config.set(event.getPlayer().toString(),old - event.getPoint());
        config.save();

    }

    @EventHandler
    public void onSetPoint(PlayerSetPointEvent event){
        if(event.isCancelled()){
            return;
        }
        UUID uuid = event.getPlayer();
        Player player = Server.getInstance().getOfflinePlayer(uuid).getPlayer();
        if(player != null){
            player.sendMessage(PlayerPoints.getInstance().getLanguage().getString("player.point.set")
                    .replace(POINT,String.format("%.2f",event.getPoint())).replace(POINT_NAME,
                            PlayerPoints.getInstance().getPointName()));
        }
        Config config = PlayerPoints.getInstance().getPointConfig();
        config.set(event.getPlayer().toString(), event.getPoint());
        config.save();

    }
}
