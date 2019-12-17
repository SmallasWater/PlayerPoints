package net.player.commands.admin;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import net.player.PlayerPoints;
import net.player.api.Point;
import net.player.commands.SubCommand;

import java.util.UUID;



/**
 * @author 若水
 */
public class TakeSubCommand extends SubCommand {
    public TakeSubCommand(PlayerPoints plugin) {
        super(plugin);
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.hasPermission("PayerPoints.take") && !sender.isPlayer();
    }

    @Override
    public String getName() {
        return "扣除";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"take","remove","reduce","减少","拿"};
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(args.length > 2){
            String name = args[1];
            String point = args[2];
            double p;
            if(Point.isRightNumberPoint(point)){
                p = Double.parseDouble(point);
            }else{
                sender.sendMessage("请输入正确的数值");
                return false;
            }
            if(p > 0){
                Player player = Server.getInstance().getPlayer(name);
                if(player != null){
                    if(Point.reducePoint(player.getUniqueId(),p)){
                        sender.sendMessage("成功扣除 玩家"+player.getName()+" "+p+Point.getPointName());
                    }else{
                        sender.sendMessage("玩家"+name+"没有这么多"+Point.getPointName()+" 当前数量:"+Point.myPoint(player));
                    }

                }else{
                    UUID uuid1 = Point.getUUIDByPlayerName(name);
                    if(uuid1 != null){
                        if(Point.reducePoint(uuid1,p)){
                            sender.sendMessage("成功扣除 玩家"+Point.getPlayerNameByUUID(uuid1)+" "+p+Point.getPointName());
                        }else{
                            sender.sendMessage("玩家"+name+"没有这么多"+Point.getPointName()+" 当前数量:"+Point.myPoint(uuid1));
                        }
                    }else{
                        sender.sendMessage("未找到 玩家"+name+"的 相关数据");
                    }
                }
            }else{
                sender.sendMessage("点券数量必须大于0");
            }
        }
        return false;
    }

    @Override
    public String getHelp() {
        return "§a/points take <玩家> <数量> §7扣除玩家一定数量的"+Point.getPointName()+"§c(控制台)";
    }

}
