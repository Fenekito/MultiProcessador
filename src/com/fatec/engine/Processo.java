package com.fatec.engine;

import java.util.Comparator;
import java.util.UUID;

import com.fatec.engine.enums.Prioridade;
import com.fatec.engine.interfaces.ProcessoHandler;

public class Processo implements Runnable {
	public Prioridade prioridade = Prioridade.SECONDARY;
	public UUID id;
	private long tempoEstimado;
	private long tempoDecorrido;
	private ProcessoHandler _handler;

	public Processo(ProcessoHandler handler, long tempoEstimado) {
		_handler = handler;
		this.tempoEstimado = tempoEstimado;
		tempoDecorrido = 0;
		id = UUID.randomUUID();
	}

	public long getTempoRestante() {
		return tempoEstimado - tempoDecorrido;
	}
	
	public static Comparator<Processo> priorityComparator = new Comparator<Processo>() {
		
		@Override
		public int compare(Processo p0, Processo p1) {
			int prioridadeP0 = p0.prioridade.toInt();
			int prioridadeP1 = p1.prioridade.toInt();

			int diferenca = prioridadeP0 - prioridadeP1;

			return diferenca != 0
				? diferenca / Math.abs(diferenca)
				: 0;
		}

	};

	@Override
	public boolean equals(Object objeto) {
		if (objeto == null) {
            return false;
        }

        if (!(objeto instanceof Processo)) {
            return false;
        }

        Processo processo = (Processo)objeto;
        return processo.id.equals(id);
	}
	
	@Override
	public void run() {
		long timestampInicio = System.nanoTime();

		try {
			Thread.sleep(getTempoRestante());
		} catch (Exception e) {
			long timestampPausa = System.nanoTime();
			long tempoDecorridoAtePausa = timestampPausa - timestampInicio;

			tempoDecorrido += tempoDecorridoAtePausa;

			_handler.onInterrompido(this);
			return;
		}

		long timestampFim = System.nanoTime();
		long tempoDecorridoAteTermino = timestampFim - timestampInicio;
		tempoDecorrido += tempoDecorridoAteTermino;

		_handler.onFinalizado(this);
	}
}
