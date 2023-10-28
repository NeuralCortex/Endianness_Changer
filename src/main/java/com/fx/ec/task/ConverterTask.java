package com.fx.ec.task;

import com.fx.ec.dialog.ProgressDialog;
import com.fx.ec.pojo.FileNameSize;
import com.fx.ec.pojo.FileTreeItem;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import javafx.concurrent.Task;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TreeItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author pscha
 */
public class ConverterTask extends Task<Integer> {

    private static final Logger _log = LogManager.getLogger(ConverterTask.class);
    private final ProgressDialog progressDialog;
    private boolean stop = false;
    private int c = 0;
    private final TreeItem<FileTreeItem> root;
    private final TreeItem<FileTreeItem> out;
    private int count = 0;
    private final int size;
    private final int nameSize;

    public ConverterTask(ProgressDialog progressDialog, TreeItem<FileTreeItem> root, TreeItem<FileTreeItem> out, ComboBox<FileNameSize> cbBits, ComboBox<FileNameSize> cbNameSize) {
        this.progressDialog = progressDialog;
        this.root = root;
        this.out = out;

        size = cbBits.getSelectionModel().getSelectedItem().getId() / 8;
        nameSize = cbNameSize.getSelectionModel().getSelectedItem().getId();

        countFilesToConvert(root);

        initProgressDlg();
    }

    private void initProgressDlg() {
        progressDialog.getLbLeft().setText("1");
        progressDialog.getProgressBar().progressProperty().bind(progressProperty());
        progressDialog.getLbRight().setText(count + "");
        progressDialog.setProgressDialogInterface(() -> {
            stop = true;
        });
        progressDialog.show();
    }

    @Override
    protected Integer call() throws Exception {
        String oldParent = root.getValue().getFile().getParent();
        String newParent = out.getValue().getFile().getAbsolutePath();

        traverseTree(root, out, oldParent, newParent);

        return 1;
    }

    private void traverseTree(TreeItem<FileTreeItem> root, TreeItem<FileTreeItem> out, String oldParent, String newParent) {

        TreeItem<FileTreeItem> outRoot = new TreeItem(root.getValue());

        if (outRoot.getValue().isActive()) {

            String fileName = outRoot.getValue().getFile().getAbsolutePath().replace(oldParent, newParent);
            File dir = new File(nameSize == 1 ? fileName.toUpperCase() : fileName.toLowerCase());

            outRoot.getValue().setFileName(dir.getName());
            //outRoot.getValue().setFile(dir);
            out.getChildren().add(outRoot);

            dir.mkdir();

            updateProgress(c++, count);
        }

        for (TreeItem<FileTreeItem> child : root.getChildren()) {
            if (child.getChildren().isEmpty()) {

                if (child.getValue().isActive()) {

                    String fileName = child.getValue().getFile().getAbsolutePath().replace(oldParent, newParent);
                    File file = new File(nameSize == 1 ? fileName.toUpperCase() : fileName.toLowerCase());

                    child.getValue().setFileName(file.getName());
                    outRoot.getChildren().add(child);

                    convertFile(child, file);

                    updateProgress(c++, count);
                }

            } else {
                traverseTree(child, outRoot, oldParent, newParent);
            }
        }
    }

    private void convertFile(TreeItem<FileTreeItem> itemInput, File fileOutput) {
        if (itemInput != null) {
            File fileInput = itemInput.getValue().getFile();

            try {
                DataInputStream dataInputStream = new DataInputStream(new FileInputStream(fileInput));
                byte input[] = dataInputStream.readAllBytes();
                dataInputStream.close();

                byte[] buffer = new byte[input.length];

                for (int k = 0; k < input.length; k += size) {

                    byte[] part = partition(input, 0 + k, size);

                    for (int i = 0; i < part.length; i++) {
                        buffer[k + i] = part[part.length - 1 - i];
                    }
                }

                DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(fileOutput));
                dataOutputStream.write(buffer);

                dataOutputStream.close();

            } catch (Exception ex) {
                _log.error(ex.getMessage());
            }
        }
    }

    private byte[] partition(byte[] a, int start, int length) {
        //System.out.println(start+" "+length);
        byte[] b = new byte[length];
        for (int k = 0; k < length; k++) {
            b[k] = a[start + k];
        }
        return b;
    }

    private void countFilesToConvert(TreeItem<FileTreeItem> root) {
        TreeItem<FileTreeItem> outRoot = new TreeItem(root.getValue());

        if (outRoot.getValue().isActive()) {
            count++;
        }

        for (TreeItem<FileTreeItem> child : root.getChildren()) {
            if (child.getChildren().isEmpty()) {
                if (child.getValue().isActive()) {
                    count++;
                }
            } else {
                countFilesToConvert(child);
            }
        }
    }

    @Override
    protected void succeeded() {
        progressDialog.closeDialog();
    }
}
