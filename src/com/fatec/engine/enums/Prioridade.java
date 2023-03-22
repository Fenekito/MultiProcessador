package com.fatec.engine.enums;

public enum Prioridade {
  OS(0),
  INTERACTIVE(1),
  INTERACTIVE_EDITION(2),
  BATCH(3),
  SECONDARY(4);

  private final int codigo;

  private Prioridade(int codigo) {
    this.codigo = codigo;
  }

  public int toInt() {
    return codigo;
  }
}
