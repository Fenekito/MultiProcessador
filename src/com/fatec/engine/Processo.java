package com.fatec.engine;

import java.util.Comparator;

import com.fatec.engine.enums.Prioridade;

public class Processo implements Runnable {
  //TODO: tornar essa classe pausável, de uma maneira em que a fila possa controlar quando o processo roda ou não

  //TODO: receber um event handler de Fila (um possível delegado) que será executado assim que o processo terminar. NOME SUGERIDO: onProcessoFinalizado()
	protected Thread thread;
	
	private CPU parent;

	public int tempoEstimado;
	
	public Prioridade prioridade = Prioridade.SECONDARY;
        
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
    //TODO: remover o código desnecessário (a checagem de processqueue e etc.)
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
