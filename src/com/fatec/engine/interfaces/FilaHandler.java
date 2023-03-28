package com.fatec.engine.interfaces;

import com.fatec.engine.Processo;

//a classe que implementar essa interface irá passar uma referência de si própria para uma fila.
public interface FilaHandler {
	//método que a fila chamará sempre que seus processos acabarem
	public void onEsvaziada();
  
	//método que a fila chamará sempre que ela iniciar a execução de um novo processo
	public void onNovoProcesso(Processo processo);
	
	//método que a fila chamará sempre que um processo dentro dela for finalizado
	public void onProcessoFinalizado(Processo processo);
}
