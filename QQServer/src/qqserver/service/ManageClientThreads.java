package qqserver.service;

import java.util.HashMap;
import java.util.Iterator;

/**
 * @author 王俊彪
 * @version 1.0
 * 该类用于管理和客户端通信的线程
 */
public class ManageClientThreads {

    private static  HashMap<String,ServerConnectClientThread> hm = new HashMap<>();

    public static HashMap<String, ServerConnectClientThread> getHm() {
        return hm;
    }

    //添加线程对象到hm集合
    public static void addClientThread(String userId,ServerConnectClientThread serverConnectClientThread){
        hm.put(userId,serverConnectClientThread);
    }
    //根据userId 返回ServerConnectClientThread对象
    public static ServerConnectClientThread getServerConnectClientThread(String userId){
        return  hm.get(userId);
    }

    //增加一个方法，从集合中，移除某个线程对象
    public static void removeServerConnectClientThread(String userId){
        hm.remove(userId);
    }

    //编写方法，返回在线用户列表
    public static String onlineUser(){
        //集合遍历 ，遍历HashMap的key
        Iterator<String> iterator = hm.keySet().iterator();
        String onlineUserList="";
        while (iterator.hasNext()){
            onlineUserList+=iterator.next().toString()+" ";
        }
        return  onlineUserList;
    }
    //编写方法，返回用户是否在线
    public static boolean isOnline(String getterId){
        //集合遍历 ，遍历HashMap的key
        Iterator<String> iterator = hm.keySet().iterator();
        while (iterator.hasNext()){
            if(iterator.next().equals(getterId)){
                return true;
            }
        }
        System.out.println("该用户不在线");
        return false;
    }
}
