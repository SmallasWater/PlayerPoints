package net.player.api.load;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.scheduler.AsyncTask;
import cn.nukkit.utils.ConfigSection;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.player.PlayerPoint;
import net.player.api.CodeException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author SmallasWater
 * @create 2020/9/22 21:31
 */
public class LoadMcRmb {

    private final int sid;

    private final String key;

    public LoadMcRmb(int sid,String key){
        this.sid = sid;
        this.key = key;
    }

    private static final String URL = "http://api.mcrmb.com/Api/{api}?{value}";


    /**
     * 消费一定金额
     * */
    public boolean toPay(String playerName,int money) throws CodeException {
        Player player = Server.getInstance().getPlayer(playerName);
        ConfigSection section = null;
        try {
            section = send("Pay",md5Value(playerName,
                        new GetValue("use", URLEncoder.encode("兑换PlayerPoint点券","UTF-8")),
                        new GetValue("money",money+""),
                        new GetValue("time",String.valueOf(System.currentTimeMillis() / 1000L))));
        } catch (UnsupportedEncodingException e) {
            return false;
        }
        if(section.size() == 0){
            return false;
        }

        Map map = section.getMapList("data").get(0);
        if(Integer.parseInt(section.getString("code")) == 101){
            if(player != null){
                player.sendMessage("§a"+section.getString("msg")+" 剩余: "+map.get("money").toString());
            }
            return true;
        }
        else if(Integer.parseInt(section.getString("code")) == 102){
            if(player != null){
                player.sendMessage("§c"+section.getString("msg")+
                        " 还差 "+map.get("need").toString()+
                        " 当前: "+map.get("money").toString());
            }
        }else{
            throw new CodeException(Integer.parseInt(section.getString("code")),section.getString("msg"));
        }
        return false;
    }
    /**
     * 获取玩家充值的rmb
     * */
    public int checkMoney(String playerName) throws CodeException {
        ConfigSection section = send("CheckMoney",md5Value(playerName));
        if(Integer.parseInt(section.getString("code")) == 101){
            Map map = section.getMapList("data").get(0);
            return Integer.parseInt(map.get("money").toString());
        }else if(Integer.parseInt(section.getString("code")) == 102) {
            return 0;
        }else{
            throw new CodeException(Integer.parseInt(section.getString("code")),section.getString("msg"));
        }
    }

    /**
     * 玩家打印交易信息
     * */
    public void sendPayMessage(String playerName) throws CodeException {
        ConfigSection section = send("CheckRecord",md5Value(playerName));

        if(Integer.parseInt(section.getString("code")) == 101){
            System.out.println("------- "+playerName+" 的交易记录 -------");
            System.out.println("状态: "+section.getString("msg"));
            String sd;
            for(Map<?,?> map : section.getMapList("data")){
                System.out.println("时间: "+toTime(map.get("date").toString()));
                System.out.println("交易信息: "+map.get("text").toString());
                System.out.println("交易金额: "+map.get("money").toString());
                System.out.println("--------------");
            }
        } else if(Integer.parseInt(section.getString("code")) == 102){
            System.out.println("------- "+playerName+" 的交易记录 -------");
            System.out.println("状态: "+section.getString("msg"));
            System.out.println("--------------");
        }else{
            throw new CodeException(Integer.parseInt(section.getString("code")),section.getString("msg"));
        }
    }

    private String toTime(String time){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(time);
        Date date = new Date(lt * 1000L);
        return simpleDateFormat.format(date);
    }




    private String md5Value(String wname,GetValue... value)  {
        StringBuilder v = new StringBuilder();
        StringBuilder v1 = new StringBuilder();
        for(GetValue va: value){
            v.append(va.value);
            v1.append("&").append(va.name).append("=").append(va.value);
        }
        return "sign="+getMd5(sid+ wname+v.toString()+key)+"&sid="+sid+"&wname="+wname+v1.toString();

    }



    /**使用md5加密字符串*/
    public static String getMd5(String plainText) {

        // 返回字符串
        String md5Str = null;
        try {
            StringBuilder buf = new StringBuilder();
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte[] b = md.digest();
            int i;
            for (byte value : b) {
                i = value;
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }
            md5Str = buf.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return md5Str;
    }



    private static ConfigSection send(String api,String value){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return new ConfigSection(gson.fromJson(loadJson(api,value), (new TypeToken<LinkedHashMap<String, Object>>() {
        }).getType()));
    }

    private static String loadJson (String api,String value) {
        StringBuilder json = new StringBuilder();
        try {
            URL urlObject = new URL(URL.replace("{api}",api).replace("{value}",value));
            HttpURLConnection uc = (HttpURLConnection) urlObject.openConnection();
            uc.addRequestProperty(".USER_AGENT","Mozilla/5.0 (X11; U; Linux i686; zh-CN; rv:1.9.1.2) Gecko/20090803 java");
            uc.setRequestMethod("GET");
            uc.setConnectTimeout(15000);
            uc.setReadTimeout(15000);
            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream(), StandardCharsets.UTF_8));
            String inputLine;
            while ( (inputLine = in.readLine()) != null) {
                json.append(inputLine);
            }
            in.close();
        } catch (Exception e) {
            return null;
        }
        return json.toString();
    }

    private static class GetValue {
        private final String name;

        private final String value;

        public GetValue(String name,String value){
            this.name = name;
            this.value = value;
        }
    }


}
