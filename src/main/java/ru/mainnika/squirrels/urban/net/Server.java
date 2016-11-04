package ru.mainnika.squirrels.urban.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server implements Runnable {

    private static final Logger log;

    static {
        log = Logger.getLogger(Connection.class.getName());
    }

    private final Thread thread;
    private final InetSocketAddress addr;
    private ServerSocketChannel channel;
    private Selector selector;

    public Server() {

        this.thread = new Thread(this);
        this.addr = new InetSocketAddress("localhost", 7777);
    }

    public Thread start() {

        this.thread.start();

        return this.thread;
    }

    @Override
    public void run() {

        try {
            selector = Selector.open();

            channel = ServerSocketChannel.open();
            channel.bind(addr);
            channel.configureBlocking(false);
            channel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException err) {
            log.log(Level.WARNING, "Failed to start server: {0}", err.getMessage());
            return;
        }

        while (!Thread.interrupted()) {

            try {
                selector.select();
            } catch (IOException err) {
                log.log(Level.WARNING, "Failed to select: {0}", err.getMessage());
                continue;
            }

            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iter = keys.iterator();

            while (iter.hasNext()) {
                SelectionKey key = iter.next();

                try {
                    this.processAcceptable(key);
                    this.processReadable(key);
                } catch (IOException err) {
                    log.log(Level.WARNING, "Failed to handle: {0}", err.getMessage());
                } finally {
                    iter.remove();
                }
            }
        }
    }

    private void processAcceptable(SelectionKey acceptable) throws IOException {
        if (!acceptable.isAcceptable()) {
            return;
        }

        SocketChannel client = this.channel.accept();

        client.configureBlocking(false);
        client.register(this.selector, SelectionKey.OP_READ);

        log.log(Level.INFO, "Client connected: {0}", client.getRemoteAddress());
    }

    private void processReadable(SelectionKey readable) throws IOException {
        if (!readable.isReadable()) {
            return;
        }

        SocketChannel client = (SocketChannel) readable.channel();

        log.log(Level.INFO, "Client was sent something: {0}", client.getRemoteAddress());
    }
}
