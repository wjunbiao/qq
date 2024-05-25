package service;

import qqcommon.Message;
import qqcommon.MessageType;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * @author 王俊彪
 * @version 1.0
 * 该类/对象完成文件传输服务
 */
public class FileClientService {
    /**
     *
     * @param src  源文件
     * @param dest  把该文件传输到对方的哪个目录
     * @param senderId  发送用户id
     * @param getterId  接收用户id
     */
    public void sendFileToOne(String src,String dest,String senderId,String getterId){
            //读取 src ---> message
        Message message = new Message();
        message.setMessType(MessageType.MESSAGE_FILE_MES);
        message.setSender(senderId);
        message.setGetter(getterId);
        message.setSrc(src);
        message.setDest(dest);

        //需要将文件读取
        FileInputStream fileInputStream = null;
        byte [] fileBytes = new byte[(int)new File(src).length()];//将src 文件读入到字节数组

        try {
            fileInputStream = new FileInputStream(src);
            fileInputStream.read(fileBytes);

            //将文件对应字节数组放到到message
            message.setFileBytes(fileBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(fileInputStream!=null){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //提示信息
        System.out.println("\n"+senderId +" 给 "+getterId +"发送文件"+src +" 到对方电脑目录"+dest);

        //发送
        try {
            ObjectOutputStream oos =
                    new ObjectOutputStream(
                            ManageClientConnectServerThread.getClientConnectServerThread(senderId).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
