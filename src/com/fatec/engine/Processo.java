package com.fatec.engine;

import java.util.Comparator;
import java.util.UUID;

import com.fatec.engine.enums.Prioridade;
import com.fatec.engine.interfaces.ProcessoHandler;

public class Processo implements Runnable {
	protected Thread thread;
	public int tempoEstimado;
	public Prioridade prioridade = Prioridade.SECONDARY;
	public UUID id;
	private ProcessoHandler _handler;

	public Processo(ProcessoHandler handler, int tempoEstimado) {
		_handler = handler;
		this.tempoEstimado = tempoEstimado;
		id = UUID.randomUUID();
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
			thread.sleep(tempoEstimado);
			thread.join();
		} catch (Exception e) {
			_handler.onInterrompido(this);
		}

		_handler.onFinalizado(this);
	}
	
	public synchronized void start() {
		thread = new Thread(this);
		thread.start();
	}
}
