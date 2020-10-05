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
public class MeSubCommand extends SubCommand {
    public MeSubCommand(PlayerPoint plugin) {
        super(plugin);
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.hasPermission("PayerPoints.me") && sender.isPlayer();
    }

    @Override
    public String getName() {
        return "我的";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"me","my"};
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        UUID uuid = ((Player) sender).getUniqueId();
        sender.sendMessage(PlayerPoint.getInstance().getLanguage().getString("player.point.me")
                .replace("%point%", Point.myPoint(uuid)+"")
                .replace("%name%", Point.getPointName()));
        return false;
    }

    @Override
    public String getHelp() {
        return "§a/points me §7查看"+ Point.getPointName();
    }
}
