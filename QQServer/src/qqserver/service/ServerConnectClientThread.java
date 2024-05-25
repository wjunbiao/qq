package qqserver.service;

import qqcommon.Message;
import qqcommon.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author 王俊彪
 * @version 1.0
 * 该类的一个对象和某个客户端保持通信
 */
public class ServerConnectClientThread extends Thread{
    //该线程需要持有socket
    private Socket socket;
    private String userId;//连接到服务端的用户id
    private ArrayList<Message> offOnlineMessageArrayList = new ArrayList<>();
    HashMap<String, ArrayList<Message>> hm = new HashMap<>();

    //从构造器接收一个socket
    public ServerConnectClientThread(Socket socket,String userId) {
        this.socket = socket;
        this.userId = userId;
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public void run() {//这里线程处于run状态，可以发送/接收消息
        while(true){
            System.out.println("服务端和客户端 "+userId+" 保持通信，读取数据....");
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message =(Message) ois.readObject();
                //后面会使用message,根据message 的类型，做出相应的业务处理
                if(message.getMessType().equals(MessageType.MESSAGE_GET_ONLINE_FRIEND)){
                    //客户端要在线用户列表
                    System.out.println(message.getSender()+" 要在线用户列表");
                    String onlineUser = ManageClientThreads.onlineUser();
                    //返回message
                    //构建一个 Message 对象，返回给客户端
                    Message message2 = new Message();
                    message2.setGetter(message.getSender());
                    message2.setContent(onlineUser);
                    message2.setMessType(MessageType.MESSAGE_RET_ONLINE_FRIEND);
                    //得到对应的socket 对象
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(message2);
                }else if(message.getMessType().equals(MessageType.MESSAGE_CLIENT_EXIT)){
                    System.out.println(message.getSender()+ "退出系统 ");
                    //将客户端对应的线程，从集合中移除
                    ManageClientThreads.removeServerConnectClientThread(message.getSender());
                    //关闭连接
                    socket.close();
                    //退出线程
                    break;
                }else if (message.getMessType().equals(MessageType.MESSAGE_COMM_MES)){
                    //私聊消息
//                    OutputStream os = ManageClientThreads.getServerConnectClientThread(message.getGetter()).socket.getOutputStream();
//                    ObjectOutputStream oos = new ObjectOutputStream(os);
//                    oos.writeObject(message);// 提示：后面扩展，如果用户不在线，可以保存到数据库，这样就可以实现离线留言
                    if(ManageClientThreads.isOnline(message.getGetter())){
                        OutputStream os = ManageClientThreads.getServerConnectClientThread(message.getGetter()).socket.getOutputStream();
                        ObjectOutputStream oos = new ObjectOutputStream(os);
                        oos.writeObject(message);// 提示：后面扩展，如果用户不在线，可以保存到数据库，这样就可以实现离线留言
                    }else{
                        boolean flag =false;
                        Iterator<String> iterator = hm.keySet().iterator();
                        while (iterator.hasNext()) {
                            String next = iterator.next();
                            if(next.equals(message.getGetter())){ ;
                                 hm.get(next).add(message);
                                 flag=true;
                             }
                        }
                        if(flag==false){
                            offOnlineMessageArrayList=new ArrayList<>();
                            offOnlineMessageArrayList.add(message);
                        }

                        hm.put(message.getGetter(),offOnlineMessageArrayList);//发送到，消息
                        System.out.println("hm.size="+hm.size());
                        QQServer.setOffLineDb(hm);
                    }



                }else if(message.getMessType().equals(MessageType.MESSAGE_TO_ALL_MES)){
                    //需要遍历，管理线程的集合，把所有线程的socket得到，然后把message转发即可
                    HashMap<String, ServerConnectClientThread> hm = ManageClientThreads.getHm();
                    Iterator<String> iterator = hm.keySet().iterator();
                    while (iterator.hasNext()) {
                        //取出在线用户id
                        String onLineUserId = iterator.next().toString();

                        if(!onLineUserId.equals(message.getSender())){//排除群发消息的这个用户
                            ObjectOutputStream oos = new ObjectOutputStream(
                                    ManageClientThreads.getServerConnectClientThread(onLineUserId).socket.getOutputStream()
                            );
                            oos.writeObject(message);
                        }

                    }
                }else if(message.getMessType().equals(MessageType.MESSAGE_FILE_MES)){
                    //通过getter id 获取对应线程，将message 转发
                    ObjectOutputStream oos =
                            new ObjectOutputStream(ManageClientThreads.getServerConnectClientThread(message.getGetter()).socket.getOutputStream());
                       //写入
                        oos.writeObject(message);
                }

                else{
                    System.out.println("其他类型的消息，暂时处理");
                }


            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
