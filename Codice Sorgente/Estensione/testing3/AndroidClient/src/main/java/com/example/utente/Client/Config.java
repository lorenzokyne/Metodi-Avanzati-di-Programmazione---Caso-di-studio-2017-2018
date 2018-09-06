package com.example.utente.Client;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Classe che modella la visualizzazione di un popup per la modifica dell'indirizzo ip e porta del server a cui ci si vuole connettere.
 * @author Lorenzo
 */
public class Config extends AppCompatDialogFragment {
    /**
     * EditText in cui l'utente scriverà l'ip.
     */
    private EditText ip_text;
    /**
     * EditText in cui l'utente scriverà la porta.
     */
    private EditText port_text;

     /**
     * Metodo che modella la visualizzazione del popup sullo schermo.
     * @param savedInstanceState Attributo che contiene lo stato dell'applicazione.
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.config_layout,null);
        ip_text=(EditText)view.findViewById(R.id.ip_edit) ;
        port_text=(EditText)view.findViewById(R.id.port_edit) ;
        ip_text.setText(MainActivity.ip);
        port_text.setText(Integer.toString(MainActivity.port));
        builder.setView(view)
                .setTitle("Configurazione")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {

                    /**
                    * Metodo che stabilisce cosa fare quando viene premuto il tasto 'cancel', in questo caso non fa niente e il popup si chiude.
                    */
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //TODO
                    }
                })
                .setPositiveButton("apply", new DialogInterface.OnClickListener() {
                    /**
                    * Metodo che stabilisce cosa fare quando viene premuto il tasto 'apply', modifica i campi ip e port di MainActivity.
                    */
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String temp="";
                        temp=ip_text.getText().toString();
                        if(temp.equals("")){
                            mostraPopup("Attenzione, compilare tutti i campi");
                        }else{
                            MainActivity.ip=temp;
                            temp=port_text.getText().toString();
                            if(temp.equals("")){
                                mostraPopup("Attenzione, compilare tutti i campi");
                            }else{
                                MainActivity.port=Integer.parseInt(temp);
                            }
                        }
                    }
                });
        return builder.create();
    }

    /**
     * Mostra un popup con il messaggio di errore specificato.
     * @param errText Il messaggio che si vuole mostrare.
     */
    public void mostraPopup(String errText) {
        PopClass p=new PopClass(errText);
        p.show(getFragmentManager(),"example");
    }
}
