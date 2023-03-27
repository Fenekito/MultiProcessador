package com.fatec.engine;

import com.fatec.engine.enums.Prioridade;
import com.fatec.engine.interfaces.CPUHandler;

/**
* @author Fenekito
*/

public class Simulacao {
	private static class Logger implements CPUHandler {
		@Override
		public void onNovaFila(Fila fila, CPU cpu) {
			String msg = String.format("[nova_fila] CPU.id: %s, Fila.id: %s, Fila.prioridade: %s", cpu.id.toString(), fila.id.toString(), fila.prioridade.toString());
			System.out.println(msg);
		}

		@Override
		public void onNovoProcesso(Processo processo, CPU cpu) {
			String msg = String.format("[novo_processo] CPU.id: %s, Processo.id: %s, Processo.prioridade: %s", cpu.id.toString(), processo.id.toString(), processo.prioridade.toString());
			System.out.println(msg);
		}
	}

    public static void main(String[] args) {
		Logger logger = new Logger();
		CPUController controller = new CPUController(logger, 4);
		Prioridade prioridade = Prioridade.SECONDARY;

		//adiciona 1000 processos, com um tempo de execução variando de 1 a 1000 e prioridade variavel
		for (int i = 1; i <= 1000; i++) {
			Processo processo = new Processo(i);
			processo.prioridade = prioridade;

			controller.addProcesso(processo);
			
			prioridade = prioridade.tiltNext();
		}
	}
}
