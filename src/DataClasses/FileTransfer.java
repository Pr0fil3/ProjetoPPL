package DataClasses;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;

/**
 * Classe serializável.
 * Utilizada para transferir ficheiros entre as várias aplicações.
 */
public class FileTransfer implements Serializable{
    private final byte[] ficheiro;
    
    /**
     * Guarda o nome do ficheiro
     */
    private final String titulo;

    /**
     * Construtor.
     * Os objectos são construidos com o byte[] do ficheiros
     * indicados no construtor.
     * 
     * @param path - File a ser transferido
     * @throws IOException - Se o File não fôr válido.
     */
    public FileTransfer(File path) throws IOException {
        this.ficheiro = Files.readAllBytes(path.toPath());
        this.titulo = path.getName();
    }

    /**
     * Permite gravar o byte[] no local especificado pelo parametro de entrada.
     * 
     * @param file - File com o local onde criar o ficheiro
     * @return falso se o input for null
     * @throws FileNotFoundException
     * @throws IOException Se não for possivel criar o ficheiro
     */
    public boolean gravar(File file) throws FileNotFoundException, IOException{
        if (file != null){
            file.getParentFile().mkdirs();
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(ficheiro);
            fos.close();
            return true;
        } else return false;
    }
    
    /**
     * 
     * @return O nome do ficheiro
     */
    public String getTitulo() {
        return titulo;
    }
}
