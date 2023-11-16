package net.player.api.load;

import cn.nukkit.Player;
import me.onebone.economyapi.EconomyAPI;
import money.Money;
import net.player.PlayerPoint;

/**
 * @author 若水
 */
public class LoadMoney {

    private static final int MONEY = 1;
    private static final int ECONOMY_API = 2;

    private int money = 0;

    public LoadMoney() {
        try {
            Class.forName("me.onebone.economyapi.EconomyAPI");
            money = ECONOMY_API;
            PlayerPoint.getInstance().getLogger().info("检测到 EconomyAPI 对接 EconomyAPI 经济系统");
        } catch (ClassNotFoundException e) {
            try {
                Class.forName("money.Money");
                money = MONEY;
                PlayerPoint.getInstance().getLogger().info("检测到 Money 对接 Money 经济系统");
            } catch (ClassNotFoundException ex) {
                PlayerPoint.getInstance().getLogger().warning("不存在 EconomyAPI 或 Money");
            }
        }
    }

    public String getName() {
        if (this.money == ECONOMY_API) {
            return PlayerPoint.getInstance().getLanguage().getString("economyapi", "金币");
        } else if (money != 0) {
            return PlayerPoint.getInstance().getLanguage().getString("money", "金条");
        } else {
            return "无经济系统";
        }
    }

    public void addMoney(Player player, double money) {
        addMoney(player.getName(), money);
    }

    private void addMoney(String player, double money) {
        switch (this.money) {
            case MONEY:
                if (Money.getInstance().getPlayers().contains(player)) {
                    Money.getInstance().addMoney(player, (float) money);
                    return;
                }
                break;
            case ECONOMY_API:
                EconomyAPI.getInstance().addMoney(player, money, true);
                return;

            default:
                break;
        }
    }


}
