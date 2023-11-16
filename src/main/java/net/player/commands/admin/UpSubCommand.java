package net.player.commands.admin;

import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.Config;
import net.player.PlayerPoint;
import net.player.api.Point;
import net.player.commands.SubCommand;

import java.util.Map;
import java.util.UUID;

public class UpSubCommand extends SubCommand {
    public UpSubCommand(PlayerPoint plugin) {
        super(plugin);
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.hasPermission("PayerPoints.up");
    }

    @Override
    public String getName() {
        return "同步";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"up", "转移"};
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length > 1) {
            boolean type = "uuid".equals(args[1]);
            Config config = PlayerPoint.getInstance().getPlayerUUIDConfig();
            Config config1 = PlayerPoint.getInstance().getPlayerNameConfig();
            Map<String, Object> map;
            if (type) {
                map = config.getAll();
                for (String name : map.keySet()) {
                    config1.set(Point.getPlayerNameByUUID(UUID.fromString(name)), map.get(name));
                }
                config1.save();
                sender.sendMessage("转换完成~~");
            } else {
                map = config1.getAll();
                for (String name : map.keySet()) {
                    UUID uuid = Point.getUUIDByPlayerName(name);
                    if (uuid != null) {
                        config.set(uuid.toString(), map.get(name));
                    }
                }
                config1.save();
                sender.sendMessage("转换完成~~");
            }
        }
        return false;
    }

    @Override
    public String getHelp() {
        return "§a/points up <uuid/player> §7将玩家的" + Point.getPointName() + "从 UUID 转移到 玩家名 或 相反";
    }
}
