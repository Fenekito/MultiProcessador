package com.fatec.engine.interfaces;

import com.fatec.engine.Processo;

public interface ProcessoHandler {
  public void onFinalizado(Processo processo);
  public void onInterrompido(Processo processo);
}
