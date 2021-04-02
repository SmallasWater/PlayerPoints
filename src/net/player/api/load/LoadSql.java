package net.player.api.load;

import com.smallaswater.easysql.api.SqlEnable;
import com.smallaswater.easysql.exceptions.MySqlLoginException;
import com.smallaswater.easysql.mysql.utils.TableType;
import com.smallaswater.easysql.mysql.utils.Types;
import com.smallaswater.easysql.mysql.utils.UserData;
import net.player.PlayerPoint;

/**
 * @author SmallasWater
 * Create on 2021/1/26 14:19
 * Package net.player.api.load
 */
public class LoadSql {

    private SqlEnable enable = null;
    public boolean getLoadSql(){
        if(PlayerPoint.getInstance().isCanLoadSql()){
            return loadSql();
        }
        return false;
    }

    public SqlEnable getEnable() {
        return enable;
    }

    private boolean loadSql(){
        String user = PlayerPoint.getInstance().getConfig().getString("database.MySQL.username");
        int port = PlayerPoint.getInstance().getConfig().getInt("database.MySQL.port");
        String url = PlayerPoint.getInstance().getConfig().getString("database.MySQL.host");
        String passWorld = PlayerPoint.getInstance().getConfig().getString("database.MySQL.password");
        String table = PlayerPoint.getInstance().getConfig().getString("database.MySQL.database");
        UserData data = new UserData(user,passWorld,url,port,table);
        try {
            enable = new SqlEnable(PlayerPoint.getInstance(),
                    PlayerPoint.TABLE_NAME, data, new TableType("user", Types.CHAR), new TableType("count", Types.DOUBLE));
        }catch (MySqlLoginException e){
            PlayerPoint.getInstance().getLogger().info(e.getMessage());
            return false;
        }
        return true;
    }
}
