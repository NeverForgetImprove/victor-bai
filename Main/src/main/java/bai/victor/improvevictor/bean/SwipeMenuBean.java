package bai.victor.improvevictor.bean;

import java.util.Random;

/**
 * Created by Victor-Bai on 2017/7/10.
 */

public class SwipeMenuBean extends BaseBean{

    public String desc;
    public int unreadNum;

    public SwipeMenuBean(){
        Random r = new Random();
        int i = r.nextInt(20);
        desc = "这是第"+i+"条数据";
        unreadNum = i;
    }
}
