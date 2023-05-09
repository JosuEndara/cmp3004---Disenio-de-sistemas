
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class SubastaManager {
    private Map<String, Subasta> mapa;
    private Timer timer;
    boolean timerActive = false;
    int timeLeft = 10;

    private Alert alert;
    @FXML
    private TextField txtProducto;
    @FXML
    private TextField txtPrecioBase;
    @FXML
    private TextField txtTiempo;
    @FXML
    private ChoiceBox<String> chBox;
    @FXML
    private Label lbtimer;
    @FXML
    private Label lbAC1;
    @FXML
    private Label lbAC2;
    @FXML
    private Label lbAC3;
    @FXML
    private TextField ApuestaC1;
    @FXML
    private TextField ApuestaC2;
    @FXML
    private TextField ApuestaC3;
    @FXML
    private Button btnApuestaC1;
    @FXML
    private Button btnApuestaC2;
    @FXML
    private Button btnApuestaC3;
    @FXML
    private TextArea logC1;
    @FXML
    private TextArea logC2;
    @FXML
    private TextArea logC3;
    @FXML
    private Label name1;
    @FXML
    private Label name2;
    @FXML
    private Label name3;
    @FXML
    private Button btnName1;
    @FXML
    private Button btnName2;
    @FXML
    private Button btnName3;

    public SubastaManager() {
        mapa = new HashMap<>();
    }

    public void initialize() {
        deactivateClients();
    }

    @FXML
    void ingresarItem() {
        if (txtProducto.getText().isEmpty() || txtPrecioBase.getText().isEmpty() || txtTiempo.getText().isEmpty()) {
            mensaje("Un campo se encuentra vacio");
        } else {
            mapa.put(txtProducto.getText(), new Subasta(txtProducto.getText(),
                    Double.parseDouble(txtPrecioBase.getText()), Integer.parseInt(txtTiempo.getText())));
            chBox.getItems().add(txtProducto.getText());
            txtProducto.clear();
            txtPrecioBase.clear();
            txtTiempo.clear();
            mensaje("El item ha sido agregado");
        }

    }

    @FXML
    void empezarTimer() {
        lbtimer.setText("");
        String item = chBox.getValue();
        if(item != null){
            timerActive = true;
            timer = new Timer();
            activateClients();
            timer.schedule(new TimerTask() {
                int remainingSeconds = mapa.get(item).getTiempo();
    
                @Override
                public void run() {
                    if (remainingSeconds == 0) {
                        timer.cancel();
                        Platform.runLater(() -> {
                            lbtimer.setText("00:00:00");
                            mensaje("Subasta por item " + item + " ha termiando");
                            timerActive = false;
                        });
                        deactivateClients();
                        
                    } else {
                        remainingSeconds--;
                        Platform.runLater(() -> lbtimer.setText((remainingSeconds) + ""));
                    }
                }
            }, 0, 1000);
        }
        else{
            mensaje("Elija alguna opcion");
        }

        
    }

    public void mensaje(String mensaje) {
        alert = new Alert(AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public void deactivateClients() {

        ApuestaC1.setVisible(false);
        ApuestaC2.setVisible(false);
        ApuestaC3.setVisible(false);
        btnApuestaC1.setVisible(false);
        btnApuestaC2.setVisible(false);
        btnApuestaC3.setVisible(false);
        logC1.setVisible(false);
        logC2.setVisible(false);
        logC3.setVisible(false);
        lbAC1.setVisible(false);
        lbAC2.setVisible(false);
        lbAC3.setVisible(false);

    }

    public void activateClients() {
        ApuestaC1.setVisible(true);
        ApuestaC2.setVisible(true);
        ApuestaC3.setVisible(true);
        btnApuestaC1.setVisible(true);
        btnApuestaC2.setVisible(true);
        btnApuestaC3.setVisible(true);
        logC1.setVisible(true);
        logC2.setVisible(true);
        logC3.setVisible(true);
        lbAC1.setVisible(true);
        lbAC2.setVisible(true);
        lbAC3.setVisible(true);
    }
    public void manageLabels(boolean flag){
        name1.setVisible(flag);
        name2.setVisible(flag);
        name3.setVisible(flag);
    }
}
