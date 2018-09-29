package com.zzq.blockingNIO;

import org.junit.Test;

import javax.naming.ldap.SortKey;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class BlockingNIO2 {

    @Test
    public void client() throws IOException {
        //客户端通道
        SocketChannel socketChannel;
        FileChannel fileChannel ;
        ByteBuffer buffer;

        socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 8888));
         fileChannel = FileChannel.open(Paths.get("pom.xml"), StandardOpenOption.READ);
         buffer = ByteBuffer.allocate(1024);
        while (fileChannel.read(buffer)!=-1){
            buffer.flip();
            socketChannel.write(buffer);
            buffer.clear();
        }

        socketChannel.shutdownOutput();  //这里是阻塞模式，使用shutdownOutput就可以看到效果，可以使用非阻塞操作


        //接受服务端返回的数据
        int len = 0 ;
        while ((len = socketChannel.read(buffer))!=-1){
            buffer.flip();
            System.out.println(new String(buffer.array(),0, len));
            buffer.clear();
        }


        fileChannel.close();
        socketChannel.close();
    }


    @Test
    public void server() throws Exception {
        ServerSocketChannel serverSokectChannel;
        FileChannel fileChannel ;
        ByteBuffer buf;
        SocketChannel socketChannel;
         serverSokectChannel = ServerSocketChannel.open();

        //绑定端口
         serverSokectChannel.bind(new InetSocketAddress(8888));
        socketChannel = serverSokectChannel.accept();
         //创建覆盖
         fileChannel = FileChannel.open(Paths.get("pom2.xml"),StandardOpenOption.WRITE,StandardOpenOption.CREATE);
         //创建buffer
         buf = ByteBuffer.allocate(1024);
        while (socketChannel.read(buf)!=-1){
            buf.flip();
            fileChannel.write(buf);
            buf.clear();
        }

        socketChannel.shutdownInput();

        //返回数据
        buf.put("upload success".getBytes());
        //压缩此缓冲区
        buf.flip();
        socketChannel.write(buf);
        socketChannel.close();
        fileChannel.close();
        serverSokectChannel.close();

    }





}
