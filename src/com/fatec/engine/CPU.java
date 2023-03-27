package com.fatec.engine;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.UUID;

import com.fatec.engine.enums.Prioridade;
import com.fatec.engine.filas.FilaFCFS;
import com.fatec.engine.filas.FilaRR;
import com.fatec.engine.filas.FilaSJF;
import com.fatec.engine.interfaces.Administravel;
import com.fatec.engine.interfaces.Atualizavel;
import com.fatec.engine.interfaces.CPUHandler;
import com.fatec.engine.interfaces.FilaHandler;

public class CPU implements Administravel<Atualizavel>, FilaHandler, Atualizavel {

	private Fila[] filas = new Fila[5];
	private Fila filaAtual;
	private CPUHandler _handler;
	public UUID id;
	
	public CPU(CPUHandler handler) {
		_handler = handler;
		id = UUID.randomUUID();
		filas[Prioridade.OS.toInt()] = new FilaSJF(this, Prioridade.OS);
		filas[Prioridade.INTERACTIVE.toInt()] = new FilaRR(this, Prioridade.INTERACTIVE);
		filas[Prioridade.INTERACTIVE_EDITION.toInt()] = new FilaRR(this, Prioridade.INTERACTIVE_EDITION);
		filas[Prioridade.BATCH.toInt()] = new FilaFCFS(this, Prioridade.BATCH);
		filas[Prioridade.SECONDARY.toInt()] = new FilaFCFS(this, Prioridade.SECONDARY);
	}

	public static Comparator<CPU> tempoRestanteComparator = new Comparator<CPU>() {
		@Override
		public int compare(CPU cpu0, CPU cpu1) {
			long diferenca =  cpu0.countTempoRestante() - cpu1.countTempoRestante();
			
			return diferenca != 0
				? (int)(diferenca / Math.abs(diferenca))
				: 0;
		}
	};	

	@Override
	public boolean equals(Object objeto) {
		if (objeto == null) {
			return false;
		}

		if (!(objeto instanceof CPU)) {
			return false;
		}

		CPU cpu = (CPU)objeto;
		return cpu.id.equals(id);
	}

	//método que checa a quantidade de processos em cada fila e define qual deve ser executada
	@Override
	public void atualizar() {
		Fila proximaFila = null;

		for (Fila fila : filas) {
			int countProcessos = fila.countProcessos();

			if (countProcessos > 0) {
				proximaFila = fila;

				break;
			}
		}

		if (proximaFila == null) {
			filaAtual.pausar();
			return;
		}

		//se a proximaFila já é a filaAtual e ela já está rodando, não faz nada
		if (proximaFila.equals(filaAtual) && filaAtual.estaRodando()) {
			return;
		}
		
		//se a filaAtual estiver rodando, ela será pausada
		if (filaAtual != null && filaAtual.estaRodando()) {
			filaAtual.pausar();
		}
		
		//se a proximaFila é diferente da filaAtual, atribui filaAtual a proximaFila
		if (!proximaFila.equals(filaAtual)) {
			filaAtual = proximaFila;
		}

		//avisa o handler que uma nova fila foi criada
		_handler.onNovaFila(proximaFila, this);

		proximaFila.iniciar();
	}

	//método chamado pelas filas
	@Override
	public void onEsvaziada() {
		//define qual será a próxima fila a ser executada
		atualizar();
	}

	//método chamado pelas filas quando um novo processo é rodado
	@Override
	public void onNovoProcesso(Processo processo) {
		_handler.onNovoProcesso(processo, this);
	}

	@Override
	public long countTempoRestante() {
		long tempoRestanteAcumulado = 0;

		for (Fila fila : filas) {
			tempoRestanteAcumulado += fila.countTempoRestante();
		}

		return tempoRestanteAcumulado;
	}

	@Override
	public int countProcessos() {
		int contagem = 0;

		for (Fila fila : filas) {
			contagem += fila.countProcessos();
		}

		return contagem;
	}

	@Override
	public ArrayList<Processo> getProcessos() {
		ArrayList<Processo> processos = new ArrayList<Processo>();

		for (Fila fila : filas) {
			processos.addAll(fila.getProcessos());
		}

		return processos;
	}

	@Override
	public Atualizavel addProcesso(Processo processo) {
		filas[processo.prioridade.toInt()].addProcesso(processo);
		return this;
	}

	@Override
	public Atualizavel addProcessos(ArrayList<Processo> processos) {
		for (Processo processo : processos) {
			addProcesso(processo);
		}
		
		return this;
	}
}
