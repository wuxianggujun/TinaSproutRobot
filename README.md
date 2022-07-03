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
# 延伸阅读

