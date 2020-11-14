package net.player;

import cn.nukkit.Server;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import com.smallaswater.easysql.api.SqlEnable;
import com.smallaswater.easysql.exceptions.MySqlLoginException;
import com.smallaswater.easysql.mysql.utils.TableType;
import com.smallaswater.easysql.mysql.utils.Types;
import com.smallaswater.easysql.mysql.utils.UserData;
import net.player.api.load.LoadMcRmb;
import net.player.api.load.LoadMoney;
import net.player.commands.PointCommand;

import updata.AutoData;


import java.io.File;



/**
 * @author 若水
 */
public class PlayerPoint extends PluginBase {

    public static final String TABLE_NAME = "PlayerPoint";

    private static PlayerPoint instance;

    private String name;

    private double defaultPoint;

    private LoadMoney load;

    private double maxPoint;

    private boolean canLoadSql = false;

    private int count;

    private Config language;

    private Config pointConfig = null;

    private SqlEnable enable;

    private int rmb;

    private LoadMcRmb mcRmb;


    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        reloadConfig();
        this.getServer().getLogger().info("点券系统正在启动...");
        if(Server.getInstance().getPluginManager().getPlugin("AutoUpData") != null){
            if(AutoData.defaultUpData(this,this.getFile(),"SmallasWater","PlayerPoints")){
                return;
            }
        }
        if(canUseSql()){
            if(Server.getInstance().getPluginManager().getPlugin("EasyMySQL") != null) {
                if (loadSql()) {
                    canLoadSql = true;
                }
            }
        }

        this.load();
        mcRmb = new LoadMcRmb(getConfig().getInt("mcrmb.sid",0),getConfig().getString("mcrmb.sign"));
        load = new LoadMoney();
        getServer().getCommandMap().register("PlayerPoints", new PointCommand(this));
        this.getServer().getPluginManager().registerEvents(new PointListener(),this);
    }

    public LoadMcRmb getMcRmb() {
        return mcRmb;
    }

    public boolean isCanLoadSql() {
        return canLoadSql;
    }

    private boolean loadSql(){
        String user = getConfig().getString("database.MySQL.username");
        int port = getConfig().getInt("database.MySQL.port");
        String url = getConfig().getString("database.MySQL.host");
        String passWorld = getConfig().getString("database.MySQL.password");
        String table = getConfig().getString("database.MySQL.database");
        UserData data = new UserData(user,passWorld,url,port,table);
        try {
            this.enable = new SqlEnable(this,
                    TABLE_NAME, data, new TableType("user", Types.CHAR), new TableType("count", Types.DOUBLE));
        }catch (MySqlLoginException e){
            this.getLogger().info(e.getMessage());
            return false;
        }
        return true;
    }


    public LoadMoney getLoad() {
        return load;
    }

    public void load(){
        if(!new File(this.getDataFolder()+"/language.yml").exists()){
            saveResource("language.yml",false);
        }
        language = new Config(this.getDataFolder()+"/language.yml",Config.YAML);
        name = getConfig().getString("货币名称");
        pointConfig = getPointConfig();
        defaultPoint = getConfig().getDouble("基础数量");
        maxPoint = getConfig().getDouble("点券最大值");
        count = getConfig().getInt("排行榜显示玩家数量");
        rmb = getConfig().getInt("rmb与点券兑换比例",10);

    }

    public int getRmb() {
        return rmb;
    }

    public String getPointName() {
        return name;
    }


    public Config getLanguage() {
        return language;
    }

    public Config getPointConfig(){
        if(pointConfig == null){
           if(canSaveUUID()){
                pointConfig = new Config(this.getDataFolder()+"/point.yml",Config.YAML);
            }else{
                pointConfig = new Config(this.getDataFolder()+"/pointPlayer.yml",Config.YAML);
            }
        }
        return pointConfig;
    }

    public boolean canUseSql(){
        return getConfig().getBoolean("database.open");
    }
    public Config getPlayerUUIDConfig(){
        return new Config(this.getDataFolder()+"/point.yml",Config.YAML);
    }

    public Config getPlayerNameConfig(){
        return new Config(this.getDataFolder()+"/pointPlayer.yml",Config.YAML);
    }

    public boolean canSaveUUID(){
        return getConfig().getInt("存储类型",0) == 0;
    }

    public int getCount() {
        return count;
    }

    public SqlEnable getEnable() {
        return enable;
    }

    public static PlayerPoint getInstance() {
        return instance;
    }

    public double getMaxPoint() {
        return maxPoint;
    }

    public double getDefaultPoint() {
        return defaultPoint;
    }

    @Override
    public void onDisable() {
        if(this.enable != null){
            enable.disable();
        }
    }
}