class Subasta {
    //variables
    private String nombre;
    private double precioBase;
    private int tiempo;
    //constructor
    public Subasta(String nombre, double precioBase, int tiempo){
        this.nombre = nombre;
        this.precioBase = precioBase;
        this.tiempo = tiempo;        
    }
    //setters
    public void setNombre(String nombre){
        this.nombre = nombre;
    }
    public void setPrecioBase(double precioBase){
        this.precioBase = precioBase;
    }
    public void setTiempo(int tiempo){
        this.tiempo = tiempo;
    }
    //getters
    public String getNombre(){
        return nombre;
    }
    public double getPrecioBase(){
        return precioBase;
    }
    public int getTiempo(){
        return tiempo;
    }
    public boolean modificarPrecio(double precioBase){
        //si el usuario ingreso un numero mayor al base, retorna true.
        if(this.precioBase< precioBase){
            this.precioBase = precioBase;
            return true;
        } else{
            return false;
        }
    }
    //toString
    @Override
    public String toString(){
        return "Nombre del item: " + getNombre() + ", PrecioItem: " + getPrecioBase() + ", Tiempo de subasta: " + getTiempo();
    }
}