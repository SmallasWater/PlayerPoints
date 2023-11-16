package net.player.commands.player;

import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.scheduler.AsyncTask;
import net.player.PlayerPoint;
import net.player.api.CodeException;
import net.player.commands.SubCommand;

/**
 * @author SmallasWater
 * @create 2020/9/22 22:37
 */
public class CheckMoneySubCommand extends SubCommand {
    public CheckMoneySubCommand(PlayerPoint plugin) {
        super(plugin);
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.isPlayer();
    }

    @Override
    public String getName() {
        return "check";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        sender.sendMessage("§2正在查询 请稍后..");
        Server.getInstance().getScheduler().scheduleAsyncTask(PlayerPoint.getInstance(), new AsyncTask() {
            @Override
            public void onRun() {
                try {
                    sender.sendMessage("§a 当前可以兑换 "+(PlayerPoint.getInstance().getMcRmb().checkMoney(sender.getName()) * PlayerPoint.getInstance().getRmb())+" "+PlayerPoint.getInstance().getPointName());
                } catch (CodeException e) {
                    System.out.println(e.getMessage());
                }
            }
        });
        return true;
    }

    @Override
    public String getHelp() {
        return "§a/points check §7查看当前可兑换多少点券 ps: 1 rmb = "+PlayerPoint.getInstance().getRmb()+" "+PlayerPoint.getInstance().getPointName();
    }
}
