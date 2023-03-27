package com.fatec.engine.filas;

import java.util.Comparator;
import java.util.ArrayList;

import com.fatec.engine.Fila;
import com.fatec.engine.Processo;
import com.fatec.engine.interfaces.FilaHandler;
import com.fatec.engine.enums.Prioridade;

public class FilaSJF extends Fila {

	public FilaSJF(FilaHandler handler, Prioridade priority) {
		super(handler, priority);
	}

	@Override
	protected synchronized Processo getProximoProcesso() {
		Processo proximoProcesso = null;

		if(processos.size() > 0) {
			//Ordena a fila por tempo de processo
			processos.sort(menorTempo);
			proximoProcesso = processos.get(0);
		}

		return proximoProcesso;
	}

	public Void addProcesso(Processo processo) {
		super.addProcesso(processo);

		/* Adiciona o Processo novo a fila
		* Executar Próximo processo já verifica a fila e como ela será reordenada por tempo
		* não há necessidade de manualmente interromper o processo
		*/
		executarProximoProcesso();

		return null;
	}

	public Void addProcessos(ArrayList<Processo> processos) {
		super.addProcessos(processos);

		executarProximoProcesso();
		return null;
	}

	public Comparator<Processo> menorTempo = new Comparator<Processo>() {
		
		@Override
		public synchronized int compare(Processo p0, Processo p1) {
			if(p0.getTempoRestante() < p1.getTempoRestante()) {
				return +1;
			} else {
				return -1;
			}
		}
	};
}
