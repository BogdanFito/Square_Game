package sample;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.*;

public class Controller implements Initializable {

    @FXML
    private Rectangle rectangle;
    @FXML
    private Button start_button;
    @FXML
    private Button stop_button;
    @FXML
    private TextArea time;
    @FXML
    private TextArea mistakes;
    @FXML
    BorderPane pane;
    @FXML
    private CheckBox first_opponent;
    @FXML
    private CheckBox second_opponent;
    @FXML
    private CheckBox third_opponent;
    @FXML
    private CheckBox fourth_opponent;
    @FXML
    private Rectangle rectangle2;
    @FXML
    private Rectangle rectangle1;
    @FXML
    private Rectangle rectangle3;
    @FXML
    private Rectangle rectangle4;
    @FXML
    private Slider slider;
    @FXML
    private Slider slider2;
    @FXML
    private CheckBox auto_speed;
    @ FXML private Text speed;
    @FXML private Text increase;

    private double orgSceneX, orgSceneY;
    private double orgTranslateX, orgTranslateY;
    private boolean game_started = false, game_in_pause;
    private int count, sec, mistake;

    private Timer timer = new Timer();

    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            if (count==10){time.setText(String.valueOf(++sec)); count=0;
                if (auto_speed.isSelected()) slider.setValue(slider.getValue()+slider2.getValue()); //speed increase
            } //timer's seconds
            for (int i = 0; i < checks.size(); i++) {
                if (!checks.get(i).isDisable() && checks.get(i).isSelected() && !game_in_pause) {
                    rectangles.get(i).setVisible(true);
                    checks.get(i).setDisable(true);

                    move(rectangles.get(i));
                }

            }


            speed.setText(String.valueOf((int)(slider.getValue())));
            increase.setText(String.valueOf((slider2.getValue()*10)));
            count++;
        }
    };

    private TimerTask timerTask2 = new TimerTask() {

        @Override
        public void run() {
            if (!game_in_pause) {
                for (Rectangle rect : rectangles) {
                    if (rect.getBoundsInParent().intersects(rectangle.getBoundsInParent()) && rect.isVisible()) {
                        for (javax.swing.Timer moves : movements) {
                            moves.stop(); //if there is an intersection stop all rectangles

                        }
                        game_in_pause = true; mistakes.setText(String.valueOf(++mistake)); break;
                    }
                }

            }
            else {
                boolean change = true;
                for (Rectangle rect : rectangles) {
                    if (rect.getBoundsInParent().intersects(rectangle.getBoundsInParent()) && rect.isVisible()) {
                        change = false;
                        break;
                    }

                }
                if (change) {
                    for (javax.swing.Timer moves : movements) {
                        moves.setInitialDelay((int) slider.getMax() - (int) slider.getValue()+ 1);
                        moves.setDelay((int) slider.getMax() - (int) slider.getValue() + 1);
                        moves.restart(); //restart rectangles

                    }
                    game_in_pause = false;

                }
            }
        }
    };



    private ArrayList<javax.swing.Timer> movements = new ArrayList<>();
    private ArrayList<CheckBox> checks = new ArrayList<>();
    private ArrayList<Rectangle> rectangles = new ArrayList<>();


    private Random random = new Random();

    private void start_timer() {
        time.setText("0");  mistakes.setText("0");
        count=0;sec=0;mistake=0;
        timer.scheduleAtFixedRate(timerTask,100,100);
        timer.scheduleAtFixedRate(timerTask2,10,10);
    }



    @FXML
    private void start_game() {
        game_started = true;
        game_in_pause=false;
        start_button.setDisable(true);
        stop_button.setDisable(false);
        rectangle.setTranslateY(0); rectangle.setTranslateX(0);
        movements.clear();
        slider.setValue(slider.getMin());


        for (int i=0;i<rectangles.size();i++) {
            rectangles.get(i).setTranslateY(0); rectangles.get(i).setTranslateX(0);
            if (checks.get(i).isSelected()) {
                rectangles.get(i).setVisible(true); checks.get(i).setDisable(true);
                move(rectangles.get(i));
            }
            else  rectangles.get(i).setVisible(false);
        }

        start_timer();

    }

    @FXML
    private void stop_game() {
        game_started = false;
        start_button.setDisable(false);
        stop_button.setDisable(true);

        for (CheckBox check : checks) {
            check.setSelected(false);
            check.setDisable(false);
        }
        for (javax.swing.Timer movement : movements) {
            movement.stop();
        }

        timer.cancel(); timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (count==10){time.setText(String.valueOf(++sec)); count=0;
                    if (auto_speed.isSelected()) slider.setValue(slider.getValue()+slider2.getValue()); //speed increase
                } //timer's seconds
                for (int i = 0; i < checks.size(); i++) {
                    if (!checks.get(i).isDisable() && checks.get(i).isSelected() && !game_in_pause) {
                        rectangles.get(i).setVisible(true);
                        checks.get(i).setDisable(true);

                        move(rectangles.get(i));
                    }

                }


                speed.setText(String.valueOf((int)(slider.getValue())));
                increase.setText(String.valueOf((slider2.getValue()*10)));
                count++;
            }
        };

        timerTask2 = new TimerTask() {


            @Override
            public void run() {
                if (!game_in_pause) {
                    for (Rectangle rect : rectangles) {
                        if (rect.getBoundsInParent().intersects(rectangle.getBoundsInParent()) && rect.isVisible()) {
                            for (javax.swing.Timer moves : movements) {
                                moves.stop(); //if there is an intersection stop all rectangles

                            }
                            game_in_pause = true; mistakes.setText(String.valueOf(++mistake)); break;
                        }
                    }

                }
                else {
                    boolean change = true;
                    for (Rectangle rect : rectangles) {
                        if (rect.getBoundsInParent().intersects(rectangle.getBoundsInParent()) && rect.isVisible()) {
                            change = false;
                            break;
                        }

                    }
                    if (change) {
                        for (javax.swing.Timer moves : movements) {
                            moves.setInitialDelay((int) slider.getMax() - (int) slider.getValue()+ 1);
                            moves.setDelay((int) slider.getMax() - (int) slider.getValue() + 1);
                            moves.restart(); //restart rectangles

                        }
                        game_in_pause = false;

                    }
                }
            }
        };



    }

    private EventHandler<MouseEvent> circleOnMousePressedEventHandler =
            t -> {
                orgSceneX = t.getSceneX();
                orgSceneY = t.getSceneY();
                orgTranslateX = ((Rectangle)(t.getSource())).getTranslateX();
                orgTranslateY = ((Rectangle)(t.getSource())).getTranslateY();
            };

    private EventHandler<MouseEvent> circleOnMouseDraggedEventHandler =
            t -> {
                if (game_started) {
                    double offsetX = t.getSceneX() - orgSceneX;
                    double offsetY = t.getSceneY() - orgSceneY;
                    double newTranslateX = orgTranslateX + offsetX;
                    double newTranslateY = orgTranslateY + offsetY;

                    ((Rectangle) (t.getSource())).setTranslateX(newTranslateX);
                    ((Rectangle) (t.getSource())).setTranslateY(newTranslateY);

                    if (rectangle.getTranslateY() + rectangle.getLayoutY() <= 0)
                        rectangle.setTranslateY(-rectangle.getLayoutY());
                    if (rectangle.getTranslateY() + rectangle.getLayoutY() >= pane.getHeight() - rectangle.getHeight())
                        rectangle.setTranslateY(rectangle.getLayoutY());
                    if (rectangle.getTranslateX() + rectangle.getLayoutX() >= pane.getWidth() - rectangle.getWidth())
                        rectangle.setTranslateX(rectangle.getLayoutX());
                    if (rectangle.getTranslateX() + rectangle.getLayoutX() <= 0)
                        rectangle.setTranslateX(-rectangle.getLayoutX());


                }

            };

    private void move(Rectangle one_rectangle) {
        javax.swing.Timer timer = new javax.swing.Timer((int) (slider.getMax() -  slider.getValue() + 1) , new ActionListener() {
            boolean top = random.nextBoolean();
            boolean right = random.nextBoolean();

            @Override
            public void actionPerformed(ActionEvent e) {
                Platform.runLater(() -> {


                    if (top) {

                        if (one_rectangle.getTranslateY() + one_rectangle.getLayoutY() <= 0) top = false;
                        else one_rectangle.setTranslateY(one_rectangle.getTranslateY() - 1);


                    } else {

                        if (one_rectangle.getTranslateY() + one_rectangle.getLayoutY() >= pane.getHeight() - one_rectangle.getHeight())
                            top = true;
                        else one_rectangle.setTranslateY(one_rectangle.getTranslateY() + 1);
                    }
                    if (right) {

                        if (one_rectangle.getTranslateX() + one_rectangle.getLayoutX() >= pane.getWidth() - one_rectangle.getWidth())
                            right = false;
                        else one_rectangle.setTranslateX(one_rectangle.getTranslateX() + 1);
                    } else {

                        if (one_rectangle.getTranslateX() + one_rectangle.getLayoutX() <= 0) right = true;
                        else one_rectangle.setTranslateX(one_rectangle.getTranslateX() - 1);
                    }


                });



            }

        });timer.start(); movements.add(timer);  }

    public void initialize(URL url, ResourceBundle rb) {
        rectangles.add(rectangle1); rectangles.add(rectangle2); rectangles.add(rectangle3); rectangles.add(rectangle4);
        checks.add(first_opponent); checks.add(second_opponent); checks.add(third_opponent); checks.add(fourth_opponent);

        for (Rectangle rect : rectangles) {
            rect.setVisible(false);
        }
        stop_button.setDisable(true);


        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        rectangle.setOnMousePressed(circleOnMousePressedEventHandler);
        rectangle.setOnMouseDragged(circleOnMouseDraggedEventHandler);



        slider.valueProperty().addListener((changed, oldValue, newValue) -> {
            if (game_started && !game_in_pause) {
                for (javax.swing.Timer timer1 : movements) {
                    timer1.setInitialDelay((int) slider.getMax() - (int) newValue.doubleValue() + 1);
                    timer1.setDelay((int) slider.getMax() - (int) newValue.doubleValue() + 1);
                    timer1.restart();
                }
            }
        });




        speed.setText(String.valueOf(slider.getMin()));
        increase.setText(String.valueOf(slider.getMin()));



    }


}
