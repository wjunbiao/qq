package qqclient.view;

import service.FileClientService;
import service.MessageClientService;
import service.UserClientService;
import utils.Utility;

import javax.rmi.CORBA.Util;
import java.io.ObjectOutputStream;

/**
 * @author 王俊彪
 * @version 1.0
 *客户端的菜单界面
 */
public class QQView {

    private boolean loop =true;//控制是否显示菜单
    private String key = "";//接收用户的键盘输入
   private UserClientService userClientService= new UserClientService();//对象是用于登录服务/注册用户
    private MessageClientService messageClientService = new MessageClientService();//对象用于用户私聊/群聊
    private FileClientService fileClientService =  new FileClientService();//对象是用于传输文件
    public static void main(String[] args) {
        new QQView().mainMenu();
        System.out.println("=============客户端退出系统=================");
    }
    //显示主菜单
    public void mainMenu(){
        while (loop){
            System.out.println("==========欢迎登录网络通讯系统===========");
            System.out.println("\t\t 1 登录系统");
            System.out.println("\t\t 9 退出系统");
            System.out.print("请输入您的选择：");
            key = Utility.readString(1);//读一个字符
            switch (key){
                case "1":
                    System.out.print("请输入用户名：");
                    String userId = Utility.readString(50);
                    System.out.print("请输入密  码：");
                    String pwd = Utility.readString(50);
                    //这里就比较麻烦了，需要到服务端去验证用户是否合法
                    //这里有许多代码
                    if(userClientService.checkUser(userId,pwd)){//还没有写完，先把整个逻辑打通
                        System.out.println("==============欢迎 用户（"+userId+")登录成功==============");

                        //进入二级菜单
                        while (loop){
                            System.out.println("==============网络通信系统二级菜单(用户 "+userId+")==============");
                            System.out.println("\t\t 1 显示在线用户列表");
                            System.out.println("\t\t 2 群发消息");
                            System.out.println("\t\t 3 私聊消息");
                            System.out.println("\t\t 4 发送文件");
                            System.out.println("\t\t 9 退出系统");
                            System.out.print("请输入您的选择：");
                            key = Utility.readString(1);//读一个字符
                            switch(key){
                                case "1":
                                    userClientService.onlineFriendList();
                                    System.out.println("显示在线用户列表");
                                    try {
                                        Thread.sleep(500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                case "2":
                                    System.out.print("请输入想对大家说的话：");
                                    String s = Utility.readString(100);
                                    //调用一个方法，将消息封装成message 对象，发送给服务端
                                    messageClientService.sendMessageToAll(s,userId);
//                                    System.out.println("群发消息");
                                    break;
                                case "3":
                                    System.out.print("请输入想聊天的用户号（在线/离线）：");
                                    String getterId = Utility.readString(50);
                                    System.out.print("请输入请说的话：");
                                    String content = Utility.readString(100);
                                    //编写一个方法，将消息发送给服务端
                                   messageClientService.sendMessageToOne(content,userId,getterId);
//                                    System.out.println("私聊消息");
                                    try {
                                        Thread.sleep(500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                case "4":
                                    System.out.print("请输入您想把文件发送给的用户（在线）：");
                                    getterId = Utility.readString(50);
                                    System.out.print("请输入要发送文件的路径（形式 d:\\ xxx.jpg）");
                                    String src = Utility.readString(100);
                                    System.out.print("请输入把文件发送给对应的路径（形式 d:\\xxx.jpg）");
                                    String dest = Utility.readString(100);
                                    fileClientService.sendFileToOne(src,dest,userId,getterId);
//                                    System.out.println("发送文件");
                                    break;
                                case "9":
                                    loop =false;
                                    userClientService.logout();
                                    break;

                            }
                        }

                    }else{//登录服务器失败
                        System.out.println("==============登录失败=================");
                    }
                    break;
                case "9":
                    loop = false;
                    break;
            }
        }
    }
}
