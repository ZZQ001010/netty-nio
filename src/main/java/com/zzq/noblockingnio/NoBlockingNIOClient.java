package com.zzq.noblockingnio;




import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;

/**
 * 非阻塞
 * 1.通道负责连接
 *  java.nio.channels.Channel接口
 *          SelecttableChannel
 *              SocketChannel
 *              ServerSocketChannel
 *              DatagramChannel
 *          Pipe.SinkChannel
 *          Pipe.SourceChannel
 * 2.缓冲区buffer 负责数据的存取
 * 3.选择器，是selecttableChannel 的多路复用器，用于监控SelecttableChannel的IO状态
 */
public class NoBlockingNIOClient {

    public static void main(String[] args)  throws  Exception{
        //1. 获取通道
        SocketChannel sChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 8888));

        //2. 切换非阻塞模式
        sChannel.configureBlocking(false);

        //3. 分配指定大小的缓冲区
        ByteBuffer buf = ByteBuffer.allocate(1024);

        //4. 发送数据给服务端
        Scanner scan = new Scanner(System.in);

        while(scan.hasNext()){
            String str = scan.next();
            buf.put((new Date().toString() + "\n" + str).getBytes());
            buf.flip();
            sChannel.write(buf);
            buf.clear();
        }

        //5. 关闭通道
        sChannel.close();
    }
}
