package com.fatec.engine.interfaces;

import com.fatec.engine.Processo;

//a classe que implementar essa interface irá passar uma referência de si própria para um processo.
public interface ProcessoHandler {
	//método que o processo chamará quando estiver finalizado
	public void onFinalizado(Processo processo);
	
	//método que o processo chamará se for interrompido
	public void onInterrompido(Processo processo);
}
