package service;

import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;
import javax.swing.SwingUtilities;

public class IniciarDisputa {

    public IniciarDisputa(String meuTime) {
        Runnable executarRodada = () -> {
            RodadasService rodadasService = new RodadasService();
            DadosService dadosService = new DadosService();
            rodadasService.fazerRodada(dadosService.lerDados(), meuTime);
        };

        if (SwingUtilities.isEventDispatchThread()) {
            Thread thread = new Thread(executarRodada, "iniciar-disputa");
            thread.start();
            return;
        }
        executarRodada.run();
    }
    
    public static void main(String[] args) {

        String meuTime = "Godoiense ";
        RodadasService rodadasService = new RodadasService();
        DadosService dadosService = new DadosService();
        rodadasService.fazerRodada(dadosService.lerDados(), meuTime);
        
    }
}
