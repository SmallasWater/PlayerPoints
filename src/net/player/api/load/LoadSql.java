package net.player.api.load;


import com.smallaswater.easysql.exceptions.MySqlLoginException;
import com.smallaswater.easysql.mysql.utils.TableType;
import com.smallaswater.easysql.mysql.utils.Types;
import com.smallaswater.easysql.mysql.utils.UserData;
import com.smallaswater.easysql.v3.mysql.manager.SqlManager;
import net.player.PlayerPoint;

/**
 * @author SmallasWater
 * Create on 2021/1/26 14:19
 * Package net.player.api.load
 */
public class LoadSql {

    private SqlManager enable = null;
    public boolean getLoadSql(){
        if(PlayerPoint.getInstance().canUseSql()){
            return loadSql();
        }
        return false;
    }

    public SqlManager getEnable() {
        return enable;
    }

    private boolean loadSql(){
        PlayerPoint.getInstance().getLogger().info("正在连接数据库");
        String user = PlayerPoint.getInstance().getConfig().getString("database.MySQL.username");
        int port = PlayerPoint.getInstance().getConfig().getInt("database.MySQL.port");
        String url = PlayerPoint.getInstance().getConfig().getString("database.MySQL.host");
        String passWorld = PlayerPoint.getInstance().getConfig().getString("database.MySQL.password");
        String table = PlayerPoint.getInstance().getConfig().getString("database.MySQL.database");
        UserData data = new UserData(user,passWorld,url,port,table);
        try {

            enable = new SqlManager(PlayerPoint.getInstance(),data);
            enable.createTable(PlayerPoint.TABLE_NAME, new TableType("user", Types.CHAR),
                    new TableType("count", Types.DOUBLE));
            PlayerPoint.getInstance().getLogger().info("数据库连接成功");
        }catch (MySqlLoginException e){
            PlayerPoint.getInstance().getLogger().info(e.getMessage());
            return false;
        }
        return true;
    }
}
