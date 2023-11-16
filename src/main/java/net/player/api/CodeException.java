package net.player.api;

/**
 * @author SmallasWater
 * @create 2020/9/22 22:30
 */
public class CodeException extends Exception{

    private final int code;

    private final String text;
    public CodeException(int code,String text){
        this.code = code;
        this.text = text;
    }

    @Override
    public String getMessage() {
        return "code: "+code+" "+text;
    }
}
