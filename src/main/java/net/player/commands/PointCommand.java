package net.player.commands;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;
import cn.nukkit.utils.TextFormat;
import net.player.PlayerPoint;
import net.player.api.Point;
import net.player.commands.admin.*;
import net.player.commands.player.*;
import net.player.window.CreateWindow;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 若水
 */
public class PointCommand extends PluginCommand<PlayerPoint> {

    private final List<SubCommand> commands = new ArrayList<>();
    private final ConcurrentHashMap<String, Integer> SubCommand = new ConcurrentHashMap<>();

    public PointCommand(PlayerPoint owner) {
        super("points", owner);
        this.setAliases(new String[]{"point"});
        this.setDescription("points主命令");
        this.loadSubCommand(new GiveSubCommand(getPlugin()));
        this.loadSubCommand(new ReloadSubCommand(getPlugin()));
        this.loadSubCommand(new SetSubCommand(getPlugin()));
        this.loadSubCommand(new TakeSubCommand(getPlugin()));
        this.loadSubCommand(new LeadSubCommand(getPlugin()));
        this.loadSubCommand(new LookSubCommand(getPlugin()));
        this.loadSubCommand(new MeSubCommand(getPlugin()));
        if (PlayerPoint.getInstance().getConfig().getBoolean("是否允许交易", false)) {
            this.loadSubCommand(new PaySubCommand(getPlugin()));
        }
        this.loadSubCommand(new ResetSubCommand(getPlugin()));
        this.loadSubCommand(new UpSubCommand(getPlugin()));
        if (PlayerPoint.getInstance().getConfig().getInt("rmb与点券兑换比例") != 0) {
            this.loadSubCommand(new CheckMoneySubCommand(getPlugin()));
            this.loadSubCommand(new PayMoneySubCommand(getPlugin()));
            this.loadSubCommand(new SendPayMessageSubCommand(getPlugin()));
        }


    }

    private void loadSubCommand(SubCommand cmd) {
        commands.add(cmd);
        int commandId = (commands.size()) - 1;
        SubCommand.put(cmd.getName().toLowerCase(), commandId);
        for (String alias : cmd.getAliases()) {
            SubCommand.put(alias.toLowerCase(), commandId);
        }
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        if (!sender.hasPermission("PayerPoints.points")) {
            sender.sendMessage(TextFormat.RED + "抱歉，，您没有使用此指令权限");
            return true;
        }
        if (args.length == 0) {
            if (sender instanceof Player) {
                CreateWindow.sendMenu((Player) sender);
            } else {
                sender.sendMessage("控制台无法显示GUI");
            }
            return true;
        }
        String subCommand = args[0].toLowerCase();
        if (SubCommand.containsKey(subCommand)) {
            SubCommand command = commands.get(SubCommand.get(subCommand));
            boolean canUse = command.canUse(sender);
            if (canUse) {
                return command.execute(sender, args);
            } else {
                return false;
            }
        } else {
            return this.sendHelp(sender, args);
        }
    }

    private boolean sendHelp(CommandSender sender, String[] args) {
        if ("help".equals(args[0])) {
            sender.sendMessage("§a§l >> §eHelp for PlayerPoints§a<<");
            sender.sendMessage(getHelp());
            for (net.player.commands.SubCommand subCommand : commands) {
                if (subCommand.canUse(sender)) {
                    sender.sendMessage(subCommand.getHelp());
                }
            }
            sender.sendMessage("§a§l >> §eHelp for PlayerPoints §a<<");
        }
        return true;
    }

    public String getHelp() {
        return "§a/points §7打开" + Point.getPointName() + "GUI";
    }

}
