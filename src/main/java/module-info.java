module project_javamon.homis_arena {
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
    requires com.google.gson;


    opens project_javamon.homis_arena.Game.Pokemon to com.google.gson;
    opens project_javamon.homis_arena.Util to com.google.gson;
    exports project_javamon.homis_arena.Util to com.google.gson;
    opens project_javamon.homis_arena to javafx.fxml;
    exports project_javamon.homis_arena;
    exports project_javamon.homis_arena.Controller;
    opens project_javamon.homis_arena.Controller to javafx.fxml;
}