
package service;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class ArquivoJson {

    private final Path caminhoArquivo;

    private final Gson gson;

    public ArquivoJson(String caminhoArquivo) {
        this.caminhoArquivo = Paths.get(caminhoArquivo);

        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

   
    public <T> T ler(Class<T> classe) throws IOException {

        if (!Files.exists(caminhoArquivo)) {
            return null;
        }

        try (Reader reader = Files.newBufferedReader(
                caminhoArquivo, StandardCharsets.UTF_8)) {

            return gson.fromJson(reader, classe);
        }
    }


    public void salvar(Object objeto) throws IOException {

        Path diretorio = caminhoArquivo.getParent();

        if (diretorio != null) {
            Files.createDirectories(diretorio);
        }

        try (Writer writer = Files.newBufferedWriter(
                caminhoArquivo,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.WRITE)) {

            gson.toJson(objeto, writer);
        }
    }


    public boolean existeArquivo() {
        return Files.exists(caminhoArquivo);
    }


    public void criarArquivoVazio() throws IOException {

        Path diretorio = caminhoArquivo.getParent();
        if (diretorio != null) {
            Files.createDirectories(diretorio);
        }

        if (!Files.exists(caminhoArquivo)) {
            Files.write(
                    caminhoArquivo,
                    "{}".getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE_NEW,
                    StandardOpenOption.WRITE
            );
        }
    }
}