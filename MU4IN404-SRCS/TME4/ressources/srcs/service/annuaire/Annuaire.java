package srcs.service.annuaire;

import srcs.service.Service;

import java.io.IOException;
import java.net.Socket;

public interface Annuaire {
    public String lookup(String nom);
    public void bind(String name, String value);
    public void unbind(String name);
}
