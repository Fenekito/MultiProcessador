package com.fatec.engine;

public class Processo implements Runnable {
	protected Thread thread;
	public void Processo() {
		start();
	}
	
	@Override
	public void run() {
		try {
			int index = Simulacao.cpuIndex;
			System.out.println("CPU " + index + ":" + " Iniciou um Processo");
			thread.sleep(1000);
			System.out.println("CPU " + index + ":" + " Finalizou um Processo");
			if(CPU.curProcess == this) {
				CPU.processqueue.remove(this);
				CPU.curProcess = null;
				thread.join();
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
