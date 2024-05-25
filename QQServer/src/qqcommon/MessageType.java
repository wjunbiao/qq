package qqcommon;

/**
 * @author 王俊彪
 * @version 1.0
 * 表示消息类型
 */
public interface MessageType {
    // 在接口中定义一些常量
    // 不同常量的值，表示不同的消息类型
    String MESSAGE_LOGIN_SUCCEED="1";//表示登录成功
    String MESSAGE_LOGIN_FAIL="2";//表示登录失败
    String MESSAGE_COMM_MES="3";//普通信息包
    String MESSAGE_GET_ONLINE_FRIEND="4";//要求返回在线用户列表
    String MESSAGE_RET_ONLINE_FRIEND="5";//返回在线用户列表
    String MESSAGE_CLIENT_EXIT="6";//客户端请求退出
    String MESSAGE_TO_ALL_MES="7";//群发消息包
    String MESSAGE_FILE_MES="8";//文件消息（发送文件）
    String MESSAGE_offOnLine_MES="9";

}
