package nachos.pokemen;

import nachos.machine.Machine;
import nachos.machine.MalformedPacketException;
import nachos.machine.NetworkLink;
import nachos.machine.Packet;
import nachos.threads.Semaphore;

public class MyNetworkLink {

	NetworkLink net;
	public Boolean isTrading = false;
	public Boolean isWaiting = false;
	
	Runnable read = new Runnable() {
		@Override
		public void run() {
			if(isTrading){
				MainSystem.sem.V();
			}
			else if(isWaiting) {
			}
			else if(!isTrading){
				for (int i = 0; i < 40; i++) {
					System.out.println();
				}
				System.out.println("You got new trade request!");
				System.out.println("Press enter to continue...");
				isTrading = true;
			}
		}
	};
	
	public MyNetworkLink() {
		if(net == null) {
			net = Machine.networkLink();
			net.setInterruptHandlers(read,null);
		}
	}
	
	public String readMsg() {
		String msg = "";
		
		Packet pkt = net.receive();
		try {
			msg = new String(pkt.contents);
		}catch(NullPointerException e) {
			return null;
		}
		
		if(msg.isEmpty()) 
			return "";
		
		isTrading = false;
		return msg;
	}
	
	public boolean sendMsg(String msg, int dst) {
		try {
			Packet p = new Packet(dst, getAddr(), msg.getBytes());
			net.send(p);
			isTrading = true;
			return true;
		} catch (MalformedPacketException e) {
			return false;
		}
	}
	
	public String returner(String x){
		return x;
	}
	
	public Integer getAddr() {
		return net.getLinkAddress();
	}
	

}
