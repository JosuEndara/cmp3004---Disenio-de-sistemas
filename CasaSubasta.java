import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class CasaSubasta extends Observable {
    private int currentBid;
    private String currentBidder;
    private List<Observer> observers;
    private String currentbet;

    public CasaSubasta(){
        this.currentBid = 0;
        this.currentBidder = "No one";
        this.observers = new ArrayList<>();
    }
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }
    
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    
    public void setCurrentBid(int bid, String bidder) {
        if (bid > currentBid) {
            currentBid = bid;
            currentBidder = bidder;
            setChanged();
            notifiObservers();
        }
    }
    public void notifiObservers() {
        for (Observer observer : observers) {
            observer.update(this, observer);
        }
    }
    
    public int getCurrentBid() {
        return currentBid;
    }
    
    public String getCurrentBidder() {
        return currentBidder;
    }
    public void setCurrentBet(String bet){
        currentbet=bet;
    }
    public String getCurrentBet(){
        return currentbet;

    }
    public void restoreObservers(){
        for(Observer a:observers){
             Cliente nuevo = (Cliente) a;
             nuevo.restore();
        }

    }
    public ArrayList<Cliente> getObservers() {
        ArrayList<Cliente>Lista_nueva= new ArrayList<>();
        for(Observer a:observers){
            Cliente nuevo = (Cliente) a;
            Lista_nueva.add(nuevo);
       }
       return Lista_nueva;
    }
    public void resetBid(){
        currentBid=0;
        currentBidder="";
        currentbet = "";

    }
}
