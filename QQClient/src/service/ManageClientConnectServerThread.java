package service;

import java.util.HashMap;

/**
 * @author 王俊彪
 * @version 1.0
 */
public class ManageClientConnectServerThread {
    //我们把多个线程放入到HashMap集合中，key 就是用户的id,value 就是线程
    private static HashMap<String,ClientConnectServerThread> hm = new HashMap<>();

    //将某个线程加入到集合
    public static void addClientConnectServerThread(String userId,ClientConnectServerThread clientConnectServerThread){
        hm.put(userId,clientConnectServerThread);
    }
    //通过userId 得到对应线程
    public static ClientConnectServerThread getClientConnectServerThread(String userId){
        return hm.get(userId);
    }

}
