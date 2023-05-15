import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

public class Cliente implements Observer {
    //variables
    private boolean  inscritoSubasta;
    private boolean inscritoItem;
    private String name;
    private CasaSubasta auction;
    //constructor
    public Cliente(String name, CasaSubasta auction){
        inscritoSubasta = false;
        inscritoItem = false;
        this.name = name;
        this.auction = auction;
        auction.registerObserver(this);
    }

    public void setInscritoSubasta(boolean inscritoSubasta){
        if(!inscritoItem){
            inscritoSubasta = false;
        }else{
            this.inscritoSubasta = inscritoSubasta;
        }
    }
    public void setInscritoItem(boolean inscritoItem){
        this.inscritoItem = inscritoItem;
    }
    public void setName(String name){
        this.name = name;
    }

    public boolean getInscritoSubasta(){
        return inscritoSubasta;
    }
    public boolean getInscritoItem(){
        return inscritoItem;//si es true, puede ver pero no pagar
    }
    public String getName(){
        return name;
    }
    public void restore(){
        inscritoSubasta = false;
        inscritoItem = false;
    }

    public String getTime(long currTime){
        long currentTimeMillis = currTime;
        Date currentDate = new Date(currentTimeMillis);
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String formattedTime = timeFormat.format(currentDate);
        return formattedTime;

    }
    
    @Override
    public void update(Observable o, Object arg) {
            CasaSubasta auction = (CasaSubasta) o;
            auction.setCurrentBet(auction.getCurrentBidder() + " ha incrementado el precio a: "
            + auction.getCurrentBid() + " a las " + getTime(System.currentTimeMillis()) + "\n");
        
    }
}
