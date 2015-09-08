package org.test.touchpointcountmismatch;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(Main.class);
    
    @Override
    public void start(Stage stage) {
        final Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();

        final Pane root = new Pane();
        final Label label = new Label("Touch and release with at least 2 fingers simultaneously. TouchCount should be '0'");
        label.setTranslateX(0);
        label.setTranslateY(label.getFont().getSize() + 2);
        
        final Text touchCountText = new Text("0");
        touchCountText.setTextOrigin(VPos.TOP);
        touchCountText.layoutXProperty().bind(root.widthProperty().subtract(touchCountText.prefWidth(-1)).divide(2));
        touchCountText.layoutYProperty().bind(root.heightProperty().subtract(touchCountText.prefHeight(-1)).divide(2));
        
        final Bounds textBounds = touchCountText.getBoundsInLocal();
        root.layoutBoundsProperty().addListener((ObservableValue<? extends Bounds> observable, Bounds oldValue, Bounds newValue) -> {
            double scaleX = newValue.getWidth()/textBounds.getWidth();
            double scaleY = newValue.getHeight()/textBounds.getHeight();
            touchCountText.setScaleX(scaleX);
            touchCountText.setScaleY(scaleY);
        });
        
        
        root.addEventHandler(TouchEvent.ANY, (TouchEvent event) -> {
            log.info("handler(TouchEvent.ANY): touchCount=" + event.getTouchCount());
            if (event.getEventType().equals(TouchEvent.TOUCH_PRESSED)) {
                touchCountText.setText(String.valueOf(event.getTouchCount()));
            } else if (event.getEventType().equals(TouchEvent.TOUCH_RELEASED)) {
                touchCountText.setText(String.valueOf(event.getTouchCount() - 1));
            }            
        });

        root.getChildren().addAll(label, touchCountText);
//        root.getChildren().add(touchCountText);
        Scene scene = new Scene(root, visualBounds.getWidth(), visualBounds.getHeight());

        stage.setScene(scene);
        stage.show();
    }

}
