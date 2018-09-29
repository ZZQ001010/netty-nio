package com.zzq.noblockingnio;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * 服务器端
 */
public class NoBlockingNIOServer {

    static ServerSocketChannel serverSocketChannel ;
    static volatile Selector selector ;
    static ByteBuffer buffer ;

    /**
     * 初始化通道
     * @param port
     * @throws IOException
     */
    public static void initchannel(Integer port) throws IOException{
        serverSocketChannel= ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(port));
    }

    /**
     * 注册通道到选择器中
     * @throws Exception
     */
    public static  void regSelector() throws  Exception{
        selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }


    /**
     *
     */
    public static void select () throws  Exception{
        while (selector.select()>0){
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            while (it.hasNext()){
                SelectionKey selectionKey  = it.next();
                it.remove();
                if(selectionKey.isAcceptable()){
                    accept();
                }else if(selectionKey.isReadable()){
                    read((SocketChannel) selectionKey.channel());
                }

            }
        }
    }




    /**
     *
     */
    public static void accept(){
            try {
                SocketChannel socketChannel = serverSocketChannel.accept();
                socketChannel.configureBlocking(false);
                System.out.println("accept ........");
                socketChannel.register(selector, SelectionKey.OP_READ);
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("连接下线");
                return;
            }
        }

    public static void read(SocketChannel socketChannel){
            try {
                int len = 0;
                buffer=ByteBuffer.allocate(1024);
                while ((len = socketChannel.read(buffer)) > 0) {
                    buffer.flip();
                    System.out.println(new String(buffer.array(), 0, len));
                    buffer.clear();
                }
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("读写失败");
                return;
            }
        }


    public static void main(String[] args)  throws  Exception{
        initchannel(8888);
        regSelector();
        select() ;
    }
}