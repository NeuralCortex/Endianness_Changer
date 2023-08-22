package com.fx.ec.controller.tabs;

import com.fx.ec.Globals;
import com.fx.ec.controller.MainController;
import com.fx.ec.controller.PopulateInterface;
import com.fx.ec.dialog.ProgressDialog;
import com.fx.ec.pojo.FileNameSize;
import com.fx.ec.pojo.FileTreeItem;
import com.fx.ec.task.ConverterTask;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.CheckBoxTreeTableCell;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;

/**
 *
 * @author pscha
 */
public class ChangeController implements Initializable, PopulateInterface {

    @FXML
    private Button btnLoadDir;
    @FXML
    private Button btnSaveDir;
    @FXML
    private HBox hBoxOben;
    @FXML
    private BorderPane borderPaneCenter;
    @FXML
    private GridPane gridPane;
    @FXML
    private ComboBox<FileNameSize> cbBits;
    @FXML
    private ComboBox<FileNameSize> cbUpper;

    private final MainController mainController;
    private TreeTableView<FileTreeItem> ttvInput;
    private TreeTableView<FileTreeItem> ttvOutput;
    private int count = 0;

    public ChangeController(MainController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void initialize(URL url, ResourceBundle bundle) {

        hBoxOben.setId("hec-background-blue");

        btnLoadDir.setText(bundle.getString("dir.open"));
        btnSaveDir.setText(bundle.getString("dir.save"));

        cbBits.getItems().clear();
        List<FileNameSize> bitsList = new ArrayList<>();
        for (int i = 1; i < 9; i++) {
            bitsList.add(new FileNameSize(i * 8, i * 8 + " Bits"));
        }
        cbBits.setItems(FXCollections.observableArrayList(bitsList));

        cbUpper.getItems().clear();
        cbUpper.getItems().add(new FileNameSize(1, bundle.getString("to.upper")));
        cbUpper.getItems().add(new FileNameSize(2, bundle.getString("to.lower")));

        ttvInput = new TreeTableView();
        ttvOutput = new TreeTableView();

        VBox vBoxInput = new VBox();
        VBox vBoxOutput = new VBox();

        VBox.setVgrow(ttvInput, Priority.ALWAYS);
        VBox.setVgrow(ttvOutput, Priority.ALWAYS);

        HBox hBoxInput = new HBox();
        HBox hBoxOutput = new HBox();
        HBox spacerInput = new HBox();
        HBox spacerOutput = new HBox();

        HBox.setHgrow(spacerInput, Priority.ALWAYS);
        HBox.setHgrow(spacerOutput, Priority.ALWAYS);

        hBoxInput.setAlignment(Pos.CENTER_LEFT);
        hBoxOutput.setAlignment(Pos.CENTER_LEFT);

        hBoxInput.setSpacing(10);
        hBoxOutput.setSpacing(10);

        hBoxInput.setId("hec-background-blue");
        hBoxOutput.setId("hec-background-blue");

        hBoxInput.setPadding(new Insets(10));
        hBoxOutput.setPadding(new Insets(10));

        Label lbInput = new Label("Input");
        Label lbOutput = new Label("Output");

        CheckBox cbAll = new CheckBox(bundle.getString("sel.all"));
        CheckBox cbExpandInput = new CheckBox(bundle.getString("tree.expand"));
        CheckBox cbExpandOutput = new CheckBox(bundle.getString("tree.expand"));

        cbAll.setId("hec-text-white");
        cbExpandInput.setId("hec-text-white");
        cbExpandOutput.setId("hec-text-white");

        lbInput.setId("hec-text-white");
        lbOutput.setId("hec-text-white");

        hBoxInput.getChildren().addAll(lbInput, spacerInput, cbAll, cbExpandInput);
        hBoxOutput.getChildren().addAll(lbOutput, spacerOutput, cbExpandOutput);

        VBox.setMargin(ttvInput, new Insets(10, 0, 0, 0));
        VBox.setMargin(ttvOutput, new Insets(10, 0, 0, 0));

        vBoxInput.getChildren().addAll(hBoxInput, ttvInput);
        vBoxOutput.getChildren().addAll(hBoxOutput, ttvOutput);

        gridPane.addRow(0, vBoxInput, vBoxOutput);

        TreeTableColumn<FileTreeItem, String> colInputName = new TreeTableColumn<>(bundle.getString("tree.struct"));
        TreeTableColumn<FileTreeItem, String> colInputSize = new TreeTableColumn<>(bundle.getString("size.byte"));
        TreeTableColumn<FileTreeItem, Boolean> colInputActive = new TreeTableColumn<>(bundle.getString("act"));

        colInputName.setCellValueFactory(new TreeItemPropertyValueFactory<>("fileName"));
        colInputSize.setCellValueFactory(new TreeItemPropertyValueFactory<>("size"));

        colInputActive.setCellValueFactory((TreeTableColumn.CellDataFeatures<FileTreeItem, Boolean> p) -> {

            TreeItem<FileTreeItem> treeItem = p.getValue();
            FileTreeItem fileTreeItem = treeItem.getValue();

            fileTreeItem.getActiveProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                fileTreeItem.setActive(newValue);
                if (newValue) {
                    setParentActive(treeItem);
                    //setSiblingsActive(treeItem);
                } else {
                    setChildrenInactive(treeItem);
                }
            });

            return fileTreeItem.getActiveProperty();
        });

        colInputActive.setCellFactory((TreeTableColumn<FileTreeItem, Boolean> p) -> {
            CheckBoxTreeTableCell<FileTreeItem, Boolean> cell = new CheckBoxTreeTableCell<>();
            cell.setAlignment(Pos.CENTER);
            return cell;
        });

        ttvInput.getColumns().addAll(colInputName, colInputSize, colInputActive);
        ttvInput.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
        ttvInput.setEditable(true);

        TreeTableColumn<FileTreeItem, String> colOutputName = new TreeTableColumn<>(bundle.getString("tree.struct"));
        TreeTableColumn<FileTreeItem, String> colOutputSize = new TreeTableColumn<>(bundle.getString("size.byte"));

        colOutputName.setCellValueFactory(new TreeItemPropertyValueFactory<>("fileName"));
        colOutputSize.setCellValueFactory(new TreeItemPropertyValueFactory<>("size"));

        ttvOutput.getColumns().addAll(colOutputName, colOutputSize);
        ttvOutput.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);

        btnLoadDir.setOnAction(e -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setInitialDirectory(new File(Globals.propman.getProperty(Globals.DIR_INPUT, System.getProperty("user.dir"))));
            File dir = directoryChooser.showDialog(mainController.getStage());
            if (dir != null) {
                Globals.propman.put(Globals.DIR_INPUT, dir.getAbsolutePath());
                Globals.propman.save();

                TreeItem root = new TreeItem(new FileTreeItem(dir));
                ttvInput.setRoot(root);
                traverseDir(dir, root);

                cbAll.setSelected(false);
                cbExpandInput.setSelected(false);
                cbExpandOutput.setSelected(false);
                ttvOutput.setRoot(null);
            }
        });

        btnSaveDir.setOnAction(e -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setInitialDirectory(new File(Globals.propman.getProperty(Globals.DIR_OUTPUT, System.getProperty("user.dir"))));
            File dir = directoryChooser.showDialog(mainController.getStage());
            if (dir != null) {
                if (cbBits.getSelectionModel().getSelectedItem() != null && cbUpper.getSelectionModel().getSelectedItem() != null) {
                    Globals.propman.put(Globals.DIR_OUTPUT, dir.getAbsolutePath());
                    Globals.propman.save();

                    TreeItem root = new TreeItem(new FileTreeItem(dir));
                    ttvOutput.setRoot(root);

                    ProgressDialog progressDialog = new ProgressDialog(mainController.getStage(), bundle);
                    ConverterTask converterTask = new ConverterTask(progressDialog, ttvInput.getRoot(), ttvOutput.getRoot(), cbBits, cbUpper);
                    new Thread(converterTask).start();
                }
            }
        });

        cbAll.selectedProperty().addListener((ov, o, n) -> {
            setAllActive(ttvInput.getRoot(), n);
        });

        cbExpandInput.selectedProperty().addListener((ov, o, n) -> {
            expandTree(ttvInput.getRoot(), n);
        });

        cbExpandOutput.selectedProperty().addListener((ov, o, n) -> {
            expandTree(ttvOutput.getRoot(), n);
        });
    }

    private void setParentActive(TreeItem<FileTreeItem> item) {
        if (item.getParent() != null) {
            if (item.getParent().getValue().getFile().isDirectory()) {
                item.getParent().getValue().setActive(true);
            }
        }
    }

    private void expandTree(TreeItem<FileTreeItem> item, boolean expand) {
        if (item != null) {
            if (item.getValue().getFile().isDirectory()) {
                item.setExpanded(expand);
            }
            for (TreeItem<FileTreeItem> child : item.getChildren()) {
                expandTree(child, expand);
            }
        }
    }

    private void setAllActive(TreeItem<FileTreeItem> item, boolean setActive) {
        if (item != null) {
            item.getValue().setActive(setActive);
            for (TreeItem<FileTreeItem> child : item.getChildren()) {
                child.getValue().setActive(setActive);
                setAllActive(child, setActive);
            }
        }
    }

    private void setChildrenInactive(TreeItem<FileTreeItem> item) {
        if (item.getChildren() != null) {
            for (TreeItem<FileTreeItem> i : item.getChildren()) {
                i.getValue().setActive(false);
            }
        }
    }

    private void traverseDir(File file, TreeItem root) {

        for (File c : file.listFiles()) {
            if (c.listFiles() == null) {
                FileTreeItem fileTreeItem = new FileTreeItem(c);
                //getEndianness(c);
                root.getChildren().add(new TreeItem(fileTreeItem));
            } else {
                TreeItem fileItem = new TreeItem<>(new FileTreeItem(c));
                traverseDir(c, fileItem);
                root.getChildren().add(fileItem);
            }
        }
    }

    private void getEndianness(File file) {
        if (file.isFile()) {
            try {
                DataInputStream dataInputStream = new DataInputStream(new FileInputStream(file));

                System.out.println(file.getAbsolutePath());

                while (dataInputStream.available() > 0) {
                    byte[] input = dataInputStream.readNBytes(2);

                    String hexStart = String.format("%02X", input[0]);
                    String hexEnd = String.format("%02X", input[input.length - 1]);

                    BigInteger start = new BigInteger(hexStart, 16);
                    BigInteger end = new BigInteger(hexEnd, 16);

                    int erg = start.compareTo(end);

                    if (erg == -1 || erg == 1) {
                        System.out.println(erg);
                        break;
                    }
                }

                dataInputStream.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void traverseTree(TreeItem<FileTreeItem> root, TreeItem<FileTreeItem> out) {

        TreeItem<FileTreeItem> outRoot = new TreeItem(root.getValue());

        if (outRoot.getValue().isActive()) {
            out.getChildren().add(outRoot);
        }

        for (TreeItem<FileTreeItem> child : root.getChildren()) {
            if (child.getChildren().isEmpty()) {

                if (child.getValue().isActive()) {
                    outRoot.getChildren().add(child);
                }

            } else {

                traverseTree(child, outRoot);

            }
        }
    }

    @Override
    public void populate() {

    }

    @Override
    public void reset() {

    }

    @Override
    public void clear() {

    }
}
