import java.io.BufferedWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
    private Map<String, Cliente> mapaCliente;
    private Timer timer;
    private boolean timerActive = false;
    private int timeLeft = 10;
    private Cliente cliente1;
    private Cliente cliente2;
    private Cliente cliente3;
    private Alert alert;
    private String winner;

    @FXML
    private TextField txtProducto;
    @FXML
    private TextField txtPrecioBase;
    @FXML
    private TextField txtTiempo;
    @FXML
    private ChoiceBox<String> chBox;
    @FXML
    private ChoiceBox<String> chCliente1;
    @FXML
    private ChoiceBox<String> chCliente2;
    @FXML
    private ChoiceBox<String> chCliente3;
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
    private Button btnStart;
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
    @FXML
    private TextField txtName1;
    @FXML
    private TextField txtName2;
    @FXML
    private TextField txtName3;
    @FXML
    private Button btnIngresarC1;
    @FXML
    private Button btnIngresarC2;
    @FXML
    private Button btnIngresarC3;
    @FXML
    private Button btnItem1;
    @FXML
    private Button btnItem2;
    @FXML
    private Button btnItem3;

    public SubastaManager() {
        mapa = new HashMap<>();
        mapaCliente = new HashMap<>();
    }
    public String getTime(long currTime){
        long currentTimeMillis = currTime;
        Date currentDate = new Date(currentTimeMillis);
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String formattedTime = timeFormat.format(currentDate);
        return formattedTime;

    }

    public void initialize() {
        deactivateClients();
        name1.setVisible(false);
        name2.setVisible(false);
        name3.setVisible(false);
        chCliente1.setVisible(false);
        chCliente2.setVisible(false);
        chCliente3.setVisible(false);
        btnIngresarC1.setVisible(false);
        btnIngresarC2.setVisible(false);
        btnIngresarC3.setVisible(false);
        btnItem1.setVisible(false);
        btnItem2.setVisible(false);
        btnItem3.setVisible(false);
        

    }

    @FXML
    void ingresarItem() {
        if (txtProducto.getText().isEmpty() || txtPrecioBase.getText().isEmpty() || txtTiempo.getText().isEmpty()) {
            mensajeError("Un campo se encuentra vacio");
        } else {
            mapa.put(txtProducto.getText(), new Subasta(txtProducto.getText(),
                    Double.parseDouble(txtPrecioBase.getText()), Integer.parseInt(txtTiempo.getText())));
            ingresarChoiceBox(txtProducto.getText());
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
        if (item != null) {
            timerActive = true;
            timer = new Timer();
            activateClients();
            btnStart.setVisible(false);
            timer.schedule(new TimerTask() {
                int remainingSeconds = mapa.get(item).getTiempo();

                @Override
                public void run() {
                    if (remainingSeconds == 0) {
                        timer.cancel();
                        Platform.runLater(() -> {
                            lbtimer.setText("00:00:00");
                            mensaje("Subasta por item " + item + " ha terminado\n" + winner + " lo ha comprado por "
                                    + mapa.get(item).getPrecioBase());
                                    try {
                                        BufferedWriter writer = new BufferedWriter(new FileWriter("manager.txt", true));
                                        writer.write("Subasta por item " + item + " ha terminado\n" + winner + " lo ha comprado por "
                                        + mapa.get(item).getPrecioBase() + " a las " + getTime(System.currentTimeMillis()) + "\n");
                                        writer.close();
                                      } catch (IOException ex) {
                                        ex.printStackTrace();
                                      }
                            timerActive = false;
                            btnStart.setVisible(true);
                            chBox.getItems().remove(item);
                            chCliente1.getItems().remove(item);
                            chCliente2.getItems().remove(item);
                            chCliente3.getItems().remove(item);
                            chBox.setValue(null);
                            chCliente1.setValue(null);
                            chCliente2.setValue(null);
                            chCliente3.setValue(null);

                        });
                        deactivateClients();
                        cliente1.restore();
                        cliente2.restore();
                        cliente3.restore();

                    } else {
                        remainingSeconds--;
                        Platform.runLater(() -> lbtimer.setText((remainingSeconds) + ""));

                    }
                }
            }, 0, 1000);
        } else {
            mensajeError("Elija alguna opcion");
        }

    }

    public void mensaje(String mensaje) {
        alert = new Alert(AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public void mensajeError(String mensaje) {
        alert = new Alert(AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public void deactivateClients() {
        ApuestaC1.clear();
        ApuestaC2.clear();
        ApuestaC3.clear();
        ApuestaC1.setVisible(false);
        ApuestaC2.setVisible(false);
        ApuestaC3.setVisible(false);
        btnApuestaC1.setVisible(false);
        btnApuestaC2.setVisible(false);
        btnApuestaC3.setVisible(false);
        logC1.setVisible(false);
        logC2.setVisible(false);
        logC3.setVisible(false);
        logC1.clear();
        logC2.clear();
        logC3.clear();
        lbAC1.setVisible(false);
        lbAC2.setVisible(false);
        lbAC3.setVisible(false);

    }

    public void ingresarChoiceBox(String nombres) {
        chBox.getItems().add(nombres);
        chCliente1.getItems().add(nombres);
        chCliente2.getItems().add(nombres);
        chCliente3.getItems().add(nombres);
    }

    public void activateClients() {
        try {
            if (cliente1.getInscritoItem()) {
                logC1.setVisible(true);
            }
            if (cliente1.getInscritoSubasta()) {
                lbAC1.setVisible(true);
                ApuestaC1.setVisible(true);
                btnApuestaC1.setVisible(true);
            }

            if (cliente2.getInscritoItem()) {
                logC2.setVisible(true);
            }
            if (cliente2.getInscritoSubasta()) {
                lbAC2.setVisible(true);
                ApuestaC2.setVisible(true);
                btnApuestaC2.setVisible(true);
            }

            if (cliente3.getInscritoItem()) {
                logC3.setVisible(true);
            }
            if (cliente3.getInscritoSubasta()) {
                lbAC3.setVisible(true);
                ApuestaC3.setVisible(true);
                btnApuestaC3.setVisible(true);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public void manageLabels(boolean flag) {
        name1.setVisible(flag);
        name2.setVisible(flag);
        name3.setVisible(flag);
    }

    @FXML
    void getClient1() {
        if (!txtName1.getText().isEmpty()) {
            name1.setVisible(true);
            cliente1 = new Cliente(txtName1.getText());
            name1.setText(txtName1.getText());
            txtName1.setVisible(false);
            btnName1.setVisible(false);
            chCliente1.setVisible(true);
            btnIngresarC1.setVisible(true);
            btnItem1.setVisible(true);
            btnItem1.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        if (chCliente1.getValue().equals(chBox.getValue())) {
                            if (!(chCliente1.getSelectionModel().getSelectedItem() == null)) {
                                cliente1.setInscritoItem(true);
                                mensaje("Se ha inscrito al item");
                                try {
                                    BufferedWriter writer = new BufferedWriter(new FileWriter("client1.txt", true));
                                    writer.write("Se ha inscrito al item " + chBox.getValue() + " a las " + getTime(System.currentTimeMillis()) + "\n");
                                    writer.close();
                                  } catch (IOException ex) {
                                    ex.printStackTrace();
                                  }
                                
                            } else {
                                mensajeError("No se ha ingresado subasta");
                            }
                        } else {
                            mensajeError("El item a suscribir debe coincidir con el que se va a subastar");
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                    
                }
            });
            btnIngresarC1.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (cliente1.getInscritoItem()) {
                        cliente1.setInscritoSubasta(true);
                        mensaje("Se ha inscrito a la subasta");
                        try {
                            BufferedWriter writer = new BufferedWriter(new FileWriter("client1.txt", true));
                            writer.write("Se ha inscrito a la subasta del item " + chBox.getValue() + " a las " + getTime(System.currentTimeMillis()) + "\n");
                            writer.close();
                          } catch (IOException ex) {
                            ex.printStackTrace();
                          }
                    } else {
                        mensajeError("Primero tiene que inscribirse a un item");
                    }
                }
            });
            btnApuestaC1.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (!ApuestaC1.getText().isEmpty()) {
                        try {
                            double value = Double.parseDouble(ApuestaC1.getText());
                            if (!mapa.get(chCliente1.getValue()).modificarPrecio(value)) {
                                logC1.appendText("el valor no es superior al aceptado \n");
                            } else {
                                logC1.appendText("Hora: ["+ getTime(System.currentTimeMillis())+"] "+ cliente1.getName() + " ha incrementado el precio a: "
                                        + mapa.get(chCliente1.getValue()).getPrecioBase() + "\n");
                                logC2.appendText("Hora: ["+ getTime(System.currentTimeMillis())+"] "+cliente1.getName() + " ha incrementado el precio a: "
                                        + mapa.get(chCliente1.getValue()).getPrecioBase() + "\n");
                                logC3.appendText("Hora: ["+ getTime(System.currentTimeMillis())+"] "+cliente1.getName() + " ha incrementado el precio a: "
                                        + mapa.get(chCliente1.getValue()).getPrecioBase() + "\n");
                                        try {
                                            BufferedWriter writer = new BufferedWriter(new FileWriter("client1.txt", true));
                                            writer.write(cliente1.getName() + " ha incrementado el precio a: "
                                            + mapa.get(chCliente1.getValue()).getPrecioBase() + " a las " + getTime(System.currentTimeMillis()) + "\n");
                                            writer.close();
                                          } catch (IOException ex) {
                                            ex.printStackTrace();
                                          }
                                          try {
                                            BufferedWriter writer = new BufferedWriter(new FileWriter("manager.txt", true));
                                            writer.write(cliente1.getName() + " ha incrementado el precio a: "
                                            + mapa.get(chCliente1.getValue()).getPrecioBase() + " a las " + getTime(System.currentTimeMillis()) + "\n");
                                            writer.close();
                                          } catch (IOException ex) {
                                            ex.printStackTrace();
                                          }
                                winner = cliente1.getName();

                            }
                        } catch (Exception e) {
                            // TODO: handle exception
                        }

                    }
                }
            });

        }

    }

    @FXML
    void getClient2() {
        if (!txtName2.getText().isEmpty()) {
            cliente2 = new Cliente(txtName2.getText());
            name2.setVisible(true);
            name2.setText(txtName2.getText());
            txtName2.setVisible(false);
            btnName2.setVisible(false);
            chCliente2.setVisible(true);
            btnIngresarC2.setVisible(true);
            btnItem2.setVisible(true);
            btnItem2.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        if (chCliente2.getValue().equals(chBox.getValue())) {
                            if (!(chCliente2.getSelectionModel().getSelectedItem() == null)) {
                                cliente2.setInscritoItem(true);
                                mensaje("Se ha inscrito al item");
                                try {
                                    BufferedWriter writer = new BufferedWriter(new FileWriter("client2.txt", true));
                                    writer.write("Se ha inscrito al item " + chBox.getValue() + " a las " + getTime(System.currentTimeMillis()) + "\n");
                                    writer.close();
                                  } catch (IOException ex) {
                                    ex.printStackTrace();
                                  }
                            } else {
                                mensajeError("No se ha ingresado subasta");
                            }
                        } else {
                            mensajeError("El item a suscribir debe coincidir con el que se va a subastar");
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                    

                }
            });
            btnIngresarC2.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (cliente2.getInscritoItem()) {
                        cliente2.setInscritoSubasta(true);
                        mensaje("Se ha inscrito a la subasta");
                        try {
                            BufferedWriter writer = new BufferedWriter(new FileWriter("client2.txt", true));
                            writer.write("Se ha inscrito a la subasta del item " + chBox.getValue() + " a las " + getTime(System.currentTimeMillis()) + "\n");
                            writer.close();
                          } catch (IOException ex) {
                            ex.printStackTrace();
                          }
                    } else {
                        mensajeError("Primero tiene que inscribirse a un item");
                    }
                }
            });
            btnApuestaC2.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (!ApuestaC2.getText().isEmpty()) {
                        try {
                            double value = Double.parseDouble(ApuestaC2.getText());
                            if (!mapa.get(chCliente2.getValue()).modificarPrecio(value)) {
                                logC2.appendText("el valor no es superior al aceptado\n");
                            } else {
                                logC1.appendText("Hora: ["+ getTime(System.currentTimeMillis())+"] "+cliente2.getName() + " ha incrementado el precio a: "
                                        + mapa.get(chCliente2.getValue()).getPrecioBase() + "\n");
                                logC2.appendText("Hora: ["+ getTime(System.currentTimeMillis())+"] "+cliente2.getName() + " ha incrementado el precio a: "
                                        + mapa.get(chCliente2.getValue()).getPrecioBase() + "\n");
                                logC3.appendText("Hora: ["+ getTime(System.currentTimeMillis())+"] "+cliente2.getName() + " ha incrementado el precio a: "
                                        + mapa.get(chCliente2.getValue()).getPrecioBase() + "\n");
                                        try {
                                            BufferedWriter writer = new BufferedWriter(new FileWriter("client2.txt", true));
                                            writer.write(cliente1.getName() + " ha incrementado el precio a: "
                                            + mapa.get(chCliente1.getValue()).getPrecioBase() + " a las " + getTime(System.currentTimeMillis()) + "\n");
                                            writer.close();
                                          } catch (IOException ex) {
                                            ex.printStackTrace();
                                          }
                                          try {
                                            BufferedWriter writer = new BufferedWriter(new FileWriter("manager.txt", true));
                                            writer.write(cliente1.getName() + " ha incrementado el precio a: "
                                            + mapa.get(chCliente1.getValue()).getPrecioBase() + " a las " + getTime(System.currentTimeMillis()) + "\n");
                                            writer.close();
                                          } catch (IOException ex) {
                                            ex.printStackTrace();
                                          }
                                winner = cliente2.getName();
                            }
                        } catch (Exception e) {
                            // TODO: handle exception
                        }

                    }
                }
            });
        }
    }

    @FXML
    void getClient3() {
        if (!txtName3.getText().isEmpty()) {
            name3.setVisible(true);
            cliente3 = new Cliente(txtName3.getText());
            name3.setText(txtName3.getText());
            txtName3.setVisible(false);
            btnName3.setVisible(false);
            chCliente3.setVisible(true);
            btnIngresarC3.setVisible(true);
            btnItem3.setVisible(true);
            btnItem3.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        if (chCliente3.getValue().equals(chBox.getValue())) {
                            
    
                            if (!(chCliente3.getSelectionModel().getSelectedItem() == null)) {
                                cliente3.setInscritoItem(true);
                                mensaje("Se ha inscrito al item");
                                try {
                                    BufferedWriter writer = new BufferedWriter(new FileWriter("client3.txt", true));
                                    writer.write("Se ha inscrito al item " + chBox.getValue() + " a las " + getTime(System.currentTimeMillis()) + "\n");
                                    writer.close();
                                  } catch (IOException ex) {
                                    ex.printStackTrace();
                                  }
                            } else {
                                mensajeError("No se ha ingresado subasta");
                            }
                        } else {
                            mensajeError("El item a suscribir debe coincidir con el que se va a subastar");
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                    }  
                }
            });
            btnIngresarC3.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (cliente3.getInscritoItem()) {
                        cliente3.setInscritoSubasta(true);
                        mensaje("Se ha inscrito a la subasta");
                        try {
                            BufferedWriter writer = new BufferedWriter(new FileWriter("client3.txt", true));
                            writer.write("Se ha inscrito a la subasta del item " + chBox.getValue() + " a las " + getTime(System.currentTimeMillis()) + "\n");
                            writer.close();
                          } catch (IOException ex) {
                            ex.printStackTrace();
                          }
                    } else {
                        mensaje("Primero tiene que inscribirse a un item");
                    }
                }
            });
            btnApuestaC3.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (!ApuestaC3.getText().isEmpty()) {
                        try {
                            double value = Double.parseDouble(ApuestaC3.getText());
                            if (!mapa.get(chCliente3.getValue()).modificarPrecio(value)) {
                                logC3.appendText("el valor no es superior al aceptado\n");
                            } else { 
                                logC1.appendText("Hora: ["+ getTime(System.currentTimeMillis())+"] "+cliente3.getName() + " ha incrementado el precio a: "
                                        + mapa.get(chCliente3.getValue()).getPrecioBase() + "\n");
                                logC2.appendText("Hora: ["+ getTime(System.currentTimeMillis())+"] "+cliente3.getName() + " ha incrementado el precio a: "
                                        + mapa.get(chCliente3.getValue()).getPrecioBase() + "\n");
                                logC3.appendText("Hora: ["+ getTime(System.currentTimeMillis())+"] "+cliente3.getName() + " ha incrementado el precio a: "
                                        + mapa.get(chCliente3.getValue()).getPrecioBase() + "\n");
                                        try {
                                            BufferedWriter writer = new BufferedWriter(new FileWriter("client3.txt", true));
                                            writer.write(cliente1.getName() + " ha incrementado el precio a: "
                                            + mapa.get(chCliente1.getValue()).getPrecioBase() + " a las " + getTime(System.currentTimeMillis()) + "\n");
                                            writer.close();
                                          } catch (IOException ex) {
                                            ex.printStackTrace();
                                          }
                                          try {
                                            BufferedWriter writer = new BufferedWriter(new FileWriter("manager.txt", true));
                                            writer.write(cliente1.getName() + " ha incrementado el precio a: "
                                            + mapa.get(chCliente1.getValue()).getPrecioBase() + " a las " + getTime(System.currentTimeMillis()) + "\n");
                                            writer.close();
                                          } catch (IOException ex) {
                                            ex.printStackTrace();
                                          }
                                winner = cliente3.getName();
                                
                                // logC1.setText("Precio a Competir: " +
                                // mapa.get(chCliente3.getValue()).getPrecioBase());
                            }
                        } catch (Exception e) {
                            // TODO: handle exception
                        }

                    }
                }
            });
        }
    }
}
