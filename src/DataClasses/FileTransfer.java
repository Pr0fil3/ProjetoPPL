/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DataClasses;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;

/**
 *
 * @author -nikeiZprooo-
 */
public class FileTransfer implements Serializable{
    private final byte[] ficheiro;
    private final String titulo;

    public FileTransfer(File path) throws IOException {
        this.ficheiro = Files.readAllBytes(path.toPath());
        this.titulo = path.getName();
    }

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

    public String getTitulo() {
        return titulo;
    }
}
