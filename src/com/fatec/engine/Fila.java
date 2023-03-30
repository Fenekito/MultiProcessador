package com.fatec.engine;

import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import com.fatec.engine.enums.Prioridade;
import com.fatec.engine.interfaces.Administravel;
import com.fatec.engine.interfaces.FilaHandler;
import com.fatec.engine.interfaces.ProcessoHandler;

public abstract class Fila implements Administravel<Void>, ProcessoHandler {

	protected FilaHandler _handler;
	protected Thread threadAtual;
	protected Processo processoAtual;
	protected CopyOnWriteArrayList<Processo> processos;
	protected boolean rodando; 
	public UUID id;
	public Prioridade prioridade;

	public Fila(FilaHandler handler, Prioridade prioridade) {
		id = UUID.randomUUID();
		_handler = handler;
		this.prioridade = prioridade;
		rodando = false;
		processos = new CopyOnWriteArrayList<Processo>();
	}

	public void iniciar() {
		rodando = true;
		executarProximoProcesso();
	}

	public void pausar() {
		rodando = false;

		if (threadAtual != null) {
			threadAtual.interrupt();
			threadAtual = null;
		}
	}
	
	public boolean estaRodando() {
		return rodando;
	}

	protected synchronized void executar(Processo processo) {
		processoAtual = processo;
		
		//se houver uma thread em execução, interrompe ela antes de executar a próxima
		if (threadAtual != null) {
			threadAtual.interrupt();
		}
		
		threadAtual = new Thread(processo);
		threadAtual.start();
		
		_handler.onNovoProcesso(processo);
	}

	//identifica qual será o próximo processo e executa ele
	public synchronized void executarProximoProcesso() {
		Processo proximoProcesso = getProximoProcesso();

		//caso nenhum próximo processo seja encontrado, avisa o FilaHandler injetado que não há mais processos na fila e retorna
		if (proximoProcesso == null) {
			_handler.onEsvaziada();
			return;
		}

		//se o proximoProcesso for o processoAtual, não há nada a ser feito, portanto, retorna
		if (proximoProcesso.equals(processoAtual)) {
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
	public synchronized void onFinalizado(Processo processo) {
		_handler.onProcessoFinalizado(processo);
		
		//vai remover o processo atual
		processos.remove(processo);

		//caso haja um próximo processo, executa ele
		executarProximoProcesso();
	}

	@Override
	public synchronized void onInterrompido(Processo processo) {
		//o trabalho do algoritmo é justamente não deixar de executar processos,
		//e justamente por isso, é necessário executar um novo processo sempre que haja um interrupção, independente da causa dela
		if (rodando) {
			executarProximoProcesso();
		}
	}

	@Override
	public synchronized Void addProcesso(Processo processo) {
		processo.setHandler(this);
		processos.add(processo);
		return null;
	}

	@Override
	public synchronized Void addProcessos(CopyOnWriteArrayList<Processo> processos) {
		for (Processo processo : processos) {
			addProcesso(processo);
		}
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
	public synchronized CopyOnWriteArrayList<Processo> getProcessos() {
		return processos;
	}

	//vai decidir o próximo processo que deve ser executado de acordo com o algoritmo da fila
	protected abstract Processo getProximoProcesso();

}
