package com.fatec.engine;

import java.util.Comparator;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.fatec.engine.enums.Prioridade;
import com.fatec.engine.interfaces.ProcessoHandler;

public class Processo implements Runnable {
	public Prioridade prioridade = Prioridade.SECONDARY;
	public UUID id;
	private long tempoEstimado;
	private long tempoDecorrido;
	private ProcessoHandler _handler;

	public Processo(long tempoEstimado) {
		this.tempoEstimado = tempoEstimado;
		tempoDecorrido = 0;
		id = UUID.randomUUID();
	}

	public void setHandler(ProcessoHandler handler) {
		_handler = handler;
	}

	public long getTempoRestante() {
		return tempoEstimado - TimeUnit.NANOSECONDS.toMillis(tempoDecorrido);
	}

	public long getTempoEstimado() {
		return tempoEstimado;
	}
	
	public static Comparator<Processo> priorityComparator = new Comparator<Processo>() {
		
		@Override
		public int compare(Processo p0, Processo p1) {
			int prioridadeP0 = p0.prioridade.toInt();
			int prioridadeP1 = p1.prioridade.toInt();

			return Integer.compare(prioridadeP0, prioridadeP1);
		}

	};

	public static Comparator<Processo> tempoRestanteComparator = new Comparator<Processo>() {
		
		@Override
		public synchronized int compare(Processo p0, Processo p1) {
			return Long.compare(p0.getTempoRestante(), p1.getTempoRestante());
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
		long tempoRestante = getTempoRestante();

		try {
			Thread.sleep(tempoRestante);
		} catch (Exception e) {
			long timestampPausa = System.nanoTime();
			long tempoDecorridoAtePausa = timestampPausa - timestampInicio;

			tempoDecorrido += tempoDecorridoAtePausa;

			if (getTempoRestante() > 0) {
				_handler.onInterrompido(this);
			} else {
				_handler.onFinalizado(this);
			}
			return;
		}

		long timestampFim = System.nanoTime();
		long tempoDecorridoAteTermino = timestampFim - timestampInicio;
		tempoDecorrido += tempoDecorridoAteTermino;

		_handler.onFinalizado(this);
	}
}
