package com.fatec.engine.filas;

import java.util.concurrent.CopyOnWriteArrayList;

import com.fatec.engine.Fila;
import com.fatec.engine.Processo;
import com.fatec.engine.interfaces.FilaHandler;
import com.fatec.engine.enums.Prioridade;

public class FilaSJF extends Fila {

	public FilaSJF(FilaHandler handler, Prioridade priority) {
		super(handler, priority);
	}

	@Override
	protected Processo getProximoProcesso() {
		int quantidadeItens = processos.size();

		if (quantidadeItens == 0) {
			return null;
		}

		Processo menor = processos.get(0);

		for (int i = 1; i < quantidadeItens; i++) {
			Processo atual = processos.get(i);

			if (atual.getTempoRestante() < menor.getTempoRestante()) {
				menor = atual;
			}
		}

		return menor;
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

	public Void addProcessos(CopyOnWriteArrayList<Processo> processos) {
		super.addProcessos(processos);
		executarProximoProcesso();
		return null;
	}
}
