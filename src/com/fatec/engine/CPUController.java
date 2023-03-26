package com.fatec.engine;

import java.util.ArrayList;

import com.fatec.engine.interfaces.Administravel;

public class CPUController implements Administravel<Void> {
	private ArrayList<CPU> cpus;

	public CPUController(ArrayList<CPU> cpus) {
		this.cpus = cpus;
	}

	private CPU getCPUMenosUsada() {
		cpus.sort(CPU.tempoRestanteComparator);
		return cpus.get(0);
	}

	@Override
	public long countTempoRestante() {
		long tempoRestanteAcumulado = 0;

		for (CPU cpu : cpus) {
			tempoRestanteAcumulado += cpu.countTempoRestante();
		}

		return tempoRestanteAcumulado;
	}

	@Override
	public int countProcessos() {
		int contagem = 0;

		for (CPU cpu : cpus) {
			contagem += cpu.countProcessos();
		}

		return contagem;
	}

	@Override
	public ArrayList<Processo> getProcessos() {
		ArrayList<Processo> processos = new ArrayList<Processo>();

		for (CPU cpu : cpus) {
			processos.addAll(cpu.getProcessos());
		}

		return processos;
	}

	@Override
	public Void addProcesso(Processo processo) {
		getCPUMenosUsada()
			.addProcesso(processo)
			.atualizar();

		return null;
	}

	@Override
	public Void addProcessos(ArrayList<Processo> processos) {
		for (Processo processo : processos) {
			getCPUMenosUsada().addProcesso(processo);
		}

		for (CPU cpu : cpus) {
			cpu.atualizar();
		}

		return null;
	}
}
