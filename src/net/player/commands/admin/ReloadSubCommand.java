package net.player.commands.admin;

import cn.nukkit.command.CommandSender;
import net.player.PlayerPoint;
import net.player.commands.SubCommand;

/**
 * @author 若水
 */
public class ReloadSubCommand extends SubCommand {

    public ReloadSubCommand(PlayerPoint plugin) {
        super(plugin);
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.hasPermission("PayerPoints.reload") && !sender.isPlayer();
    }

    @Override
    public String getName() {
        return "重载";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"reload"};
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        PlayerPoint.getInstance().load();
        sender.sendMessage("重新读取完成 ");
        return false;
    }

    @Override
    public String getHelp() {
        return "§a/points reload §7重载配置§c(控制台)";
    }
}
