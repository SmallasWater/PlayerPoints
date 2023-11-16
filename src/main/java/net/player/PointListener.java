package net.player;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.network.protocol.ModalFormResponsePacket;
import cn.nukkit.scheduler.AsyncTask;
import cn.nukkit.utils.TextFormat;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.player.api.CodeException;
import net.player.api.Point;
import net.player.api.events.PlayerAddPointEvent;
import net.player.api.events.PlayerReducePointEvent;
import net.player.api.events.PlayerSetPointEvent;
import net.player.api.load.LoadMcRmb;
import net.player.window.CreateWindow;



/**
 * @author ZXR
 */
public class PointListener implements Listener {
    private static final String POINT = "%point%";
    private static final String POINT_NAME = "%name%";




    @EventHandler
    public void onAddPoint(PlayerAddPointEvent event){
        if(event.isCancelled()){
            return;
        }
        Object uuid = event.getPlayer();
        Player player = Point.getPlayer(uuid);
        double old = Point.getPoint(uuid);
        if(old + event.getPoint() > PlayerPoint.getInstance().getMaxPoint()){
            event.setCancelled();
            if(player != null){
                player.sendMessage(PlayerPoint.getInstance().getLanguage().getString("player.point.add.max"));
            }
            return;
        }
        if(player != null){
            player.sendMessage(PlayerPoint.getInstance().getLanguage().getString("player.point.add")
                    .replace(POINT,String.format("%.2f",event.getPoint())).replace(POINT_NAME,
                            PlayerPoint.getInstance().getPointName()));

        }
        Point.setPoint(uuid,old + event.getPoint());
    }


    private static final String NULL = "null";
    @EventHandler
    public void getUI(DataPacketReceiveEvent event){
        String data;
        ModalFormResponsePacket ui;
        Player player = event.getPlayer();
        if((event.getPacket() instanceof ModalFormResponsePacket)) {
            ui = (ModalFormResponsePacket) event.getPacket();
            data = ui.data.trim();
            int fromId = ui.formId;
            switch(fromId) {
                case CreateWindow.MENU:
                    if(NULL.equals(data)){
                        return;
                    }
                    if(!PlayerPoint.getInstance().getConfig().getBoolean("是否允许交易",false)){
                        data = (Integer.parseInt(data)+1)+ "";
                    }else{
                        if(Integer.parseInt(data) == 0){
                            CreateWindow.sendPay(player);
                            return;
                        }
                    }

                    if(PlayerPoint.getInstance().getConfig().getInt("兑换EconomyAPI比例",0) != 0) {
                        if(Integer.parseInt(data) == 1){
                            CreateWindow.sendQ(player);
                            return;
                        }
                        if(Integer.parseInt(data) == 2){
                            CreateWindow.sendLead(player);
                            return;

                        }
                        if(Integer.parseInt(data) == 3){
                            player.sendMessage(PlayerPoint.getInstance().getLanguage().getString("player.point.me")
                                    .replace("%point%", Point.myPoint(player)+"")
                                    .replace("%name%", Point.getPointName()));

                        }
                    }else{
                        if(Integer.parseInt(data) == 1){
                            CreateWindow.sendLead(player);
                            return;

                        }
                        if(Integer.parseInt(data) == 2){
                            player.sendMessage(PlayerPoint.getInstance().getLanguage().getString("player.point.me")
                                    .replace("%point%", Point.myPoint(player)+"")
                                    .replace("%name%", Point.getPointName()));

                        }
                    }
                    if(PlayerPoint.getInstance().getConfig().getInt("rmb与点券兑换比例") != 0){
                        if(PlayerPoint.getInstance().getConfig().getInt("兑换EconomyAPI比例",0) != 0) {
                            if(Integer.parseInt(data) == 4){
                                player.sendMessage("§2正在查询 请稍后..");
                                Server.getInstance().getScheduler().scheduleAsyncTask(PlayerPoint.getInstance(), new AsyncTask() {
                                    @Override
                                    public void onRun() {
                                        try {
                                            player.sendMessage("§a 当前可以兑换 "+(PlayerPoint.getInstance().getMcRmb().checkMoney(player.getName()) * PlayerPoint.getInstance().getRmb())+" "+PlayerPoint.getInstance().getPointName());
                                        } catch (CodeException e) {
                                            System.out.println(e.getMessage());
                                        }
                                    }
                                });
                                player.sendMessage("§e查询结束");
                            }
                            if(Integer.parseInt(data) == 5){
                                FormWindowCustom simple = new FormWindowCustom(PlayerPoint.getInstance().getLanguage().getString("window.title")
                                        .replace("%name%", Point.getPointName())+"-- 兑换");

                                simple.addElement(new ElementInput(TextFormat.colorize('&',"&e请输入你要兑换的数量 ps: &a充值的金额")));
                                player.showFormWindow(simple,CreateWindow.PM);

                            }
                        }else{
                            if(Integer.parseInt(data) == 3){
                                player.sendMessage("§2正在查询 请稍后..");
                                Server.getInstance().getScheduler().scheduleAsyncTask(PlayerPoint.getInstance(), new AsyncTask() {
                                    @Override
                                    public void onRun() {
                                        try {
                                            player.sendMessage("§a 当前可以兑换 "+(PlayerPoint.getInstance().getMcRmb().checkMoney(player.getName()) * PlayerPoint.getInstance().getRmb())+" "+PlayerPoint.getInstance().getPointName());
                                        } catch (CodeException e) {
                                            System.out.println(e.getMessage());
                                        }
                                    }
                                });
                                player.sendMessage("§e查询结束");
                            }
                            if(Integer.parseInt(data) == 4){
                                FormWindowCustom simple = new FormWindowCustom(PlayerPoint.getInstance().getLanguage().getString("window.title")
                                        .replace("%name%", Point.getPointName())+"-- 兑换");

                                simple.addElement(new ElementInput(TextFormat.colorize('&',"&e请输入你要兑换的数量 ps: &a充值的金额")));
                                player.showFormWindow(simple,CreateWindow.PM);
                            }
                        }
                    }

                    break;
                case CreateWindow.PAY:
                    if(NULL.equals(data)){
                        return;
                    }
                    if(!PlayerPoint.getInstance().getConfig().getBoolean("是否允许交易",false)){
                        player.sendMessage(TextFormat.RED+"服务器禁止点券交易");
                        return;
                    }
                    Object[] datas = decodeData(data);
                    if(datas == null || datas.length < 1){
                        return;
                    }
                    String target = (String) datas[0];
                    String p = (String) datas[1];
                    if(Point.isRightNumberPoint(p)){
                        Player p1 = Server.getInstance().getPlayer(target);
                        if(p1 != null){
                            target = p1.getName();
                        }else{
                            player.sendMessage(TextFormat.RED+"玩家不在线");
                            return;
                        }
                        if(target.equalsIgnoreCase(player.getName())){
                            player.sendMessage(TextFormat.RED+"不能转账给自己");
                            return;
                        }
                        int i = Point.playerPayTargetPoint(player.getUniqueId(), Point.getUUIDByPlayerName(target),Double.parseDouble(p));
                        if(i == 0){
                            player.sendMessage(PlayerPoint.getInstance().getLanguage().getString("player.point.not.enough")
                                    .replace("%name%", Point.getPointName())
                                    .replace("%point%",String.format("%.2f", Point.myPoint(player))));
                        }else if(i == -1){
                            player.sendMessage("§c未找到玩家 "+target+" 相关数据");
                        }else{
                            player.sendMessage(PlayerPoint.getInstance().getLanguage().getString("player.point.pay")
                                    .replace("%target%",target)
                                    .replace("%point%",Double.parseDouble(p)+"")
                                    .replace("%name%", Point.getPointName()));
                        }
                    }else{
                        player.sendMessage("§c请输入正确的数值 ");
                    }
                    break;
                case CreateWindow.Q:
                    if(NULL.equals(data)){
                        return;
                    }datas = decodeData(data);
                    if(datas == null || datas.length < 1){
                        return;
                    }
                    String count = datas[1].toString();
                    try{
                        int c = Integer.parseInt(count);
                        if(c > 0){
                            if(Point.reducePoint(player,c)){
                                PlayerPoint.getInstance().getLoad().addMoney(player,(c * Point.getMax()));
                                player.sendMessage("§e您成功兑换"+(c * Point.getMax())+ PlayerPoint.getInstance().getLoad().getName());
                            }else{
                                player.sendMessage(PlayerPoint.getInstance().getLanguage().getString("player.point.not.enough")
                                        .replace("%name%", Point.getPointName())
                                        .replace("%point%",String.format("%.2f", Point.myPoint(player))));
                            }
                        }else{
                            player.sendMessage("§c请输入合法的数值 ");
                        }
                    }catch (Exception e){
                        player.sendMessage("§c请输入合法的数值 ");
                    }
                    break;

                    case CreateWindow.PM:
                        if(NULL.equals(data)){
                            return;
                        }datas = decodeData(data);
                        if(datas == null || datas.length < 1){
                            return;
                        }
                        count = datas[0].toString();
                        try {
                            int money = Integer.parseInt(count);
                            if (money > 0) {
                                LoadMcRmb mcRmb = PlayerPoint.getInstance().getMcRmb();
                                player.sendMessage("§2正在兑换点券 请稍后...");
                                Server.getInstance().getScheduler().scheduleAsyncTask(PlayerPoint.getInstance(), new AsyncTask() {
                                    @Override
                                    public void onRun() {
                                        try {
                                            if(mcRmb.toPay(player.getName(),money)){
                                                Point.addPoint(player.getName(),money * PlayerPoint.getInstance().getRmb());
                                            }else{
                                                player.sendMessage("§c兑换点券失败");
                                            }
                                        }catch (CodeException e){
                                            player.sendMessage("§c出现未知错误 请联系管理员解决问题");
                                            System.out.println(e.getMessage());
                                        }
                                    }
                                });
                                player.sendMessage("§b兑换结束");


                            }else{
                                player.sendMessage("§c请输入合法的数值 ");
                            }
                        }catch (Exception e){
                            player.sendMessage("§c请输入合法的数值 ");
                        }

                        break;

                    default:break;


            }
        }
    }
    private static Object[] decodeData(String data){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.fromJson(data,(new TypeToken<Object[]>() {
        }) .getType());
    }

    @EventHandler
    public void onReducePoint(PlayerReducePointEvent event){
        if(event.isCancelled()){
            return;
        }
        Object uuid = event.getPlayer();
        Player player = Point.getPlayer(uuid);
        double old = Point.getPoint(uuid);
        if(player != null){
            player.sendMessage(PlayerPoint.getInstance().getLanguage().getString("player.point.remove")
                    .replace(POINT,String.format("%.2f",event.getPoint())).replace(POINT_NAME,
                            PlayerPoint.getInstance().getPointName()));


        }
        Point.setPoint(uuid,old - event.getPoint());
    }

    @EventHandler
    public void onSetPoint(PlayerSetPointEvent event){
        if(event.isCancelled()){
            return;
        }
        Object uuid = event.getPlayer();
        Player player = Point.getPlayer(uuid);
        if(player != null){
            player.sendMessage(PlayerPoint.getInstance().getLanguage().getString("player.point.set")
                    .replace(POINT,String.format("%.2f",event.getPoint())).replace(POINT_NAME,
                            PlayerPoint.getInstance().getPointName()));



        }
        Point.setPoint(uuid,event.getPoint());
    }

}
