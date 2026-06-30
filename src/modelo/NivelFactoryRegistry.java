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
        factories.put("TANGO", new NivelTangoFactory());
        factories.put("MARIACHI", new NivelMariachiFactory());
        factories.put("REGGAE", new NivelReggaeFactory());
        factories.put("METALICA", new NivelMetalicaFactory());
        factories.put("SAMBA", new NivelSambaFactory());

    }

    public NivelFactory obtenerFactory(String genero) {
        return factories.getOrDefault(genero, new NivelRockFactory());
    }
}