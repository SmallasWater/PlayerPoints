package net.player.window;

import cn.nukkit.Player;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.element.ElementButtonImageData;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.form.window.FormWindowSimple;
import net.player.PlayerPoints;
import net.player.api.Point;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author 若水
 */
public class CreateWindow {

    public final static int MENU = 0xAEF0001;

    public final static int PAY = 0xAEF0002;

    public static void sendMenu(Player player){
        FormWindowSimple simple = new FormWindowSimple(PlayerPoints.getInstance().getLanguage().getString("window.title")
                .replace("%name%",Point.getPointName())+"-- 主页","");
        simple.addButton(new ElementButton("支付"+Point.getPointName(),new ElementButtonImageData("path","textures/ui/village_hero_effect")));
        String me = "炫耀"+Point.getPointName();
        if(PlayerPoints.getInstance().timer.containsKey(player.getName())){
            me += "  §c(冷却中)";
        }else{
            me += "  §a(冷却时间 "+PlayerPoints.getInstance().getTime()+" 秒)";
        }
        simple.addButton(new ElementButton(me,new ElementButtonImageData("path","textures/ui/Feedback")));
        simple.addButton(new ElementButton("查看【排行榜】",new ElementButtonImageData("path","textures/ui/store_sort_icon")));
        simple.addButton(new ElementButton("查看【我的"+Point.getPointName()+"】",new ElementButtonImageData("path","textures/ui/MCoin")));
        player.showFormWindow(simple,MENU);
    }

    public static void sendLead(Player player){
        FormWindowSimple simple = new FormWindowSimple(PlayerPoints.getInstance().getLanguage().getString("window.title")
                .replace("%name%",Point.getPointName())+"-- 排行榜","");
        StringBuilder builder = new StringBuilder();
        HashMap<String,Double> list = Point.getPlayerRankingList();
        int in = 1;
        int i = 1;
        for(String uuid: list.keySet()){
            String name = Point.getPlayerNameByUUID(UUID.fromString(uuid));
            if (player.getName().equals(name)) {
                in = i;
            }
            i++;
            if(i <= PlayerPoints.getInstance().getCount()){
                builder.append("§7No.§a").append(i).append(" §e>>§6").append(name).append("§e: ").append(String.format("%.2f", list.get(uuid))).append("\n");
            }
        }
        simple.setContent("§b当前您的排名: §7No.§a"+in+"\n\n-------------------\n"+builder.toString());
        int lead = 0xAEF0003;
        player.showFormWindow(simple, lead);
    }

    public static void sendPay(Player player){
        FormWindowCustom custom = new FormWindowCustom(PlayerPoints.getInstance().getLanguage().getString("window.title")
                .replace("%name%",Point.getPointName())+"-- 支付");
        custom.addElement(new ElementInput("请输入要转账的玩家名","例如: Steve"));
        custom.addElement(new ElementInput("请输入要转账的数量","例如: 10"));
        player.showFormWindow(custom,PAY);
    }
}
