package yuan.cam.b.util;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommonUtil {

    public static Integer currentSeconds(){
        return (int) System.currentTimeMillis() / 1000;
    }

}