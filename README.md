# 工程简介

@MessageEvent(MessageEventType.GROUP)
使用@MessageEvent注解并且实现接口的监听器才会被注册。
如果只是实现接口但添加@MessageEvent注解事件将不会被监听响应
```
@MessageEvent(MessageEventType.GROUP)
public class GroupMessageEvent implements GroupMessageListener{

  @Override
    public void handler(GroupMessageEvent message) {
        System.out.println(message.getMessage());
    }

}
```
如果是私聊信息将接口改成私信接口

# 编译
要先使用Idea工具类上的构建->重新构建项目之后才可以运行SprigBoot,不然会报错找不到类
# 废了，废了
使用WebSocket给服务器发送信息，总是API不存在，给我报错404.这个就只实现了监听群消息和私信消息，使用了netty对接服务器的WebSocket
解决不了问题，我的代码写的也很垃圾，如果用使用http进行api通信，可以看一下之前的分支，我是后面几次的提交才尝试使用websocket访问api