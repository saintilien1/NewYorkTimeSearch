package fr.emmanuelroodlyyahoo.nyts.model;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import fr.emmanuelroodlyyahoo.nyts.R;


import static fr.emmanuelroodlyyahoo.nyts.R.id.etDate;
import static fr.emmanuelroodlyyahoo.nyts.R.id.myDate;

/**
 * Created by Emmanuel Roodly on 29/07/2017.
 */

public class DatePicker extends DialogFragment {
    android.widget.DatePicker myDate;
    public Date selectedDate;


    public MonConnecteur monConnecteur; //Interface gerant les transfert de donnees | Fragment-Activite-Fragment
    static int year;
    static int month;
    static int day;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.date_picker, null);
        getDialog().setTitle("Pick a date");
        myDate = (android.widget.DatePicker) root.findViewById(R.id.myDate); //Castre de l'objet DatePicker

        Calendar calendar = Calendar.getInstance(); //Instanciation d'un object de la Classe Calendar afin de recevoir la date du systeme
        calendar.setTimeInMillis(System.currentTimeMillis());//generer la date actuel a partir du systeme android
        myDate.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new android.widget.DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(android.widget.DatePicker datePicker, int i, int i1, int i2) {
                //Toast.makeText(getContext(), "year: " +i+ " | month: " +i1+ "| day: " +i2, Toast.LENGTH_SHORT).show();
                year = i;
                month = i1;
                day = i2;
                MyDialog dialog = (MyDialog) getTargetFragment();//Instanciation du Fragment parent
                dialog.getValueFromChild(day, month, year); //Transfert des donnees au fragment parent
                dismiss();
            }
        });


        return root;
    }


    //methode de liaison entre fragment et activite
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof MonConnecteur){
            monConnecteur = (MonConnecteur) activity; // appel de la classe interface MonConnecteur: Transfert de donnees Fragment/Activite
        }
    }

    //methode le liaison de deux fragments
    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
        if(childFragment instanceof MonConnecteur){
            monConnecteur = (MonConnecteur) childFragment; // Appel de la classe interface MonConnecteur responsable du transfert des donnees Fragment/Fragment
        }
    }
    /*
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
        if(EditorInfo.IME_ACTION_DONE == actionId){
            EditDateDialogueListener listener = (EditDateDialogueListener) getActivity();
            listener.onFinishedEditDate(String.valueOf(year) + "/" + String.valueOf(month) + "/" + String.valueOf(day) );
            return true;
        }
        return false;
    }*/
}
