package nachos.pokemen;

import nachos.machine.Machine;
import nachos.threads.Semaphore;

public class MainSystem {
	
	MyConsole mc = new MyConsole();
	static MyNetworkLink net = new MyNetworkLink();
	static PokeDex pokedex = new PokeDex();
	static int choose = 0;
	static Semaphore sem = new Semaphore(0);
	
	public void cls(){
		for (int i = 0; i < 40; i++) {
			mc.println("");
		}
	}
	
	public void press(){
		mc.print("Press enter to continue...");
		mc.scan();
	}
	
	public void menu() {
		mc.println("Pokemen GO");
		mc.println("Poke Address : " + net.getAddr());
		mc.println("1. Insert pokemen");
		mc.println("2. View pokemen");
		mc.println("3. Trade pokemen with another trainer");
		mc.println("4. Exit");
		mc.print("Choose >> ");
	}
	
	public MainSystem() {
		String name = "";
		Integer hp = 0;
		Integer level = 0;
		Double exp = 0.0;
		int idx = 0;
		int address = 0;
		do {
			cls();
			menu();
			idx = mc.scanInt();
			if(idx == 1) {
				do {
					mc.print("Insert pokemen's name [4 - 11]: ");
					name = mc.scan();
				}while(name.length() < 4 || name.length() > 11);
				
				do {
					mc.print("Insert pokemen's hp [16 - 255]: ");
					hp = mc.scanInt();
				}while(hp < 16 || hp > 255);
				
				do {
					mc.print("Insert pokemen's level [1 - 99]: ");
					level = mc.scanInt();
				}while(level < 1 || level > 99);
				
				do {
					mc.print("Insert pokemen's current exp [0.0-608.0]: ");
					exp = mc.scanDouble();
				}while(exp < 0.0 || exp > 608.0);
				
				Pokemen poke = new Pokemen();
				poke.setName(name);
				poke.setHp(hp);
				poke.setExp(exp);
				poke.setLevel(level);
				
				pokedex.add(poke);
				mc.println("Successfully add a pokemen");
				press();
			}else if(idx == 2) {
				pokedex.view();
				press();
			}else if(idx == 3) {
				sem.value = 0;
				if(pokedex.isEmpty()){
					mc.println("You cannot trade if you didn't have any pokemen!");
					try {
						String receive = net.readMsg();
						net.sendMsg("Empty", Integer.parseInt(receive.split("#")[1]));
						net.isTrading = net.isWaiting = false;
					} catch (Exception e) {
						net.isTrading = net.isWaiting = false;
						continue;
					}
					continue;
				}
				if(!net.isTrading){
					int dst = 0;
					do{
						mc.print("Input pokemen trainer's address : ");
						dst = mc.scanInt();
					}while(dst == net.getAddr());
					net.sendMsg("TradeRequest#" + net.getAddr(), dst);
					
					net.isTrading = true;
					
					System.out.println("Waiting...");
					sem.P();
					String recv = "";
					
					recv = net.readMsg();
					
					Boolean check = !pokedex.isEmpty();
					
					if(check && recv.contains("TradeRequest")){
						choose = 0;
						
						pokedex.view();
						
						net.isWaiting = true;
						do{
							mc.print("Insert pokemen's index [1 - " + pokedex.size() + "]: " );
							choose = mc.scanInt();
						}while(choose < 1 || choose > pokedex.size());
						
						choose--;
						
						Pokemen p1 = pokedex.get(choose);
						
						recv = net.readMsg();
						
						Pokemen p2 = new Pokemen();
						
						if(recv == null) {
							String all = p1.getName() + "#" + p1.getHp() + "#" + 
									p1.getExp() + "#" + p1.getLevel() + "#" + net.getAddr();
							net.sendMsg(all, dst);
							System.out.println("Waiting...");
							sem.P();
							
							recv = net.readMsg();	
							
							p2.setName(recv.split("#")[0]);
							p2.setHp(Integer.parseInt(recv.split("#")[1]));
							p2.setExp(Double.parseDouble(recv.split("#")[2]));
							p2.setLevel(Integer.parseInt(recv.split("#")[3]));
							
						}else {
							
							String all = p1.getName() + "#" + p1.getHp() + "#" + 
									p1.getExp() + "#" + p1.getLevel() + "#" + net.getAddr();
							net.sendMsg(all, dst);
							
							
							p2.setName(recv.split("#")[0]);
							p2.setHp(Integer.parseInt(recv.split("#")[1]));
							p2.setExp(Double.parseDouble(recv.split("#")[2]));
							p2.setLevel(Integer.parseInt(recv.split("#")[3]));
						}
						pokedex.trade(choose, p2);
						
					}else if(recv.equals("Rejected")){
						mc.println("The trainer rejected your request :'(");
						net.isWaiting = net.isTrading = false;
						continue;
					}else if(recv.equals("Empty")) {
						mc.println("The specific trainer's with that address doesn't have pokemens!");
						net.isWaiting = net.isTrading = false;
						continue;
					}
				}else if(net.isTrading){
					
					String recv = net.readMsg();
					
					String acc = "";
					
					int dst_sender = Integer.parseInt(recv.split("#")[1]);
							
					do{
						mc.print("Are you accepting this request? (yes | no) (case insensitive) : ");
						acc = mc.scan();
					}while(!acc.equalsIgnoreCase("yes") && !acc.equalsIgnoreCase("no"));
					
					if(acc.equalsIgnoreCase("yes")){
						if(!pokedex.isEmpty())
							net.sendMsg("TradeRequest#" + net.getAddr(), Integer.parseInt(recv.split("#")[1]));
						else if(pokedex.isEmpty()) {
							mc.println("You cannot trade if you didn't have a pokemen!");
							net.sendMsg("Empty#" + net.getAddr(), Integer.parseInt(recv.split("#")[1]));
							net.isTrading = net.isWaiting = false;
							continue;
						}
						pokedex.view();
						
						net.isWaiting = true;
						
						do{
							mc.print("Insert pokemen's index [1 - " + pokedex.size() + "]: " );
							choose = mc.scanInt();
						}while(choose < 1 || choose > pokedex.size());
						
						choose--;
						
						Pokemen p1 = pokedex.get(choose);
						
						recv = net.readMsg();
						Pokemen p2 = new Pokemen();
						
						if(recv == null) {
							net.isWaiting = true;
							String all = p1.getName() + "#" + p1.getHp() + "#" + 
									p1.getExp() + "#" + p1.getLevel() + "#";
							net.sendMsg(all, dst_sender);
							System.out.println("Waiting...");
							sem.P();
							net.isWaiting = false;
							recv = net.readMsg();
							
							p2.setName(recv.split("#")[0]);
							p2.setHp(Integer.parseInt(recv.split("#")[1]));
							p2.setExp(Double.parseDouble(recv.split("#")[2]));
							p2.setLevel(Integer.parseInt(recv.split("#")[3]));
							
						}else {
							String all = p1.getName() + "#" + p1.getHp() + "#" + 
									p1.getExp() + "#" + p1.getLevel() + "#";
							net.sendMsg(all, dst_sender);
							
							p2.setName(recv.split("#")[0]);
							p2.setHp(Integer.parseInt(recv.split("#")[1]));
							p2.setExp(Double.parseDouble(recv.split("#")[2]));
							p2.setLevel(Integer.parseInt(recv.split("#")[3]));
						}
						pokedex.trade(choose, p2);
						
					}else{
						net.sendMsg("Rejected", Integer.parseInt(recv.split("#")[1]));
						net.isTrading = net.isWaiting = false;
					}
				}
			}
		}while(idx != 4);
		mc.println("Ticks of time : " + Machine.timer().getTime());
	}
}
