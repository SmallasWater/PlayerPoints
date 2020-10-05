package net.player.commands.player;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import net.player.PlayerPoint;
import net.player.api.Point;
import net.player.commands.SubCommand;

import java.util.UUID;

/**
 * @author 若水
 */
public class PaySubCommand extends SubCommand {

    public PaySubCommand(PlayerPoint plugin) {
        super(plugin);
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.isPlayer();
    }

    @Override
    public String getName() {
        return "支付";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"pay"};
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(args.length > 2){
            String target = args[1];
            String pointString = args[2];
            double point;
            if(Point.isRightNumberPoint(pointString)){
                point = Double.parseDouble(pointString);
            }else{
                sender.sendMessage("请输入正确的数值");
                return false;
            }

            UUID player = Point.getUUIDByPlayerName(target);
            int i = Point.playerPayTargetPoint(((Player) sender).getUniqueId(),player,point);
            if(i == 0){
                sender.sendMessage(PlayerPoint.getInstance().getLanguage().getString("player.point.not.enough")
                        .replace("%name%", Point.getPointName())
                        .replace("%point%",String.format("%.2f", Point.myPoint(player))));
                return true;
            }else if(i == -1){
                sender.sendMessage("§c未找到玩家 "+target+" 相关数据");
                return true;
            }else{
                sender.sendMessage(PlayerPoint.getInstance().getLanguage().getString("player.point.pay")
                        .replace("%target%",target).replace("%point%",point+"").replace("%name%", Point.getPointName()));
            }
        }
        return false;
    }

    @Override
    public String getHelp() {
        return "§a/points pay <玩家> <数量> §7支付别人"+ Point.getPointName();
    }
}
