package Animation;

import com.jfoenix.controls.JFXAlert;
import com.jfoenix.transitions.CachedTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Center scale animation for {@link JFXAlert} control
 *
 * @author Shadi Shaheen
 * @version 1.0
 * @since 2017-05-26
 */

public class CustomCenterTransition extends CachedTransition{
    public CustomCenterTransition(Node contentContainer, Node overlay) {
        super(contentContainer, new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(contentContainer.scaleXProperty(), 0, Interpolator.LINEAR),
                        new KeyValue(contentContainer.scaleYProperty(), 0, Interpolator.LINEAR),
                        new KeyValue(overlay.opacityProperty(), 0, Interpolator.EASE_BOTH)
                ),
                new KeyFrame(Duration.millis(1000),
                        new KeyValue(contentContainer.scaleXProperty(), 1, Interpolator.EASE_OUT),
                        new KeyValue(contentContainer.scaleYProperty(), 1, Interpolator.EASE_OUT),
                        new KeyValue(overlay.opacityProperty(), 1, Interpolator.EASE_BOTH)
                )));
        // reduce the number to increase the shifting , increase number to reduce shifting
        setCycleDuration(Duration.seconds(0.4));
        setDelay(Duration.seconds(0));
    }
}
