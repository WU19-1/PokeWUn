package nachos.pokemen;

public class Pokemen implements Runnable{

	private String name;
	private Integer hp;
	private Integer level;
	private Double exp;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getHp() {
		return hp;
	}

	public void setHp(Integer hp) {
		this.hp = hp;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Double getExp() {
		return exp;
	}

	public void setExp(Double exp) {
		this.exp = exp;
	}

	@Override
	public void run() {
		System.out.print("Trading");
		for(int i = 0 ; i < 3; i++) {
			try {
				Thread.sleep(500);
				System.out.print(" .");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("");
		System.out.println("Successfully trade pokemen");
	}

}
