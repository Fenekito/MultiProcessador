package com.fatec.engine;

import java.util.ArrayList;

import com.fatec.engine.interfaces.Administravel;

public class CPU implements Administravel {

	private Fila[] filas = new Fila[5];
	//TODO: adicionar um atributo que indique o processo atual, ele deve ser modificável pelas filas. NOME SUGERIDO: private Processo processoAtual
	
	public CPU() {
		//TODO: preencher array com todos os tipos de filas
	}

	//TODO: adicionar um event handler que a fila possa chamar assim que os processos dela acabem (possível método delegado) NOME SUGERIDO: onFilaEsvaziada()
	//TODO: para compactuar com o TODO da linha 10. Adicionar outro event handler para a troca de processo atual (ele será responsável por alterar o processo que está no atributo do processo atual) NOME SUGERIDO: onNovoProcessoAtual(Processo novoProcesso)

	/*
		TODO: adicionar uma função que checa qual é a fila com maior prioridade que possui processos. essa função deve identificar tal fila e, se a prioridade atual for diferente da do processo que já está em execução, pausar a fila atual e executar a fila que tenha maior prioridade
	*/

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
	public void addProcesso(Processo processo) {
		/*
			TODO: adicionar decisão por prioridade
			Exemplo: caso um processo de maior prioridade entre, o processo atual deve ser pausado para dar prioridade para o atual
			(nota: chamar o método conceituado na linha 20 deste arquivo)
		*/

		filas[processo.prioridade.toInt()].addProcesso(processo);
	}

	@Override
	public void addProcessos(ArrayList<Processo> processos) {
		for (Processo processo : processos) {
			addProcesso(processo);
		}
	}
}
