package qqcommon;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author 王俊彪
 * @version 1.0
 * 表示客户端和服务端通信时的消息对象
 */
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;//增强兼容性
    private String sender;//发送者
    private String getter;//接收者
    private String content;//消息内容
    private String sendTime;//发送时间
    private String messType;//消息类型【可以在接口定义消息类型】

    //进行扩展，和文件相关的成员
    private byte [] fileBytes;//存放文件的数组
    private int fileLen= 0;//文件长度
    private String dest;//文件传输到那里
    private String src;//源文件路径

    //进行扩展，离线消息成员
    private String offLineId;//离线用户
    private ArrayList<Message> messageArrayList;//离线成员的消息


    public String getOffLineId() {
        return offLineId;
    }

    public void setOffLineId(String offLineId) {
        this.offLineId = offLineId;
    }

    public ArrayList<Message> getMessageArrayList() {
        return messageArrayList;
    }

    public void setMessageArrayList(ArrayList<Message> messageArrayList) {
        this.messageArrayList = messageArrayList;
    }

    public byte[] getFileBytes() {
        return fileBytes;
    }

    public void setFileBytes(byte[] fileBytes) {
        this.fileBytes = fileBytes;
    }

    public int getFileLen() {
        return fileLen;
    }

    public void setFileLen(int fileLen) {
        this.fileLen = fileLen;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getMessType() {
        return messType;
    }

    public void setMessType(String mesType) {
        this.messType = mesType;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getGetter() {
        return getter;
    }

    public void setGetter(String getter) {
        this.getter = getter;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }
}
