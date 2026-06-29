package modelo.estadoCajaGuardaddo;

import modelo.Caja;

public interface EstadoCajaGuardado {
    void restaurar();
    Caja getCaja();
}
