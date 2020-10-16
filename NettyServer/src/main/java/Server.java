
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;

/**
 *
 */
public class Server {

    private int port;
    private JSONObject msgJson = new JSONObject();
    private JSONObject dataJson = new JSONObject();
    public static void main(String[] args){
        new Server(9090).start();
    }

    public Server(int port) {
        this.port = port;
    }

    public void start() {
        /**
         * 创建两个EventLoopGroup，即两个线程池，boss线程池用于接收客户端的连接，
         * 一个线程监听一个端口，一般只会监听一个端口所以只需一个线程
         * work池用于处理网络连接数据读写或者后续的业务处理（可指定另外的线程处理业务，
         * work完成数据读写）
         */
        EventLoopGroup boss = new NioEventLoopGroup(1);
        EventLoopGroup work = new NioEventLoopGroup();

            /**
             * 实例化一个服务端启动类，
             * group（）指定线程组
             * channel（）指定用于接收客户端连接的类，对应java.nio.ServerSocketChannel
             * childHandler（）设置编码解码及处理连接的类
             */
        try {
            //创建服务器端启动辅助类（用于配置参数）
            ServerBootstrap server = new ServerBootstrap()
                    //设置两个线程组
                    .group(boss, work)
                    //设置通道channel的类型
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    //设置线程队列中等待连接的个数
                    .option(ChannelOption.SO_BACKLOG, 128)

                    .childOption(ChannelOption.SO_KEEPALIVE, true)

                    .childHandler(new SimpleChatServerInitializer());
                 /*   .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast("decoder", new StringDecoder())
                                    .addLast("encoder", new StringEncoder())
                                    .addLast(new HelloWorldServerHandler());
                        }
                    });*/
            //绑定绑定并侦听某个端口
            ChannelFuture future = server.bind().sync();
            System.out.println("server started and listen " + port);

            msgJson.put("code",100001);
            dataJson.put("appName","bankmeet.apk");
            dataJson.put("versionCode",2);
            dataJson.put("downloadUrl","http://rjwlsy.top/DownloadFile/app-release.apk");
            msgJson.put("versionInfo",dataJson);
            String versionInfo = JSON.toJSONString(msgJson);
            System.out.println("versionInfo: "+versionInfo);
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            System.out.println("---------------Shut down");

            boss.shutdownGracefully();
            work.shutdownGracefully();
        }
    }

    public static class HelloWorldServerHandler extends ChannelInboundHandlerAdapter {

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            System.out.println("HelloWorldServerHandler active");
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println("server channelRead..");
            System.out.println(ctx.channel().remoteAddress()+"->Server :"+ msg.toString());
            ctx.write("server write"+msg);
            ctx.flush();
        }
    }
}

