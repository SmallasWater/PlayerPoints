package net.player.window;

import cn.nukkit.Player;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.element.ElementButtonImageData;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.element.ElementLabel;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.form.window.FormWindowSimple;
import net.player.PlayerPoint;
import net.player.api.Point;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author 若水
 */
public class CreateWindow {

    public final static int MENU = 0xAEF0001;

    public final static int Q = 0xAEF0003;

    public final static int PAY = 0xAEF0002;

    public final static int PM = 0xAEF0004;

    public static void sendMenu(Player player) {
        FormWindowSimple simple = new FormWindowSimple(PlayerPoint.getInstance().getLanguage().getString("window.title")
                .replace("%name%", Point.getPointName()) + "-- 主页", "");
        if (PlayerPoint.getInstance().getConfig().getBoolean("是否允许交易", false)) {
            simple.addButton(new ElementButton("支付" + Point.getPointName(), new ElementButtonImageData("path", "textures/ui/village_hero_effect")));
        }
        String me = "兑换" + PlayerPoint.getInstance().getLoad().getName();
        if (PlayerPoint.getInstance().getConfig().getInt("兑换EconomyAPI比例", 0) != 0) {
            simple.addButton(new ElementButton(me, new ElementButtonImageData("path", "textures/ui/Feedback")));
        }
        simple.addButton(new ElementButton("查看【排行榜】", new ElementButtonImageData("path", "textures/ui/store_sort_icon")));
        simple.addButton(new ElementButton("查看【我的" + Point.getPointName() + "】", new ElementButtonImageData("path", "textures/ui/MCoin")));
        if (PlayerPoint.getInstance().getConfig().getInt("rmb与点券兑换比例") != 0) {
            simple.addButton(new ElementButton("查询在MCRMB的余额", new ElementButtonImageData("path", "textures/ui/MCoin")));
            simple.addButton(new ElementButton("兑换MCRMB的余额", new ElementButtonImageData("path", "textures/ui/MCoin")));
        }

        player.showFormWindow(simple, MENU);
    }

    public static void sendLead(Player player) {

        FormWindowSimple simple = new FormWindowSimple(PlayerPoint.getInstance().getLanguage().getString("window.title")
                .replace("%name%", Point.getPointName()) + "-- 排行榜", "");
        StringBuilder builder = new StringBuilder();
        HashMap<String, Number> list = Point.getPlayerRankingList();
        int in = 1;
        int i = 1;
        for (String uuid : list.keySet()) {
            String name = uuid;
            if (PlayerPoint.getInstance().canSaveUUID()) {
                name = Point.getPlayerNameByUUID(UUID.fromString(uuid));
            }
            if (player.getName().equals(name)) {
                in = i;
            }
            if (i <= PlayerPoint.getInstance().getCount()) {
                builder.append("§7No.§a").append(i).append(" §e>>§6").append(name).append("§e: ").append(String.format("%.2f", list.get(uuid).doubleValue())).append("\n");
            }
            i++;
        }
        simple.setContent("§b当前您的排名: §7No.§a" + in + "\n\n-------------------\n" + builder.toString());
        int lead = 0xAEF0003;
        player.showFormWindow(simple, lead);
    }

    public static void sendQ(Player player) {
        FormWindowCustom custom = new FormWindowCustom(PlayerPoint.getInstance().getLanguage().getString("window.title")
                .replace("%name%", Point.getPointName()) + "-- 兑换");
        custom.addElement(new ElementLabel(PlayerPoint.getInstance().getLanguage().getString("window.getmoney"
                , "§d将点券兑换为当前服务器基础经济\n当前经济系统: %system% \n当前兑换比例: 1:%max%").replace("%system%"
                , PlayerPoint.getInstance().getLoad().getName()).replace("%max%", Point.getMax() + "")));
        custom.addElement(new ElementInput("请输入兑换的点券数量", "例如: 1"));
        player.showFormWindow(custom, Q);

    }

    public static void sendPay(Player player) {
        FormWindowCustom custom = new FormWindowCustom(PlayerPoint.getInstance().getLanguage().getString("window.title")
                .replace("%name%", Point.getPointName()) + "-- 支付");
        custom.addElement(new ElementInput("请输入要转账的玩家名", "例如: Steve"));
        custom.addElement(new ElementInput("请输入要转账的数量", "例如: 10"));
        player.showFormWindow(custom, PAY);
    }
}
