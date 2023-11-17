package net.player.commands.admin;

import cn.nukkit.command.CommandSender;
import net.player.PlayerPoint;
import net.player.api.CodeException;
import net.player.commands.SubCommand;

/**
 * @author SmallasWater
 * @create 2020/9/23 10:30
 */
public class SendPayMessageSubCommand extends SubCommand {
    public SendPayMessageSubCommand(PlayerPoint plugin) {
        super(plugin);
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.isOp();
    }

    @Override
    public String getName() {
        return "pmsg";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length > 1) {
            String playerName = args[1];
            try {
                PlayerPoint.getInstance().getMcRmb().sendPayMessage(playerName);
            } catch (CodeException e) {
                sender.sendMessage(e.getMessage());
            }
        } else {
            sender.sendMessage("§c请输入玩家名称");
        }
        return true;
    }

    @Override
    public String getHelp() {
        return "§a/points pmsg <玩家名> §7查看玩家最近的交易信息";
    }
}
