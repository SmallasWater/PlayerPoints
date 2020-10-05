package net.player.commands.admin;

import cn.nukkit.command.CommandSender;
import net.player.PlayerPoint;
import net.player.api.Point;
import net.player.commands.SubCommand;

import java.util.UUID;

/**
 * @author 若水
 */
public class ResetSubCommand extends SubCommand {


    public ResetSubCommand(PlayerPoint plugin) {
        super(plugin);
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.hasPermission("PayerPoints.reset") && !sender.isPlayer();
    }

    @Override
    public String getName() {
        return "重置";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"reset","clean","clear","清空"};
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(args.length > 1){
            String target = args[1];
            UUID uuid = Point.getUUIDByPlayerName(target);
            if(uuid != null){
                Point.setPoint(uuid,0.0D);
                sender.sendMessage("成功清空 玩家 "+target+" "+ Point.getPointName());
            }else{
                sender.sendMessage("未找到 玩家"+target+"的 相关数据");
            }
        }
        return false;
    }

    @Override
    public String getHelp() {
        return "§a/points reset <玩家> §7清空玩家"+ Point.getPointName()+ "§c(控制台)";
    }
}
