package com.fx.ec.pojo;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

/**
 *
 * @author pscha
 */
public class FileTreeItem {

    private File file;
    private String fileName;
    private String size;
    private BooleanProperty active = new SimpleBooleanProperty();
    private String endian;

    public FileTreeItem(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFileName() {
        return file.getName();
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSize() {
        return file.length() + "";
    }

    public void setSize(String size) {
        this.size = size;
    }

    public boolean isActive() {
        return active.get();
    }

    public void setActive(boolean active) {
        this.active.set(active);
    }

    public BooleanProperty getActiveProperty() {
        return active;
    }

    public String getEndian() {
        return endian;
    }

    public void setEndian(String endian) {
        this.endian = endian;
    }

    @Override
    public String toString() {
        return "FileTreeItem{" + "file=" + file + ", fileName=" + fileName + ", size=" + size + ", active=" + active + '}';
    }
}
