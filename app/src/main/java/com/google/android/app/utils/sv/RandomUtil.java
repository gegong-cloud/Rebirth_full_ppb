package com.google.android.app.utils.sv;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 　　　　  　　　┏┓　　      ┏┓+ +
 * 　　　　　　　┏┛┻━━━━━━━━━┛┻┓ + +
 * 　　　　　　　┃　　　　　　　 ┃
 * 　　　　　　　┃　　  ━　　   ┃ ++ + + +
 * 　　　　　　　┃  	██ ━██   ┃+
 * 　　　　　　　┃　　　　　　　 ┃ +
 * 　　　　　　　┃　　　 ┻ 　　 ┃
 * 　　　　　　　┃　　　　　　 　┃ + +
 * 　　　　　　　┗━┓　　　    ┏━┛
 * 　　　　　　　　　┃　　 　 ┃
 * 　　　　　　　　　┃　　 　 ┃ + + + +
 * 　　　　　　　　　┃　　 　 ┃　　　　Code is far away from bug with the animal protecting
 * 　　　　　　　　　┃　　　  ┃ + 　　　　神兽保佑,代码无bug
 * 　　　　　　　　　┃　　　  ┃
 * 　　　　　　　　　┃　　　  ┃　　+
 * 　　　　　　　　　┃　 　 　┗━━━┓ + +
 * 　　　　　　　　　┃ 　　　　　　　┣┓
 * 　　　　　　　　　┃ 　　　　　　　┏┛
 * 　　　　　　　　　┗┓┓┏━┳┓┏┛ + + + +
 * 　　　　　　　　　　┃┫┫　┃┫┫
 * 　　　　　　　　　　┗┻┛　┗┻┛+ + + +
 *随机工具
 */
public class RandomUtil {

    static String[] radomList = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z",
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",};


    /**
     * 创建指定个数，指定最小值，最大值的随机数
     *
     * @param count  随机数个数
     * @param minVal 随机数最小值
     * @param maxVal 随机数最大值
     * @return
     *
     */
    public static List<Integer> createRandomList(int count, Integer minVal, Integer maxVal) {
        List<Integer> integers = new ArrayList<Integer>();
        for (int i = 0; i < count; i++) {
            integers.add(createNewRandomKey(integers, minVal, maxVal));
        }
        return integers;
    }


    /**
     * 创建一个不重复的随机数
     *
     * @param keys
     * @param minVal
     * @param maxVal
     * @return
     * @author BY ztd
     */
    private static Integer createNewRandomKey(List<Integer> keys, Integer minVal, Integer maxVal) {
        Integer v = createRandomKey(minVal, maxVal);
        while (keys.contains(v)) {
            v = createRandomKey(minVal, maxVal);
        }
        return v;
    }

    /**
     * 创建一个在范围内的随机数
     *
     * @param minVal
     * @param maxVal
     * @return
     */
    private static Integer createRandomKey(Integer minVal, Integer maxVal) {
        Integer v = new Random().nextInt(maxVal);
        if (v <= minVal) {
            v = v + minVal;
        }
        return v;
    }

}
