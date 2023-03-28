package com.fatec.engine.filas;

import java.util.Comparator;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

import com.fatec.engine.Fila;
import com.fatec.engine.Processo;
import com.fatec.engine.interfaces.FilaHandler;
import com.fatec.engine.enums.Prioridade;

public class FilaPrioridades extends Fila {

	private Timer envelhecimento = new Timer();

	public FilaPrioridades(FilaHandler handler, Prioridade priority) {
		super(handler, priority);
	}

	@Override
	protected Processo getProximoProcesso() {
		Processo proximoProcesso = null;

		envelhecimento.purge();
		envelhecimento.cancel();
		if(processos.size() > 0) {
			//Ordena a fila por tempo de processo
			Collections.sort(processos, maiorPrioridade);
			proximoProcesso = processos.get(0);
		}

		envelhecimento.schedule(new Envelhecer(), 20000);
		return proximoProcesso;
	}

	@Override
	public Void addProcesso(Processo processo) {
		super.addProcesso(processo);

		/* Adiciona o Processo novo a fila
		* Executar Próximo processo já verifica a fila e como ela será reordenada por tempo
		* não há necessidade de manualmente interromper o processo
		*/
		executarProximoProcesso();

		return null;
	}

	public void subirPrioridade() {
		for(Processo processo : processos) {
			if (processo != processoAtual) {
				processo.prioridade = prioridade.getNext();
			}
			getProximoProcesso();
		}
	}

	@Override
	public Void addProcessos(CopyOnWriteArrayList<Processo> processos) {
		super.addProcessos(processos);

		Collections.sort(processos, maiorPrioridade);
		return null;
	}

	/*Compara a prioridade dada do processo atual
	* Ou seja um processo do sistema operacional ficará acima na fila de um processo do usuário 
	*/
	public Comparator<Processo> maiorPrioridade = new Comparator<Processo>() {
		
		@Override
		public int compare(Processo p0, Processo p1) {
			if(p0.prioridade.toInt() > p1.prioridade.toInt()) {
				return +1;
			} else {
				return -1;
			}
		}
	};

	class Envelhecer extends TimerTask {

		@Override
		public void run() {
			subirPrioridade();
		}
	}
}
