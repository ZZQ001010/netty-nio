package com.zzq.pip;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;

//通过管道两个线程传输数据
public class pipTest {
    static  Pipe pipe ;
    static ByteBuffer buf;

    public static void main(String[] args) throws IOException {
        //将缓冲区中的数据写入管道
         pipe = Pipe.open();
         buf =ByteBuffer.allocate(1024);
        Pipe.SinkChannel sinkChannel = pipe.sink();
        buf.put("hello".getBytes());
        buf.flip();
        sinkChannel.write(buf);


        //读取缓冲区的数据
        Pipe.SourceChannel sourceChannel = pipe.source();
        buf.flip();
        int len  =sourceChannel.read(buf);
        System.out.println(new String(new String(buf.array(),0,len)));
        sourceChannel.close();
        sinkChannel.close();


    }
}
