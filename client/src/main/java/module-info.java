module com.example.wrddbanksystem {
    requires javafx.controls;
    requires javafx.web;
    requires javafx.media;
    requires javafx.swing;
    requires javafx.graphics;
    requires javafx.base;

    requires de.jensd.fx.glyphs.fontawesome;
    requires java.sql;
    requires mysql.connector.j;
    requires io.github.cdimascio.dotenv.java;
    requires de.jensd.fx.glyphs.commons;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.compiler;

    // HTTP Client and JSON modules
    requires java.net.http;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;

    exports com.example.wrddbanksystem;
    exports com.example.wrddbanksystem.Controllers;
    exports com.example.wrddbanksystem.Controllers.Client;
    exports com.example.wrddbanksystem.Controllers.Admin;
    exports com.example.wrddbanksystem.Controllers.common;
    exports com.example.wrddbanksystem.Models;
    exports com.example.wrddbanksystem.Views;

    opens com.example.wrddbanksystem.Models to com.fasterxml.jackson.databind, javafx.base;
    exports com.example.wrddbanksystem.Core;
    opens com.example.wrddbanksystem.Core to com.fasterxml.jackson.databind, javafx.base;
} 