package com.fatec.engine;

public class Processo implements Runnable {
	protected Thread thread;
        private CPU parent;
        
	public Processo(CPU cpu) {
            parent = cpu;
            start();
	}
	
	@Override
	public void run() {
		try {
			int index = Simulacao.cpuIndex;
			thread.sleep(5000);
                        if(!parent.processqueue.isEmpty()) {
                            if(parent.curProcess != null && parent.curProcess == this) {
                                parent.processqueue.remove(this);
                                parent.curProcess = null;
                                thread.join();
                            }
                        }
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void start() {
		thread = new Thread(this);
		thread.start();
	}
}
