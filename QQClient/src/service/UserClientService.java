package service;

import qqcommon.Message;
import qqcommon.MessageType;
import qqcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author 王俊彪
 * @version 1.0
 * 该类完成用户登录验证和用户注册等等功能
 */

public class UserClientService {

    //因为我们可能在其他地方会使用user 信息，因此作为成员属性
    private User u = new User();
    //因为socket在其他地方也可能会使用，因此作出属性
    private Socket socket ;


    //根据 userid 和pwd 到服务器验证该用户是否合法
    public boolean checkUser(String userId,String pwd){
        //创建userId 对象
        u.setUserId(userId);
        u.setPassword(pwd);
        boolean b =false;
        try {
            //连接服务器，发送u对象
            socket = new Socket(InetAddress.getByName("127.0.0.1"),9999);
            //得到objectOutputStream 对象
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(u);//发送User对象
            //读取服务器回复的Message 对象
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Message ms = (Message) ois.readObject();
            if(ms.getMessType().equals(MessageType.MESSAGE_LOGIN_SUCCEED)){//登录成功

                ms = (Message) ois.readObject();
                    if(ms.getMessType()!=null && ms.getMessType().equals(MessageType.MESSAGE_offOnLine_MES)){
                        for (int i = 0; i <ms.getMessageArrayList().size() ; i++) {
                            System.out.println(ms.getSender()+" 对 " +ms.getGetter() +" 说："+ms.getMessageArrayList().get(i).getContent());
                        }
                        for (int i = 0; i <ms.getMessageArrayList().size() ; i++) {
                            ms.getMessageArrayList().remove(i);
                        }
                }
                b=true;
                //创建一个和服务器保持通讯的线程-> 创建一个类ClientConnectServerThread
                ClientConnectServerThread clientConnectServerThread = new ClientConnectServerThread(socket);
                //启动客户端线程
                clientConnectServerThread.start();
                //这里为了后面客户端扩展，我们将线程放入到集合管理
                ManageClientConnectServerThread.addClientConnectServerThread(userId,clientConnectServerThread);


            }else{
                //如果登录失败，我们就不能启动和服务器相关的线程
                socket.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }

    //向服务端请求用户列表
    public void onlineFriendList(){
        //发送一个Message , 类型MESSAGE_GET_ONLINE_FRIEND
        Message message = new Message();
        message.setSender(u.getUserId());
        message.setMessType(MessageType.MESSAGE_GET_ONLINE_FRIEND);
        //发送给服务器

        try {
            //应该得到当前线程的socket 对应的ObjectOutputStream对象
            ObjectOutputStream oos =
                    new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(
                            u.getUserId()).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //编写方法，发送私聊消息
    public void send(String getter,String count){
        Message message = new Message();
        message.setSender(u.getUserId());
        message.setGetter(getter);
        message.setMessType(MessageType.MESSAGE_COMM_MES);
        message.setContent(count);
        try {
            ObjectOutputStream oos =
                    new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(
                            u.getUserId()).getSocket().getOutputStream());
            oos.writeObject(message);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //编写方法，退出客户端，并给服务器发送一个退出系统的message 对象
    public void logout(){
        Message message = new Message();
        message.setSender(u.getUserId());
        message.setMessType(MessageType.MESSAGE_CLIENT_EXIT);
        try {
            ObjectOutputStream oos =
                    new ObjectOutputStream(ManageClientConnectServerThread.
                            getClientConnectServerThread(u.getUserId()).getSocket().getOutputStream());
            oos.writeObject(message);
            System.out.println(u.getUserId()+" 退出了系统");
            System.exit(0);//结束进程
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
