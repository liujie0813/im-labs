package com.timberliu.im.client;

import com.timberliu.im.client.console.ConsoleCommandManager;
import com.timberliu.im.client.console.LoginConsoleCommand;
import com.timberliu.im.client.handler.*;
import com.timberliu.im.codec.PacketDecoder;
import com.timberliu.im.codec.PacketEncoder;
import com.timberliu.im.codec.Spliter;
import com.timberliu.im.server.handler.IMIdleStateHandler;
import com.timberliu.im.util.SessionUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.AttributeKey;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by liujie on 2021/6/22
 */
public class NettyClient {

    public static AtomicBoolean nextCommand = new AtomicBoolean(false);

    private static final int MAX_RETRY = 3;

    public static void main(String[] args) {
        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();

        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        channel.pipeline().addLast(new IMIdleStateHandler());
                        channel.pipeline().addLast(new Spliter());
                        channel.pipeline().addLast(new PacketDecoder());
                        channel.pipeline().addLast(new LoginResponseHandler());
                        channel.pipeline().addLast(new MessageResponseHandler());
                        channel.pipeline().addLast(new GroupMessageResponseHandler());
                        channel.pipeline().addLast(new CreateGroupResponseHandler());
                        channel.pipeline().addLast(new JoinGroupResponseHandler());
                        channel.pipeline().addLast(new QuitGroupResponseHandler());
                        channel.pipeline().addLast(new ListGroupMembersResponseHandler());
                        channel.pipeline().addLast(new LogoutReponseHandler());
                        channel.pipeline().addLast(new PacketEncoder());
                        channel.pipeline().addLast(new HeartBeatTimerHandler());
                    }
                })
                .attr(AttributeKey.newInstance("clientName"), "nettyClient")
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true);

        connect(bootstrap, "127.0.0.1", 8888, MAX_RETRY);
    }

    private static void connect(Bootstrap bootstrap, String host, int port, int retry) {
        bootstrap.connect(host, port)
                .addListener(future -> {
                    if (future.isSuccess()) {
                        System.out.println("Client connect success: " + host + ":" + port);
                        Channel channel = ((ChannelFuture) future).channel();
                        startConsoleThread(channel);
                    } else if (retry == 0) {
                        System.out.println("Connect retry failed, give up");
                    } else {
                        int order = (MAX_RETRY - retry) + 1;
                        int delay = 1 << order;
                        System.out.println("Client connect failed，retry the " + order + " times");
                        bootstrap.config().group().schedule(
                                () -> connect(bootstrap, host, port, retry - 1),
                                delay,
                                TimeUnit.SECONDS);
                    }
                }).channel();
    }

    private static void startConsoleThread(Channel channel) {
        ConsoleCommandManager consoleCommandManager = new ConsoleCommandManager();
        LoginConsoleCommand loginConsoleCommand = new LoginConsoleCommand();

        new Thread(() -> {
            while (!Thread.interrupted()) {
                Scanner scanner = new Scanner(System.in);
                if (!SessionUtil.hasLogin(channel)) {
                    System.out.println("Please Log in!!!");
                    loginConsoleCommand.exec(scanner, channel);
                } else {
                    if (nextCommand.get()) {
                        nextCommand.compareAndSet(true, false);
                        consoleCommandManager.exec(scanner, channel);
                    }
                }
            }
        }).start();
    }
}
