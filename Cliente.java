public class Cliente {
    //variables
    public boolean  inscritoSubasta;
    public boolean inscritoItem;
    public String name;
    //constructor
    public Cliente(String name){
        inscritoSubasta = false;
        inscritoItem = false;
        this.name = name;
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
}
