package brandon.example.com.despachador;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by brandon on 26/10/17.
 */

public class Microprocesador {

    public static int microCount=0;
    private int id;
    private Proceso procesoActivo;
    private ArrayList<Proceso> listaProcesos = new ArrayList<Proceso>();
    private int tiempoTotal;
    private int tamanoCuantum;
    private int tcc;


    public Microprocesador(int tamanoCuantum) {
        Microprocesador.microCount++;
        id=Microprocesador.microCount;
        this.tamanoCuantum=tamanoCuantum;
        tiempoTotal=0;
    }

    public void agregarProceso(Proceso p){
        if(tiempoTotal==0){
            if(tiempoTotal<p.getTiempoListo()){
                Proceso pMuerto= new Proceso();
                pMuerto.setTiempoInicial(tiempoTotal);
                pMuerto.setTiempoFinal(p.getTiempoListo());
                pMuerto.setTiempoEjecucion(pMuerto.getTiempoFinal()-pMuerto.getTiempoInicial());
                pMuerto.setNombre("-");
                tiempoTotal=p.getTiempoListo();
                p.setTiempoCambiodeContexto(0);
                listaProcesos.add(pMuerto);
            }else{
                tiempoTotal=p.getTiempoListo();
                p.setTiempoCambiodeContexto(0);
            }
        }
        else if(tiempoTotal<p.getTiempoListo()){
            Proceso pMuerto= new Proceso();
            pMuerto.setTiempoInicial(tiempoTotal);
            pMuerto.setTiempoFinal(p.getTiempoListo());
            pMuerto.setTiempoEjecucion(pMuerto.getTiempoFinal()-pMuerto.getTiempoInicial());
            pMuerto.setNombre("-");
            tiempoTotal=p.getTiempoListo();
            p.setTiempoCambiodeContexto(0);
            listaProcesos.add(pMuerto);
        }else{
            p.setTiempoCambiodeContexto(MainActivity.tcc);
        }


        p.setTiempoVencimientoCuantum(vencimiento(p.tiempoEjecucion));
        p.CalcTiempoTotal();

        p.setTiempoInicial(tiempoTotal);
        p.setTiempoFinal(p.getTiempoInicial()+p.getTiempoTotalProceso());
        procesoActivo=p;
        tiempoTotal+=p.getTiempoTotalProceso();
        listaProcesos.add(p);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Proceso getProcesoActivo() {
        return procesoActivo;
    }

    public void setProcesoActivo(Proceso procesoActivo) {
        this.procesoActivo = procesoActivo;
    }

    public ArrayList<Proceso> getListaProcesos() {
        return listaProcesos;
    }

    public void setListaProcesos(ArrayList<Proceso> listaProcesos) {
        this.listaProcesos = listaProcesos;
    }

    public int getTiempoTotal() {
        return tiempoTotal;
    }

    public void setTiempoTotal(int tiempoTotal) {
        this.tiempoTotal = tiempoTotal;
    }

    public int getTamanoCuantum() {
        return tamanoCuantum;
    }

    public void setTamanoCuantum(int tamanoCuantum) {
        this.tamanoCuantum = tamanoCuantum;
    }

    public String toString(){
        String mensaje = "Microprocesador: "+id +"\n";
        mensaje+="PR     TCC    TE     TVC    TB     TT     TI     TF     \n";
        for(Proceso p: listaProcesos){
            mensaje+=concat(p.getNombre())+"";
            mensaje+=concat(p.getTiempoCambiodeContexto()+"");
            mensaje+=concat(p.getTiempoEjecucion()+"");
            mensaje+=concat(p.getTiempoVencimientoCuantum()+"");
            mensaje+=concat(p.getTiempoBloqueo()+"");
            mensaje+=concat(p.getTiempoTotalProceso()+"");
            mensaje+=concat(p.getTiempoInicial()+"");
            mensaje+=concat(p.getTiempoFinal()+"");
            mensaje+="\n";


        }
        mensaje+="\n";
        return mensaje;

    }

    public String concat(String mensaje){
        int n=7-mensaje.length();
        StringBuilder aux= new StringBuilder(mensaje);
        for(int i=0;i<n;i++){
            aux.append(" ");
        }

        return aux.toString();
    }

    // Cuantum = 300
    // 100 -> 0
    // 300 -> 0
    // 395 -> 1
    //

    public int vencimiento(int ej){
        int ven = 0;
        if (ej > tamanoCuantum)
        {
            ven = (int)Math.ceil((ej * 1.0)/tamanoCuantum) - 1;
        }

        return ven * MainActivity.tcc;
    }


}
