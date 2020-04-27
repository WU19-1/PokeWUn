package nachos.pokemen;

import java.util.Vector;

import nachos.threads.KThread;

public class PokeDex extends Vector<Pokemen> {
	
	public PokeDex() {
		
	}
	
	public boolean trade(int idx,Pokemen p2) {
		add(p2);
		new KThread(this.remove(idx)).fork();
		MainSystem.net.isTrading = false;
		MainSystem.net.isWaiting = false;
		return true;
	}
	
	public boolean view() {
		if(this.isEmpty()) {
			System.out.println("There are no pokemens in your pokedex");
			return false;
		}else {
			System.out.println("========================================================");
			System.out.println("| No | Name            | HP  | Level | Experience      |");
			System.out.println("========================================================");
			for(int i = 0; i < this.size(); i++) {
				if(i < 9){
					System.out.print("| 0"+ (i+1) + " |");
				}else{
					System.out.print("| "+ (i+1) + " |");
				}
				System.out.print(" " + this.get(i).getName());
				for(int j = 0 ; j < (15 - this.get(i).getName().length()); j++){
					System.out.print(" ");
				}
				System.out.print(" |");
				
				if(this.get(i).getHp() < 100)
					System.out.print("  "+ this.get(i).getHp() + " |");
				else if (this.get(i).getHp() > 99 )
					System.out.print(" "+ this.get(i).getHp() + " |");
				if(this.get(i).getLevel() < 10)
					System.out.print("     "+this.get(i).getLevel()+" |");
				else if(this.get(i).getLevel() > 9)
					System.out.print("    "+this.get(i).getLevel()+" |");					
				System.out.print(" "+this.get(i).getExp().toString());
				for(int j = 0; j < (15 - this.get(i).getExp().toString().length()); j++)
					System.out.print(" ");
				System.out.println(" |");
			}
			System.out.println("========================================================");
		}
		return true;
	}
	
}
