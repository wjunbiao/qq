package service;

import qqcommon.Message;
import qqcommon.MessageType;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * @author 王俊彪
 * @version 1.0
 */
public class ClientConnectServerThread  extends Thread{
    //该线程需要持有socket
    private Socket socket;

    public ClientConnectServerThread(Socket socket) {
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

    //构造器可以接收一个socket
    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        //因为Thread 需要在后台和服务器通信，因此我们用while 循环
        while (true){
            try {
                System.out.println("客户端线程，等待读取从服务器端发送的消息...");
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                //如果服务器没有发送Message 对象，线程会阻塞在这里
                Message message = (Message) ois.readObject();
                //注意后面我们需要去使用 message
                if(message.getMessType().equals(MessageType.MESSAGE_RET_ONLINE_FRIEND)){//在线用户列表
                    //取出在线列表信息，并显示
                    //规定：用空格分隔
                    String [] onlineUsers = message.getContent().split(" ");
                    System.out.println("\n===========当前在线用户列表============");
                    for (int i = 0; i < onlineUsers.length; i++) {
                        System.out.println("用户："+onlineUsers[i]);
                    }
                }else if(message.getMessType().equals(MessageType.MESSAGE_COMM_MES)){
                    System.out.println("\n"+message.getSender()+" 对 "+message.getGetter()+"说："+message.getContent());
                }else if(message.getMessType().equals(MessageType.MESSAGE_TO_ALL_MES)){
                    System.out.println("\n"+ message.getSender() +" 对大家说："+message.getContent());
                }else if(message.getMessType().equals(MessageType.MESSAGE_FILE_MES)){
                    System.out.println("\n"+message.getSender()+" 给 "
                            +message.getGetter()+" 发送了文件到我的电脑 "+message.getDest());
                    FileOutputStream fileOutputStream = new FileOutputStream(message.getDest());
                    fileOutputStream.write(message.getFileBytes());
                    fileOutputStream.close();
                    System.out.println("\n保存文件成功~");

                }
                else{
                    System.out.println("其他类型信息");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }
}
