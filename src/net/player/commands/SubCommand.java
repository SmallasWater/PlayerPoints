package net.player.commands;


import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import net.player.PlayerPoints;


/**
 * @author 若水
 */
public abstract class SubCommand {

    private final PlayerPoints plugin;

    protected SubCommand(PlayerPoints plugin) {
        if (plugin == null) {
            Server.getInstance().getLogger().error("AreaMainClass is null");
        }
        this.plugin = plugin;
    }

    /**
     * @return AreaMainClass
     */
    protected PlayerPoints getPlugin() {
        return plugin;
    }


    /**
     * 指令使用权限
     * @param sender CommandSender
     * @return boolean
     */
    public abstract boolean canUse(CommandSender sender);


    /**
     * 指令名称
     * @return string
     */
    public abstract String getName();
    /**
     * 指令别名
     * @return string[]
     */
    public abstract String[] getAliases();

    /**
     * 执行
     * @param sender the sender      - CommandSender
     * @param args   The arrugements      - String[]
     * @return true if true
     */

    public abstract boolean execute(CommandSender sender, String[] args);

    /**
     * 指令帮助信息
     * @return 帮助
     * */
    public abstract String getHelp();
}
