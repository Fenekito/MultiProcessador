package com.fatec.engine.filas;

import java.util.Timer;
import java.util.TimerTask;

import com.fatec.engine.Fila;
import com.fatec.engine.Processo;
import com.fatec.engine.enums.Prioridade;
import com.fatec.engine.interfaces.FilaHandler;

public class FilaRR extends Fila {

	private Timer clock = new Timer();

	private class _clockTask extends TimerTask {
		@Override
		public void run() {
			executarProximoProcesso();
		}
	}

	public long quantum = 5;

	public FilaRR(FilaHandler handler, Prioridade prioridade) {
		super(handler, prioridade);
	}
	
	public FilaRR(FilaHandler handler, Prioridade prioridade, long quantum) {
		super(handler, prioridade);
		this.quantum = quantum;
	}

	private void resetarClock() {
		clock.cancel();
		clock = new Timer();
		clock.scheduleAtFixedRate(new _clockTask(), quantum, quantum);
	}

	@Override
	public void iniciar() {
		super.iniciar();
		
		resetarClock();
	}

	@Override
	public void pausar() {
		super.pausar();

		clock.cancel();
	}

	@Override
	public void onFinalizado(Processo processo) {
		super.onFinalizado(processo);

		resetarClock();
	}

	@Override
	protected Processo getProximoProcesso() {
		//se não houverem processos, retorna null
		if (processos.size() == 0) {
			return null;
		}

		//calcula o índice do próximo processo no arraylist processos
		int indexProcessoAtual = processos.indexOf(processoAtual);
		int quantidadeProcessos = processos.size();
		int indexProximoProcesso = (indexProcessoAtual + 1) % quantidadeProcessos;

		return processos.get(indexProximoProcesso);
	}
}
