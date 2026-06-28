package modelo;

import java.util.HashMap;
import java.util.Map;

//Es para tener un registro de fábricas. Esto sirve para no poner if ni switch en el controller.
public class NivelFactoryRegistry {

    private Map<String, NivelFactory> factories;

    public NivelFactoryRegistry() {
        factories = new HashMap<>();

        factories.put("ROCK", new NivelRockFactory());
        factories.put("JAZZ", new NivelJazzFactory());
    }

    public NivelFactory obtenerFactory(String genero) {
        return factories.getOrDefault(genero, new NivelRockFactory());
    }
}