package com.example.utente.Client;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
/**
 * Classe per la visualizzazione di un popup con un messaggio.
 * @author Lorenzo
 */
@SuppressLint("ValidFragment")
public class PopClass extends AppCompatDialogFragment {
    /**
     * Messaggio di errore che verrà visualizzato.
     */
    private String error;
    /**
     * Costruttore di classe che inizializza il valore del messaggio da visualizzare.
     * @param errorText Messaggio che si vuole visualizzare.
     */
    @SuppressLint("ValidFragment")
    public PopClass(String errorText){
        error=errorText;
    }

    /**
     * Metodo che modella la visualizzazione del popup sullo schermo.
     * @param SavedInstanceState Attributo che contiene lo stato dell'applicazione.
     */
    @Override
    public Dialog onCreateDialog(Bundle SavedInstanceState){
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("Error")
                .setMessage(error)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //todo
                    }
                });
        return builder.create();
    }
}
