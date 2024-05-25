package qqserver.service;

import qqcommon.Message;
import qqcommon.MessageType;
import utils.Utility;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author 王俊彪
 * @version 1.0
 */
public class SendNewsToAllService  implements  Runnable{
    @Override
    public void run() {
        //为了推送多次新闻，使用while
        while(true){
            System.out.println("请输入服务器要推送的新闻或消息[输入exit表示退出推送服务线程]:");
            String news = Utility.readString(100);

            if("exit".equals(news)){
                break;
            }

            //构造一个消息，群发消息
            Message message = new Message();
            message.setSender("服务器");
            message.setContent(news);
            message.setMessType(MessageType.MESSAGE_TO_ALL_MES);
            message.setSendTime(new Date().toString());
            System.out.println("服务器推送消息给所有人说："+news);

            //遍历当前所有的通信线程，得到socket,并发送message
            HashMap<String, ServerConnectClientThread> hm = ManageClientThreads.getHm();
            Iterator<String> iterator = hm.keySet().iterator();
            while (iterator.hasNext()) {
                String onLineUserId =  iterator.next();
                try {
                    ObjectOutputStream oos =
                            new ObjectOutputStream(hm.get(onLineUserId).getSocket().getOutputStream());
                oos.writeObject(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
