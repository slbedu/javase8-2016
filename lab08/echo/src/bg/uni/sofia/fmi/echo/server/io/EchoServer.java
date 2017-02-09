package bg.uni.sofia.fmi.echo.server.io;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * An EchoServer developed with the blocking API of java.net.* The server has
 * two functions: - To accept new clients - To echo back everything a client
 * sends
 */
public class EchoServer implements AutoCloseable {

	public static final int SERVER_PORT = 4444;

	private ServerSocket serverSocket = null;
	private Map<Socket, ClientConnectionThread> clients = new HashMap<>();

	private class ClientConnectionThread extends Thread {
		private Socket socket;
		private volatile boolean run = true;

		/**
		 * Constructor for creating a new client connection
		 * 
		 * @param socket
		 *            The socket of the client
		 */
		public ClientConnectionThread(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			System.out.println("Client " + socket + " connected");

			try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					PrintWriter printWriter = new PrintWriter(socket.getOutputStream())) {
				while (run) {
					// We can read only in lines. If we want to read more than a
					// line we can use
					// the method reader.read
					String request = reader.readLine();

					if (request == null) {
						// The connection is broken. Exit the thread
						break;
					} else if (request.equals("count")) {
						request = countClients();
					} else if (request.contains("download ")) {
						request = getResponseFromDownloadImage(request);
					}
					// Write back to the client
					printWriter.println(request);
					printWriter.flush();

					System.out.println("Client " + socket + " received " + request);
				}
			} catch (IOException e) {
				System.err.println("An error occured while reading or writeing to the client. " + e.toString());
				e.printStackTrace();
			}
			clients.remove(socket);
			System.out.println("Client " + socket + " disconnected");
		}

		private String countClients() {
			return Integer.toString(clients.size());
		}

		private String getResponseFromDownloadImage(String request) {
			String[] commands = request.split(" ");
			if (commands.length != 3) {
				return "Invalid arguments to download an image - syntax: \"download {URL} {Path}\"";
			}

			String urlString = commands[1];
			String path = commands[2];
			URL url;
			try {
				url = new URL(urlString);
			} catch (MalformedURLException e) {
				return urlString + " is not valid.";
			}
			try (InputStream in = new BufferedInputStream(url.openStream());
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					FileOutputStream fos = new FileOutputStream(path);) {
				byte[] buf = new byte[1024];
				int n = 0;
				while (-1 != (n = in.read(buf))) {
					out.write(buf, 0, n);
				}
				byte[] response = out.toByteArray();

				fos.write(response);
			} catch (IOException e) {
				return e.getMessage();
			}
			return "Image is downloaded successfully";
		}

		/**
		 * Method to stop the thread gracefully.
		 */
		public void stopThread() {
			run = false;
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					// Nothing that we can do
				}
			}
		}

	}

	/**
	 * Create a new EchoServer and reserver the server port
	 * 
	 * @param port
	 *            The port on which to listen for new connections
	 * @throws IOException
	 *             If we could not bind the port
	 */
	public EchoServer(int port) throws IOException {
		System.out.println("EchoServer IO listening on port " + port);
		// Open the port. It can fail if the port is already taken
		this.serverSocket = new ServerSocket(port);
	}

	/**
	 * Allow the server to accept new connections.
	 * 
	 * @throws IOException
	 *             In case of problems with the server socket
	 */
	public void start() throws IOException {
		while (true) {
			// Accepts new clients
			Socket socket = serverSocket.accept();
			// Processes the clients into a new thread
			ClientConnectionThread clientThread = new ClientConnectionThread(socket);
			clients.put(socket, clientThread);

			// We set the thread as a daemon. If only daemon threads are left
			// the
			// java application will exit
			clientThread.setDaemon(true);
			clientThread.start();
		}
	}

	/**
	 * Close all the resources that the server is using
	 */
	@Override
	public void close() throws Exception {
		if (serverSocket != null) {
			try {
				serverSocket.close();
			} catch (IOException e) {
				System.err.println("Could not close the server socket. " + e.getMessage());
			}
		}
		// Stop all client connections
		for (ClientConnectionThread client : clients.values()) {
			client.stopThread();
		}
		clients.clear();
	}

	public static void main(String[] args) throws IOException {
		try (EchoServer echoServer = new EchoServer(SERVER_PORT)) {
			echoServer.start();
		} catch (Exception e) {
			System.err.println("An error has occured. " + e.getMessage());
			e.printStackTrace();
		}
	}
}