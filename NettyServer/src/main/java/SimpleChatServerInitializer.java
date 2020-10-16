
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;


public class SimpleChatServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        //pipeline.addLast( new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
        pipeline.addLast( new LengthFieldBasedFrameDecoder(1024,0,4,0,4));
        pipeline.addLast( new LengthFieldPrepender(4, false));
//        pipeline.addLast( new StringDecoder());
//        pipeline.addLast( new StringEncoder());
        pipeline.addLast("handler", new SimpleChatServerHandler());  //处理业务逻辑
        System.out.println("SimpleChatClient:"+ch.remoteAddress() +"连接上");
    }
}