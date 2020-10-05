package net.player.commands.player;

import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.scheduler.AsyncTask;
import net.player.PlayerPoint;
import net.player.api.CodeException;
import net.player.api.Point;
import net.player.api.load.LoadMcRmb;
import net.player.commands.SubCommand;

/**
 * @author SmallasWater
 * @create 2020/9/22 22:46
 */
public class PayMoneySubCommand extends SubCommand {
    public PayMoneySubCommand(PlayerPoint plugin) {
        super(plugin);
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.isPlayer();
    }

    @Override
    public String getName() {
        return "pm";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(args.length > 1){
            try {
                int money = Integer.parseInt(args[1]);
                if(money < 0){
                    sender.sendMessage("§c请输入大于0的数值!");
                    return false;
                }else{
                    LoadMcRmb mcRmb = PlayerPoint.getInstance().getMcRmb();
                    sender.sendMessage("§2正在兑换点券 请稍后...");
                    Server.getInstance().getScheduler().scheduleAsyncTask(PlayerPoint.getInstance(), new AsyncTask() {
                        @Override
                        public void onRun() {
                            try {
                                if(mcRmb.toPay(sender.getName(),money)){
                                    Point.addPoint(sender.getName(),money * PlayerPoint.getInstance().getRmb());
                                }else{
                                    sender.sendMessage("§c兑换点券失败");
                                }
                            }catch (CodeException e){
                                sender.sendMessage("§c出现未知错误 请联系管理员解决问题");
                                System.out.println(e.getMessage());
                            }
                        }
                    });

                }
            }catch (Exception e){
                sender.sendMessage("§c 请输入正确的数值!");
                return false;
            }

        }

        return false;
    }

    @Override
    public String getHelp() {
        return "§a/points pm <rmb数量> §7兑换rmb为点券 ps: 1 rmb = "+PlayerPoint.getInstance().getRmb()+" "+PlayerPoint.getInstance().getPointName();
    }
}
