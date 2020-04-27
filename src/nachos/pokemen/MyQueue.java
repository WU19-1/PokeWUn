package nachos.pokemen;

import java.util.Vector;

import nachos.threads.KThread;
import nachos.threads.ThreadQueue;

public class MyQueue extends ThreadQueue{

	Vector<KThread> queue = new Vector<>();
	
	@Override
	public void waitForAccess(KThread thread) {
		queue.add(thread);
	}

	@Override
	public KThread nextThread() {
		if(queue.isEmpty()) return null;
		return queue.remove(0);
	}

	@Override
	public void acquire(KThread thread) {
		
	}

	@Override
	public void print() {
		
	}

}
