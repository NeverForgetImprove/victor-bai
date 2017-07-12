package bai.victor.improvevictor.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor-Bai on 2017/7/11.
 */

public class DataFactory {

    public static <E> List<E> createListData(Class<E> c){
        List<E> data = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            try {
                E e = c.newInstance();
                data.add(e);
            }catch (Exception e){

            }
        }
        return data;
    }
}
