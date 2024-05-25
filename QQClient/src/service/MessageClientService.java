package service;

import qqcommon.Message;
import qqcommon.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;


/**
 * @author 王俊彪
 * @version 1.0
 * 该类/对象，提供和服务端相关的服务方法
 */
public class MessageClientService {
    /**
     *
     * @param content 内容
     * @param senderId  发送者
     */
    public void sendMessageToAll(String content,String senderId){
        //构建 message 对象
        Message message = new Message();
        message.setSender(senderId);
        message.setContent(content);
        message.setSendTime(new Date().toString());
        message.setMessType(MessageType.MESSAGE_TO_ALL_MES);//普通聊天消息类型
        System.out.println(senderId + " 对大家" + " 说 "+content);

        //发送给服务端
        try {
            ObjectOutputStream oos = new ObjectOutputStream(
                    ManageClientConnectServerThread.getClientConnectServerThread(senderId).getSocket().getOutputStream()
            );
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    /**
     *
     * @param content 内容
     * @param senderId 发送用户id
     * @param getterId 接收用户id
     */
    public void sendMessageToOne(String content,String senderId,String getterId){
        //构建 message 对象
        Message message = new Message();
        message.setSender(senderId);
        message.setGetter(getterId);
        message.setContent(content);
        message.setSendTime(new Date().toString());
        message.setMessType(MessageType.MESSAGE_COMM_MES);//普通聊天消息类型
        System.out.println(senderId + " 对 "+getterId + " 说 "+content);

        //发送给服务端
        try {
            ObjectOutputStream oos = new ObjectOutputStream(
                    ManageClientConnectServerThread.getClientConnectServerThread(senderId).getSocket().getOutputStream()
            );
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
