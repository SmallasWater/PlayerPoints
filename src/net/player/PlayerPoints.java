package net.player;

import cn.nukkit.Server;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import net.player.commands.PointCommand;
import net.player.task.TimerTask;
import updata.AutoData;


import java.io.File;
import java.util.LinkedHashMap;


/**
 * @author 若水
 */
public class PlayerPoints extends PluginBase {

    private static PlayerPoints instance;

    private String name;

    private double defaultPoint;

    private double maxPoint;

    private int time;

    private int count;

    private Config language;

    private Config pointConfig = null;

    public LinkedHashMap<String,Integer> timer = new LinkedHashMap<>();

    @Override
    public void onEnable() {
        instance = this;
        this.getServer().getLogger().info("点券系统正在启动...");
        if(Server.getInstance().getPluginManager().getPlugin("AutoUpData") != null){
            if(AutoData.defaultUpData(this,this.getFile(),"SmallasWater","PlayerPoints")){
                return;
            }
        }
        this.load();
        getServer().getCommandMap().register("PlayerPoints", new PointCommand(this));
        this.getServer().getPluginManager().registerEvents(new PointListener(),this);
        this.getServer().getScheduler().scheduleRepeatingTask(new TimerTask(),20);
    }

    public void load(){
        this.saveDefaultConfig();
        this.reloadConfig();
        if(!new File(this.getDataFolder()+"/language.yml").exists()){
            saveResource("language.yml",false);
        }
        language = new Config(this.getDataFolder()+"/language.yml",Config.YAML);
        name = getConfig().getString("货币名称");
        pointConfig = getPointConfig();
        defaultPoint = getConfig().getDouble("基础数量");
        maxPoint = getConfig().getDouble("点券最大值");
        time = getConfig().getInt("喊话冷却");
        count = getConfig().getInt("排行榜显示玩家数量");
    }

    public String getPointName() {
        return name;
    }


    public Config getLanguage() {
        return language;
    }

    public Config getPointConfig(){
        if(pointConfig == null){
            return new Config(this.getDataFolder()+"/point.yml",Config.YAML);
        }
        return pointConfig;
    }

    public int getTime() {
        return time;
    }

    public int getCount() {
        return count;
    }

    public static PlayerPoints getInstance() {
        return instance;
    }

    public double getMaxPoint() {
        return maxPoint;
    }

    public double getDefaultPoint() {
        return defaultPoint;
    }
}