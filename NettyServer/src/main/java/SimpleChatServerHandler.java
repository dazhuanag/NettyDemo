
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.GlobalEventExecutor;

import javax.swing.*;

public class SimpleChatServerHandler extends SimpleChannelInboundHandler<Object> { // (1)

    private int code;
    private String updateAPPVersion;
    private VersionInfo versionInfo;
    private String versionData;
    private JSONObject msgJson = new JSONObject();
    private JSONObject dataJson = new JSONObject();



    /**
     * A thread-safe Set  Using ChannelGroup, you can categorize Channels into a meaningful group.
     * A closed Channel is automatically removed from the collection,
     */
    public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {  // (2)
        Channel incoming = ctx.channel();
        System.out.println("---------------handlerAdded");
        // Broadcast a message to multiple Channels
        channels.writeAndFlush("[SERVER] - " + incoming.remoteAddress() + " 加入\n");
        channels.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {  // (3)
        Channel incoming = ctx.channel();
        System.out.println("---------------handlerRemoved");
        // Broadcast a message to multiple Channels
        channels.writeAndFlush("[SERVER] - " + incoming.remoteAddress() + " 离开\n");

        // A closed Channel is automatically removed from ChannelGroup,
        // so there is no need to do "channels.remove(ctx.channel());"
    }
//
//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//
//        System.out.println("---------------channelRead0 have received3 : " + msg + "  ");
//        channels.writeAndFlush("Server has reciver message3:" + msg + "\n");
//        super.channelRead(ctx, msg);
//    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {

        ByteBuf buf=(ByteBuf) msg;
        byte[] req=new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body=new String(req,"UTF-8");

//        byte [] bytes = msg.getBytes(CharsetUtil.UTF_8);
//        ByteBuf buf = Unpooled.wrappedBuffer(bytes);
//        String result = buf.toString(CharsetUtil.UTF_8);
        System.out.println("channelRead0 have received : " + body  + "  ");
        JsonObject jsonObject = new JsonParser().parse(body).getAsJsonObject();
        code = jsonObject.get("code").getAsInt();
        JsonObject data = jsonObject.get("data").getAsJsonObject();
        updateAPPVersion = data.get("updateAPPVersion").getAsString();
        if (code ==100001 && updateAPPVersion.equals("true")){
            Gson gson = new Gson();
            //versionData = gson.toJson(versionInfo);
            //versionData = new String(versionData.getBytes(),"UTF-8");
            //VersionInfo versionInfo = gson.fromJson(VersionInfo.getString(),VersionInfo.class);
            //System.out.println("versioninfo: "+versionData);
            //msgJson.put("code",100001);
            //dataJson.put("appName","test01");
            //dataJson.put("version","1.0.0");
            //msgJson.put("versionInfo",dataJson);
            //String dataMsg = msgJson.toString();
            versionInfo = new VersionInfo("test01",1.1,true,"版本一","http://rjwlsy.top/DownloadFile/Test.zip");
            String versionData = JSON.toJSONString(versionInfo);
            versionData = new String(versionData.getBytes("UTF-8"),"UTF-8");
            System.out.println("versionData:"+versionData);
            msgJson.put("code",100001);
            //dataJson.put("appName","bankmeet2.apk");
            //dataJson.put("versionCode",2);
            //dataJson.put("downloadUrl","http://rjwlsy.top/DownloadFile/app-release.apk");
            //dataJson.put("apkVersionName","2.0.1");
            //dataJson.put("updateInfo","版本优化2");
            //dataJson.put("updateForce",false);
            msgJson.put("versionInfo",dataJson);
            String versionInfo = JSON.toJSONString(msgJson);
            System.out.println("versionInfo: "+versionInfo);
            ByteBuf resp= Unpooled.copiedBuffer(versionInfo.getBytes());
            ctx.writeAndFlush(resp);
            //ctx.writeAndFlush(Unpooled.copiedBuffer(body.getBytes()));
        }
        System.out.println("jsonObject:"+jsonObject);


        Channel incoming = ctx.channel();
        for (Channel channel : channels) {
            if (channel != incoming){
                channel.writeAndFlush("[" + incoming.remoteAddress() + "]" + "Hello " + "\n");
            } else {
                //channel.writeAndFlush(Unpooled.copiedBuffer(dataMsg.getBytes()));
                //channel.writeAndFlush(Unpooled.copiedBuffer(body.getBytes()));
                System.out.println("aaaaa!!!!!1");
            }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception { // (5)
        Channel incoming = ctx.channel();
        System.out.println("SimpleChatClient:---"+incoming.remoteAddress()+"---在线");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception { // (6)
        Channel incoming = ctx.channel();
        System.out.println("SimpleChatClient:---"+incoming.remoteAddress()+"---掉线");
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        Channel incoming = ctx.channel();
        System.out.println("SimpleChatClient:---"+incoming.remoteAddress()+"---异常");
        // 当出现异常就关闭连接
        cause.printStackTrace();
        ctx.close();
    }
}