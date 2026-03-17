module pl.pwr.gui {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    //for sql
    requires java.sql;
    requires org.xerial.sqlitejdbc;

    opens pl.pwr.controller to javafx.fxml;
    exports pl.pwr.controller;
}