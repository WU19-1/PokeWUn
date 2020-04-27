package nachos.pokemen;

import nachos.machine.Machine;
import nachos.machine.SerialConsole;
import nachos.threads.Semaphore;

public class MyConsole {
	SerialConsole sercon;
	Semaphore sem = new Semaphore(0);
	
	char temp = '\0';
	
	private Runnable read = new Runnable() {
		
		@Override
		public void run() {
			temp = (char) sercon.readByte();
			sem.V();
		}
	};
	
	private Runnable write = new Runnable() {
		
		@Override
		public void run() {
			sem.V();
		}
	};
	
	public MyConsole() {
		if(sercon == null) {
			 sercon = Machine.console();
			 sercon.setInterruptHandlers(read, write);
		}
	}
	
	public String scan() {
		String res = "";
		do {
			sem.P();
			if(temp =='\n') break;
			res += temp + "";
		}while(temp != '\n');
		return res;
	}
	
	public Integer scanInt() {
		String msg = scan();
		try {
			return Integer.parseInt(msg);
		}catch(Exception e) {
			if(msg.isEmpty()) return 0;
			System.out.println("Input must be an integer!");
			return 0;
		}
	}
	
	public Double scanDouble() {
		String msg = scan();
		try {
			return Double.parseDouble(msg);
		}catch(Exception e) {
			if(msg.isEmpty()) return 0.0;
			System.out.println("Input must be an integer!");
			return 0.0;
		}
	}
	
	public void print(String message) {
		for(int i = 0; i < message.length(); i++) {
			sercon.writeByte(message.charAt(i));
			sem.P();
		}
	}
	
	public void println(String message) {
		print(message + "\n");
	}
	
}
