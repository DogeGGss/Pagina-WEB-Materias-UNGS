package com.universidad.demo.utils;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.awt.Desktop;
import java.net.URI;

@Component
public class BrowserLauncher implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        // URL que deseas abrir
        String url = "http://localhost:8080";

        // Verifica si el escritorio está soportado
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                desktop.browse(new URI(url));
                System.out.println("Navegador abierto en: " + url);
            }
        } else {
            System.out.println("El sistema no soporta abrir el navegador automáticamente.");
        }
    }
}