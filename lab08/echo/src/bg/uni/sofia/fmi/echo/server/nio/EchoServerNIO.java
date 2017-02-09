package bg.uni.sofia.fmi.echo.server.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * An EchoServer developed with the non-blocking NIO API
 * The server has two functions:
 *  - To accept new clients
 *  - To echo back everything a client sends
 *
 */
public class EchoServerNIO implements AutoCloseable {
	
	public static final int SERVER_PORT = 4444;
	
	private Selector selector;
	private ByteBuffer echoBuffer;

	public EchoServerNIO(int port) throws IOException {
		// Create a new selector
		selector = Selector.open();

		// Open a listener on each port, and register each one with the selector
		ServerSocketChannel ssc = ServerSocketChannel.open();
		ssc.configureBlocking(false);
		ServerSocket ss = ssc.socket();
		InetSocketAddress address = new InetSocketAddress(port);
		ss.bind(address);

		ssc.register(selector, SelectionKey.OP_ACCEPT);
		
		echoBuffer = ByteBuffer.allocate(1024);

		System.out.println("EchoServer NIO listening on port " + port);
	}

	private void start() throws IOException {
		while (true) {
			int num = selector.select();
			
			if (num == 0) {
				continue;
			}
			
			Set<SelectionKey> selectedKeys = selector.selectedKeys();
			Iterator<SelectionKey> it = selectedKeys.iterator();

			while (it.hasNext()) {
				SelectionKey key = it.next();
				try {
					if (key.isAcceptable()) {
						this.accept(key);
					} else if (key.isReadable()) {
						this.read(key);
					}
				} catch(IOException e) {
					System.err.println("An error has occured. " + e.getMessage());
					e.printStackTrace();
				}
				it.remove();
			}
		}
	}
	
	/**
	 * Accept a new connection
	 * 
	 * @param key The key for which an accept was received
	 * @throws IOException In case of problems with the accept
	 */
	private void accept(SelectionKey key) throws IOException {
		// Accept the new connection
		ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
		SocketChannel sc = ssc.accept();
		sc.configureBlocking(false);

		// Add the new connection to the selector
		sc.register(selector, SelectionKey.OP_READ);

		System.out.println("Client " + sc + " connected");
	}
	
	/**
	 * Read data from a connection
	 * 
	 * @param key The key for which a data was received
	 */
	private void read(SelectionKey key) {
		SocketChannel sc = (SocketChannel) key.channel();
		try {
			// Echo data
			echoBuffer.clear();
			int numBytes = sc.read(echoBuffer);

			if (numBytes == -1) {
				// The channel is broken. Close it and cancel the key
				throw new IOException("Broken channel");
			}

			echoBuffer.flip();

			sc.write(echoBuffer);
			System.out.println("Client " + sc + " wrote " + numBytes);
		} catch (IOException ioe) {
			// The channel is broken. Close it and cancel the key
			try {
				sc.close();
			} catch (IOException e) {
				// Nothing that we can do
			}
			key.cancel();
			return;
		}
	}
	
	@Override
	public void close() throws Exception {
		if (selector != null) {
			try {
				selector.close();
			} catch (IOException e) {
				// Nothing that we can do
			}
		}
	}

	public static void main(String args[]) throws Exception {
		try (EchoServerNIO es = new EchoServerNIO(SERVER_PORT)) {
			es.start();
		} catch (Exception e) {
			System.out.println("An error has occured");
			e.printStackTrace();
		}
	}
}