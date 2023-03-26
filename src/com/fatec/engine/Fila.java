package com.fatec.engine;

import java.util.ArrayList;
import java.util.UUID;

import com.fatec.engine.interfaces.Administravel;
import com.fatec.engine.interfaces.FilaHandler;
import com.fatec.engine.interfaces.ProcessoHandler;

public abstract class Fila implements Administravel<Void>, ProcessoHandler {

	private FilaHandler _handler;
	protected Thread threadAtual;
	protected ArrayList<Processo> processos;
	protected boolean rodando; 
	public UUID id;

	public Fila(FilaHandler handler) {
		id = UUID.randomUUID();
		_handler = handler;
		rodando = false;
		processos = new ArrayList<Processo>();
	}

	public void iniciar() {
		rodando = true;
		executarProximoProcesso();
	}

	public void pausar() {
		rodando = false;
		threadAtual.interrupt();
		threadAtual = null;
	}
	
	public boolean estaRodando() {
		return rodando;
	}

	protected void executar(Processo processo) {
		_handler.onNovoProcesso(processo);
		threadAtual = new Thread(processo);
		threadAtual.start();
	}

	//identifica qual será o próximo processo e executa ele
	public void executarProximoProcesso() {
		Processo proximoProcesso = getProximoProcesso();

		//caso nenhum próximo processo seja encontrado, avisa o FilaHandler injetado que não há mais processos na fila e retorna
		if (proximoProcesso == null) {
			_handler.onEsvaziada();
			return;
		}

		executar(proximoProcesso);
	}
		
	public static <T1 extends Fila, T2 extends Fila> T2 trocarAlgoritmo(T1 fila, T2 novaFila) {
		novaFila.addProcessos(fila.getProcessos());
		return novaFila;
	}
	
	@Override
	public boolean equals(Object objeto) {
		if (objeto == null) {
            return false;
        }

        if (!(objeto instanceof Fila)) {
            return false;
        }

        Fila fila = (Fila)objeto;
        return fila.id.equals(id);
	}

	@Override
	public void onFinalizado(Processo processo) {
		//vai remover o processo atual
		processos.remove(processo);

		//caso haja um próximo processo, executa ele
		executarProximoProcesso();
	}

	@Override
	public Void addProcesso(Processo processo) {
		processos.add(processo);
		return null;
	}

	@Override
	public Void addProcessos(ArrayList<Processo> processos) {
		this.processos.addAll(processos);
		return null;
	}

	@Override
	public int countProcessos() {
		return processos.size();
	}

	@Override
	public long countTempoRestante() {
		long tempoRestanteAcumulado = 0;

		for (Processo processo : processos) {
			tempoRestanteAcumulado += processo.getTempoRestante();
		}

		return tempoRestanteAcumulado;
	}

	@Override
	public ArrayList<Processo> getProcessos() {
		return processos;
	}

	//vai decidir o próximo processo que deve ser executado de acordo com o algoritmo da fila
	protected abstract Processo getProximoProcesso();

}
