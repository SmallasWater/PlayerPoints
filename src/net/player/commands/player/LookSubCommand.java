package net.player.commands.player;

import cn.nukkit.command.CommandSender;
import net.player.PlayerPoint;
import net.player.api.Point;
import net.player.commands.SubCommand;

import java.util.UUID;

/**
 * @author 若水
 */
public class LookSubCommand extends SubCommand {

    public LookSubCommand(PlayerPoint plugin) {
        super(plugin);
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.hasPermission("PayerPoints.look");
    }

    @Override
    public String getName() {
        return "查看";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"look","see"};
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(args.length > 1){
            String name = args[1];
            UUID uuid = Point.getUUIDByPlayerName(name);
            if(uuid != null){
                sender.sendMessage("§e玩家 §a"+name+"§e"+ Point.getPointName()+"数量为"+ Point.myPoint(uuid));
            }else{
                sender.sendMessage("§c未找到玩家 "+name+"相关数据");
            }
        }
        return false;
    }

    @Override
    public String getHelp() {
        return "§a/points look <玩家> §7查看别人"+ Point.getPointName();
    }
}
