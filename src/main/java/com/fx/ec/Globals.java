package com.fx.ec;

import java.nio.ByteOrder;

public class Globals {

    //ByteOrder
    public static ByteOrder BYTE_ORDER = ByteOrder.LITTLE_ENDIAN;

    //Debug Infos - Ein / Aus
    public static boolean DEBUG = true;

    //Fenstergröße global gesteuert in 16:9
    public static final double HEIGHT = 720;//900.0f;
    public static final double WIDTH = HEIGHT * 18.0f / 9.0f;

    //Schalter für Maximized Ein / Aus
    public static final boolean MAXIMIZED = false;

    //Nichts selktiert
    public static final int NO_SEL_VALUE = -9999;

    public static final String BUNDLE_PATH = "com.fx.ec.bundle.ec";
    public static final String LOG4J2_CONFIG_PATH = System.getProperty("user.dir") + "/config/log4j2.xml";
    public static final String XML_CONFIG_PATH = System.getProperty("user.dir") + "/config/config.xml";

    public static XMLPropertyManager propman;

    static {
        propman = new XMLPropertyManager(XML_CONFIG_PATH);
    }

    //Images
    public static final String APP_LOGO_PATH = System.getProperty("user.dir") + "/images/kdf.png";
    public static final String CSS_PATH = "/com/fx/ec/style/hec.css";

    //FXML
    public static final String FXML_PATH = "/com/fx/ec/fxml/";
    public static final String FXML_TABS_PATH = "/com/fx/ec/fxml/tabs/";

    public static final String FXML_MAIN_PATH = FXML_PATH + "main_app.fxml";

    public static final String FXML_TAB_CHANGE_PATH = FXML_TABS_PATH + "change.fxml";

    //FXML-Dialoge
    public static final String DLG_PROGRESS_PATH = FXML_PATH + "progress.fxml";
    public static final String DLG_TILE_PATH = FXML_PATH + "tile_dlg.fxml";
    public static final String DLG_WGS_PATH = FXML_PATH + "dlg_wgs.fxml";
    public static final String DLG_OSM_PATH = FXML_PATH + "dlg_osm.fxml";
    public static final String DLG_TX_PATH = FXML_PATH + "dlg_tx.fxml";

    //Custom-Path's
    public static final String DIR_INPUT = "DIR_INPUT";
    public static final String DIR_OUTPUT = "DIR_OUTPUT";

    public static long WAIT_TIME_SWING = 1000;
}
