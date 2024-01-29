package com.wangwei.routetool.routetool;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {


    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();

        // 创建左侧的二级菜单
        TreeView<String> treeView = createTreeView();
        root.setLeft(treeView);


        // 创建可观察的数字属性
        IntegerProperty numberProperty1 = new SimpleIntegerProperty(0);
        IntegerProperty numberProperty2 = new SimpleIntegerProperty(0);
        IntegerProperty numberProperty3 = new SimpleIntegerProperty(0);


        // 创建右侧布局
        VBox rightPane = new VBox(10); // 垂直布局
        rightPane.setPadding(new Insets(10));
        VBox.setVgrow(rightPane, Priority.ALWAYS); // 新增：使rightPane能够伸缩


        // 右侧上部 - 带图标和文本的标签
        HBox infoBox = new HBox(10);
        infoBox.getChildren().addAll(
                createInfoLabel(numberProperty1.asString(), "file:/Users/wangwei/Desktop/DesktopFile/image/数据.png", "Count"),
                createInfoLabel(numberProperty2.asString(), "file:/Users/wangwei/Desktop/DesktopFile/image/水.png", "Success"),
                createInfoLabel(numberProperty3.asString(), "file:/Users/wangwei/Desktop/DesktopFile/image/通知.png", "Fail")
        );
        HBox.setHgrow(infoBox, Priority.ALWAYS); // 新增：使infoBox能够伸缩

        // 右侧下部 - 其他内容
        Label otherContentLabel = new Label("这里是其他内容");
        rightPane.getChildren().addAll(infoBox, otherContentLabel);
        root.setCenter(rightPane);

        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setTitle("JavaFX 应用");
        primaryStage.show();


        // 模拟业务逻辑更新数字
        // 在实际应用中，这些更新将来自您的业务逻辑
        new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    Thread.sleep(1000); // 模拟业务逻辑的延迟
                    int finalI = i;
                    javafx.application.Platform.runLater(() -> {
                        numberProperty1.set(finalI);
                        numberProperty2.set(finalI * 2);
                        numberProperty3.set(finalI * 3);
                    });
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }



    /**
     * 左侧二级菜单布局
     * @return
     */
    private TreeView<String> createTreeView() {
        TreeItem<String> rootItem = new TreeItem<>("主菜单");
        rootItem.setExpanded(true);

        // 添加第一个一级菜单和它的二级菜单
        TreeItem<String> firstItem = new TreeItem<>("一级菜单1");
        firstItem.getChildren().addAll(
                new TreeItem<>("二级菜单1-1"),
                new TreeItem<>("二级菜单1-2"),
                new TreeItem<>("二级菜单1-3")
        );

        // 添加第二个一级菜单和它的二级菜单
        TreeItem<String> secondItem = new TreeItem<>("一级菜单2");
        secondItem.getChildren().addAll(
                new TreeItem<>("二级菜单2-1"),
                new TreeItem<>("二级菜单2-2")
        );

        // 继续添加更多的菜单项...

        rootItem.getChildren().addAll(firstItem, secondItem);

        return new TreeView<>(rootItem);
    }

    /**
     * 右侧上部 - 带图标和文本的标签
     * @param numberProperty
     * @param iconPath
     * @param text
     * @return
     */
    private HBox createInfoLabel(StringBinding numberProperty, String iconPath, String text) {

        Image image = new Image(iconPath);
        ImageView imageView = new ImageView(image);
        // 设置图标的显示尺寸
        imageView.setFitWidth(20);  // 设置图标的宽度
        imageView.setFitHeight(20); // 设置图标的高度
        imageView.setPreserveRatio(true); // 保持图像的纵横比

        // 使用 StackPane 使图标居中
        StackPane imagePane = new StackPane(imageView);
        imagePane.setAlignment(Pos.CENTER_RIGHT);
        imagePane.setMinSize(30, 30); // 设置适当的大小

        //数字居中
        Label numberLabel = new Label();
        numberLabel.textProperty().bind(numberProperty);
        StackPane numberPane = new StackPane(numberLabel);
        numberPane.setAlignment(Pos.CENTER);

        Label textLabel = new Label(text);
        StackPane textPane = new StackPane(textLabel);
        textPane.setAlignment(Pos.CENTER); // 确保文本居中对齐


        VBox rightSide = new VBox(5, numberPane, textPane);
        rightSide.setAlignment(Pos.CENTER);
        rightSide.setMinWidth(60);  // 设置最小宽度
        rightSide.setMaxWidth(150); // 设置最大宽度
        VBox.setVgrow(rightSide, Priority.ALWAYS); // 允许垂直增长

        HBox hbox = new HBox(20, imagePane, rightSide);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-padding: 10px;");
        hbox.setMaxWidth(200);
        HBox.setHgrow(hbox, Priority.ALWAYS); // 允许水平增长

        return hbox;

    }


    public static void main(String[] args) {
        launch();
    }
}