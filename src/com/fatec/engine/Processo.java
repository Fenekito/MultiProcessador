package com.fatec.engine;

import java.util.Comparator;

public class Processo implements Runnable {
	protected Thread thread;
	
	private CPU parent;

	public int tempoEstimado;
	
	enum Priority {
		OS,
		INTERACTIVE,
		INTERACTIVE_EDITION,
		BATCH,
		SECONDARY
	}
	
	public Priority prioridade = Priority.SECONDARY;
        
	public Processo(CPU cpu) {
		parent = cpu;
		start();
	}
	
	public static Comparator<Processo> priorityComparator = new Comparator<Processo>() {
		
		@Override
		public int compare(Processo p0, Processo p1) {
			if(p0.prioridade.compareTo(p1.prioridade) > 0){
				return 1;
			} else {
				return -1;
			}
		}
	};
	
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
