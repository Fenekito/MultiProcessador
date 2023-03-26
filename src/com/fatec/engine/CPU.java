package com.fatec.engine;

import java.util.ArrayList;
import java.util.Comparator;

import com.fatec.engine.interfaces.Administravel;
import com.fatec.engine.interfaces.Atualizavel;
import com.fatec.engine.interfaces.FilaHandler;

public class CPU implements Administravel<Atualizavel>, FilaHandler, Atualizavel {

	private Fila[] filas = new Fila[5];
	private Fila filaAtual;
	private Processo processoAtual;
	
	public CPU() {
		//TODO: preencher array com todos os tipos de filas
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

		boolean filaAtualExiste = filaAtual != null;
		boolean filaAtualEstaRodando = filaAtual.estaRodando();
		boolean filaAtualEDiferenteDeNovaFila = !filaAtual.equals(proximaFila);
		if (filaAtualExiste && filaAtualEstaRodando && filaAtualEDiferenteDeNovaFila) {
			filaAtual.pausar();
		}

		filaAtual = proximaFila;
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
		processoAtual = processo;
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
