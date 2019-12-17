package net.player.commands.player;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import net.player.PlayerPoints;
import net.player.commands.SubCommand;
import net.player.window.CreateWindow;

/**
 * @author 若水
 */
public class LeadSubCommand extends SubCommand {

    public LeadSubCommand(PlayerPoints plugin) {
        super(plugin);
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.hasPermission("PayerPoints.lead") && sender.isPlayer();
    }

    @Override
    public String getName() {
        return "排行";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"lead"};
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        CreateWindow.sendLead((Player) sender);
        return true;
    }

    @Override
    public String getHelp() {
        return "§a/points lead§7查看排行榜";
    }
}
