package qqserver.service;

import qqcommon.Message;
import qqcommon.MessageType;
import qqcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author 王俊彪
 * @version 1.0
 * 这是服务端 ，在监听9999，等待客户端连接，并保持通讯
 */
public class QQServer {

    private ServerSocket serverSocket = null;
    //创建一个集合，存放多个用户，如果是这些用户登录，就认为合法
    //这里我们了可以使用 ConcurrentHashMap,可以处理并发的集合，没有线程安全
    //HashMap 没有处理线程安全，因此 在多线程情况下是不安全的
    //ConcurrentHashMap 处理的线程安全，即线程同步处理，在多线程情况下是安全
    private static HashMap<String, User> validUser = new HashMap<>();
    private static HashMap<String, ArrayList<Message>> offLineDb = new HashMap<>();

    public static HashMap<String, ArrayList<Message>> getOffLineDb() {
        return offLineDb;
    }

    public static void setOffLineDb(HashMap<String, ArrayList<Message>> offLineDb) {
        QQServer.offLineDb = offLineDb;
    }

    static {//静态代码块，初始化validUser
        validUser.put("100", new User("100", "123456"));
        validUser.put("200", new User("200", "123456"));
        validUser.put("300", new User("300", "123456"));
        validUser.put("至尊宝", new User("至尊宝", "123456"));
        validUser.put("紫霞仙子", new User("紫霞仙子", "123456"));
        validUser.put("菩提老祖", new User("菩提老祖", "123456"));
    }

    public boolean checkUser(String userId, String pwd) {
        ServerConnectClientThread s = ManageClientThreads.getServerConnectClientThread(userId);
        if (s != null) {
            System.out.println("该用户已经登录，不能重复登录");
            return false;
        }
        User user = validUser.get(userId);
        if (user == null) {//说明userId 没有存在在 validUser的key中
            System.out.println("没有用户名");
            return false;
        }
        if (!(user.getPassword().equals(pwd))) {//userId 正确，但密码错误
            System.out.println("密码错误");
            return false;
        }
        return true;
    }

    public QQServer() {
        // 注意端口可以写在配置文件中
        try {
            System.out.println("服务器在9999端口正在监听.....");
            serverSocket = new ServerSocket(9999);
            new Thread(new SendNewsToAllService()).start();

            while (true) {//当和某个客户端连接后，会继续监听，因此while
                Socket socket = serverSocket.accept();
                //得到socket 关联的输入流
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                User u = (User) ois.readObject();//读取客户端的user对象
                //得到socket 关联的输出流
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                //创建一个message 对象，准备回复客户端
                Message message = new Message();
                if (checkUser(u.getUserId(), u.getPassword())) {//登录成功
                    message.setMessType(MessageType.MESSAGE_LOGIN_SUCCEED);
                    //将message 数据回复客户端
                    oos.writeObject(message);

                    //创建一个线程，和客户端通信，该线程执有socket 对象
                    ServerConnectClientThread serverConnectClientThread =
                            new ServerConnectClientThread(socket, u.getUserId());
                    //启动该线程
                    serverConnectClientThread.start();
                    //把该线程对象，放入到一个集合中，进行管理
                    ManageClientThreads.addClientThread(u.getUserId(), serverConnectClientThread);


                    Message message1 = new Message();
                    Iterator<String> iterator = offLineDb.keySet().iterator();
                    while (iterator.hasNext()) {
                        String next = iterator.next();
                        System.out.println(next);
                        for (int i = 0; i < offLineDb.get(next).size(); i++) {//接收者是唯一的，而发送者有许多
                            if (next.equals(u.getUserId())) {
                                message1.setSender(offLineDb.get(next).get(i).getSender());
                                message1.setGetter(u.getUserId());
                                message1.setMessType(MessageType.MESSAGE_offOnLine_MES);
                                message1.setMessageArrayList(offLineDb.get(next));
                            }
                        }
                    }


                    oos.writeObject(message1);
                } else {//登录失败
                    System.out.println("用户:" + u.getUserId() + "密码:" + u.getPassword() + "验证失败");
                    message.setMessType(MessageType.MESSAGE_LOGIN_FAIL);
                    oos.writeObject(message);
                    socket.close();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            //如果服务器退出了while 说明服务端不在监听，因此关闭ServeSocket
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}
