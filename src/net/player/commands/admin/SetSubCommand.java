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
public class SetSubCommand extends SubCommand {

    public SetSubCommand(PlayerPoints plugin) {
        super(plugin);
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.hasPermission("PayerPoints.set") && !sender.isPlayer();
    }

    @Override
    public String getName() {
        return "设置";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"set"};
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
                    Point.setPoint(player.getUniqueId(),p);
                    sender.sendMessage("成功设置 玩家"+player.getName()+p+Point.getPointName());
                }else{
                    UUID uuid1 = Point.getUUIDByPlayerName(name);
                    if(uuid1 != null){
                        Point.setPoint(uuid1,p);
                        sender.sendMessage("成功设置 玩家"+name+" "+p+Point.getPointName());
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
        return "§a/points set <玩家> <数量> §7设置玩家"+Point.getPointName()+"数量§c(控制台)";
    }
}
