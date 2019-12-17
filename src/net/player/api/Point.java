package net.player.api;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.utils.Config;
import net.player.PlayerPoints;
import net.player.api.events.PlayerAddPointEvent;
import net.player.api.events.PlayerReducePointEvent;
import net.player.api.events.PlayerSetPointEvent;


import java.io.File;
import java.util.*;
import java.util.regex.Pattern;

/**
 * API类
 * @author 若水
 */
public class Point {

    public static void addPoint(UUID player,double point){
        if(point > 0){
            PlayerAddPointEvent event = new PlayerAddPointEvent(player,point);
            Server.getInstance().getPluginManager().callEvent(event);
        }

    }

    public static void addPoint(Player player, double point){
        addPoint(player.getUniqueId(),point);
    }


    public static void setPoint(UUID player,double point){
        PlayerSetPointEvent event = new PlayerSetPointEvent(player,point);
        Server.getInstance().getPluginManager().callEvent(event);
    }

    public static void setPoint(Player player, double point){
        setPoint(player.getUniqueId(),point);
    }


    public static boolean reducePoint(UUID player,double point){
        if(point > 0){
            Config config = PlayerPoints.getInstance().getPointConfig();
            double old = config.getDouble(player.toString());
            if(old < point){
                return false;
            }else{
                PlayerReducePointEvent event = new PlayerReducePointEvent(player,point);
                Server.getInstance().getPluginManager().callEvent(event);
                return true;
            }
        }
        return false;
    }
    public static int playerPayTargetPoint(Player player, Player target, double point){
        return playerPayTargetPoint(player.getUniqueId(),target.getUniqueId(),point);
    }

    public static int playerPayTargetPoint(UUID player, UUID target, double point){
        if(player != null && target != null){
            if(myPoint(player) >= point){
                reducePoint(player,point);
                addPoint(target,point);
                return 1;
            }else{
                return 0;
            }
        }

        return -1;
    }



    public static boolean reducePoint(Player player, double point){
        return reducePoint(player.getUniqueId(),point);
    }

    public static String getPointName(){
        return PlayerPoints.getInstance().getPointName();
    }

    public static double getDefaultPoint(){
        return PlayerPoints.getInstance().getDefaultPoint();
    }

    public static double getMaxPoint(){
        return PlayerPoints.getInstance().getMaxPoint();
    }

    public static double myPoint(UUID player){
        Config config = PlayerPoints.getInstance().getPointConfig();
        return config.getDouble(player.toString());
    }

    public static double myPoint(Player player){
        return myPoint(player.getUniqueId());
    }


    public static String getPlayerNameByUUID(UUID uuid){
        return Server.getInstance().getOfflinePlayer(uuid).getName();
    }

    private static final Pattern P =  Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}.dat$");
    public static UUID getUUIDByPlayerName(String name){
        Player player = Server.getInstance().getPlayer(name);
        if(player != null){
            return player.getUniqueId();
        }else{
            File dataDirectory = new File(Server.getInstance().getDataPath(), "players/");
            File[] files = dataDirectory.listFiles((file) -> {
                String names = file.getName();
                return P.matcher(names).matches() && names.endsWith(".dat");
            });
            if (files != null) {
                for(File file:files){
                    String uu = file.getName();
                    uu = uu.substring(0, uu.length() - 4);
                    UUID uuid = UUID.fromString(uu);
                    if(Server.getInstance().getOfflinePlayer(uuid).getName().equals(name)){
                        return uuid;
                    }
                }
            }
        }

        return null;
    }
    public static boolean isRightNumberPoint(String point){
        try {
            double p = Double.parseDouble(point);
            return p > 0;
        }catch (Exception e){
            return false;
        }
    }

    /**
     * String 为 UUID
     * 排行榜
     * */
    public static HashMap<String,Double> getPlayerRankingList(){
        Map<String,Object> map = PlayerPoints.getInstance().getPointConfig().getAll();
        HashMap<String,Double> rank = new HashMap<>(map.size());
        for(String name:map.keySet()){
            rank.put(name, (double)map.get(name));
        }
        List<Map.Entry<String, Double>> wordMap = new ArrayList<>(rank.entrySet());
        wordMap.sort((o1, o2) -> {
            double result = o2.getValue() - o1.getValue();
            if (result > 0) {
                return 1;
            } else if (result == 0) {
                return 0;
            } else {
                return -1;
            }
        });
        return rank;
    }
}
