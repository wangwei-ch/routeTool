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
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class HelloApplication extends Application {


    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();

        // 创建左侧的二级菜单
        root.setLeft(createTreeView());

        // 创建可观察的数字属性
        IntegerProperty numberProperty1 = new SimpleIntegerProperty(0);
        IntegerProperty numberProperty2 = new SimpleIntegerProperty(0);
        IntegerProperty numberProperty3 = new SimpleIntegerProperty(0);

        // 右侧上部 - 带图标和文本的标签
        HBox infoBox = new HBox(10);
        infoBox.getChildren().addAll(
                createInfoLabel(numberProperty1.asString(), "file:/Users/wangwei/Desktop/DesktopFile/image/数据.png", "Count"),
                createInfoLabel(numberProperty2.asString(), "file:/Users/wangwei/Desktop/DesktopFile/image/水.png", "Success"),
                createInfoLabel(numberProperty3.asString(), "file:/Users/wangwei/Desktop/DesktopFile/image/通知.png", "Fail")
        );
        HBox.setHgrow(infoBox, Priority.ALWAYS); // 新增：使infoBox能够伸缩

        VBox outerBox = new VBox(infoBox); // 外部框
        outerBox.setStyle("-fx-border-color: #bfbfd9; -fx-border-width: 2; -fx-padding: 10px;");
        outerBox.setAlignment(Pos.CENTER);

        // 右侧下部布局
        VBox lowerSection = createLowerSection();

        // 将外部框添加到右侧布局中
        VBox rightPane = new VBox(10, outerBox, lowerSection);
        rightPane.setPadding(new Insets(10));
        VBox.setVgrow(lowerSection, Priority.ALWAYS); // 允许垂直增长
        rightPane.setAlignment(Pos.TOP_CENTER);
        root.setCenter(rightPane);


        Scene scene = new Scene(root, 1000, 800);
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
        imagePane.setAlignment(Pos.CENTER);
        imagePane.setMinSize(30, 30); // 设置适当的大小

        //数字居中
        Label numberLabel = new Label();
        numberLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: blue;");
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
        hbox.setAlignment(Pos.CENTER);
        hbox.setStyle("-fx-border-color: #bfbfd9; -fx-border-width: 1; -fx-padding: 10px;");
        hbox.setMaxWidth(500);
        HBox.setHgrow(hbox, Priority.ALWAYS); // 允许水平增长

        return hbox;

    }

    private VBox createLowerSection() {
        // 左侧部分
        ProgressBar progressBar = new ProgressBar(0);
        TextField directoryTextField = new TextField();
        directoryTextField.setPromptText("选择文件夹路径");

        Button selectDirectoryButton = new Button("选择文件夹");
        selectDirectoryButton.setOnAction(e -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            File selectedDirectory = directoryChooser.showDialog(new Stage());
            if (selectedDirectory != null) {
                directoryTextField.setText(selectedDirectory.getAbsolutePath());
            }
        });

        VBox leftPart = new VBox(10, progressBar, directoryTextField, selectDirectoryButton);

        // 右侧部分 - 内容待定
        VBox rightTopPart = new VBox(new Label("右上部分内容"));
        VBox rightBottomPart = new VBox(new Label("右下部分内容"));

        // 整合左侧和右侧部分
        HBox combinedParts = new HBox(20, leftPart, rightTopPart, rightBottomPart);

        // 将HBox包裹在一个VBox中
        VBox lowerSection = new VBox(combinedParts);
        VBox.setVgrow(lowerSection, Priority.ALWAYS);

        return lowerSection;
    }



    public static void main(String[] args) {
        launch();
    }
}