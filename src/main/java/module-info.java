module com.wangwei.routetool.routetool {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.wangwei.routetool.routetool to javafx.fxml;
    exports com.wangwei.routetool.routetool;
}