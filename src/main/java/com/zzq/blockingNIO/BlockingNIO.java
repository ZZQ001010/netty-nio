package com.zzq.blockingNIO;

import org.junit.Test;
import sun.security.util.Length;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class BlockingNIO {

    @Test
    public void client() throws IOException {
        SocketChannel channel;
        ByteBuffer buf;
        FileInputStream inputStream;
        BufferedInputStream buffIn;

        //开启通道
         channel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 8888));
        //创建缓冲区
         buf = ByteBuffer.allocate(1024);
        //读取本地文件，发送服务器
        FileChannel inChannel = FileChannel.open(Paths.get("pom.xml"), StandardOpenOption.READ);
        while (inChannel.read(buf)!=-1){
            buf.flip();
            channel.write(buf);
            buf.clear();
        }

        //关闭通道
        inChannel.close();
        channel.close();
    }


    @Test
    public void server() throws  Exception{
        ServerSocketChannel ssChannel ;
        SocketChannel clientConnection ;
        ByteBuffer buf;
        FileChannel outChannel;

        //获取通道
         ssChannel = ServerSocketChannel.open();
        outChannel = FileChannel.open(Paths.get("pom2.xml"), StandardOpenOption.WRITE,StandardOpenOption.CREATE);
        //绑定连接
        ssChannel.bind(new InetSocketAddress(8888));

        //获取客户端的连接
         clientConnection = ssChannel.accept();
        //创建缓冲区
         buf= ByteBuffer.allocate(1024);
        while (clientConnection.read(buf)!=-1){
            buf.flip();
            outChannel.write(buf);
            buf.clear();
        }
        outChannel.close();
        ssChannel.close();
        clientConnection.close();
    }

}
