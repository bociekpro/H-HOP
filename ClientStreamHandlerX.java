package Mechanics;
import java.net.Socket;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.net.SocketException;
//import java.net.SocketTimeoutException;
import java.lang.Thread;

public class ClientStreamHandlerX extends Thread{

	private final int timeout = 5000;

	private Socket s;
	private InputStream iS;
	private OutputStream oS;
	private ObjectOutputStream oOut;
	private ObjectInputStream oIn;
	
	private SharedData sharedData;
	private Summer[] summers;
	

	public ClientStreamHandlerX(Socket s) throws Exception {
		try{
			s.setSoTimeout(timeout);
			this.s = s;
			iS = s.getInputStream();
			oS = s.getOutputStream();
			oOut = new ObjectOutputStream(oS);
			oIn = new ObjectInputStream(iS);
		} catch(SocketException se){
			close(true);
		} catch(IOException ioe){
			close(true);
		}
	}
	
	
	public void listen() throws Exception {
		try{
			int clients = oIn.readInt();
			System.out.println("Recieved integer: "+clients);
			
			sharedData = new SharedData(clients);
			summers = new Summer[clients];
			
			for(int i = 0; i < clients; i++){
				Summer summer = new Summer(sharedData, i);
				summers[i] = summer;
				summer.start();

				int port = summer.getPort();
				oOut.writeInt(port);
				oOut.flush();
				System.out.println("Created summer #"+i+" at port "+port);
			}
			
			while(true){
				if(oIn.available() > 0){
					
					int data = oIn.readInt();
					System.out.println("Recieved Question " + data);
					int answer = 0;
					
					switch(data){
						case 0:
							shutDownSummers();
							close(false);
							return;
						case 1:
							answer = sharedData.sumAll().intValue();
							break;
						case 2:
							answer = sharedData.getMax();
							break;
						case 3:
							answer = sharedData.countAll().intValue();
							break;
						default:
							answer = -1;
					}
					oOut.writeInt(answer);
					oOut.flush();
					Thread.sleep(750);
				}
			}
		} catch(IOException ioe){
			ioe.printStackTrace(System.out);
		}
		close(true);
	}
	
	private void shutDownSummers(){
		for(Summer s : summers){
			s.shutdown();
		}
	}

	public void close(boolean sendError) throws IOException{
		if(sendError){
			oOut.writeInt(-1);
			oOut.flush();
		}
		oOut.close();
		oIn.close();
		oS.close();
		iS.close();
		s.close();
	}
}