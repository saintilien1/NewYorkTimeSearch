package fr.emmanuelroodlyyahoo.nyts.model;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fr.emmanuelroodlyyahoo.nyts.R;
import fr.emmanuelroodlyyahoo.nyts.activities.SearchActivity;

/**
 * Created by Emmanuel Roodly on 28/07/2017.
 */

public class MyDialog extends DialogFragment implements EditDateDialogListener, MonConnecteur {
    DatePicker d; // objet instancie du fragment DatePicker
    FragmentManager fh;

    public void setEtDate(String i) {
        this.etDate.setText(i);
    }

    static int jours;
    static int mois;
    static int annee;

    EditText etDate;
    Spinner spFilter;
    CheckBox cbNone; // element visuel checkBox Arts
    CheckBox cbForeign; // element visuel Checkbox Sport
    CheckBox cbOpEd;//element visuel CheckBox Style
    CheckBox cbNational;
    Button btnSave; //boutton Save
    Button btnClose;
    List<String> spinValues; // variable qui va contenir les valeurs du spinner
    ArrayAdapter<String> spinnerAdapter; //adapter qui lie le spinner a la liste
    private SharedPreferences prefs;
    public String prefValue = "Newest"; // varaiable contenant la valeur par defaut du spinner
    int id = 0;

    //Creation du fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_layout, null);
        int width = getResources().getDimensionPixelSize(R.dimen.dialog_width);
        int height = getResources().getDimensionPixelSize(R.dimen.dialog_height);
        getDialog().getWindow().setLayout(width, height); //redefinition des dimensions du fragment
        getDialog().setTitle("Settings");
        d = new DatePicker(); // initialisation d'un fragment DatePicker
        fh = getFragmentManager();
        etDate = (EditText) rootView.findViewById(R.id.etDate);
        spFilter = (Spinner) rootView.findViewById(R.id.spFilter);
        cbNone = (CheckBox) rootView.findViewById(R.id.cbNone);
        cbForeign = (CheckBox) rootView.findViewById(R.id.cbForeign);
        cbOpEd = (CheckBox) rootView.findViewById(R.id.cbOpEd);
        cbNational = (CheckBox) rootView.findViewById(R.id.cbNational);
        btnSave = (Button) rootView.findViewById(R.id.btnSave);
        btnClose = (Button) rootView.findViewById(R.id.btnClose);
        spinValues = new ArrayList<>(); //List contenant les valeurs a afficher dans le spinner
        spinValues.add(0, "newest");
        spinValues.add(1, "oldest");
        spinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, spinValues); // adapter qui va lier le spinner a la list
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFilter.setAdapter(spinnerAdapter);//liaison du spinner a la liste

        spFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getContext(),spFilter.getSelectedItem().toString(), Toast.LENGTH_LONG).show();//Test  valeur Spinner
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ouverture d'un fragment DatePicker avec onClick pour recevoir la date choisit par l'utilisateur via un calendrier
                d.setTargetFragment(MyDialog.this, 300);
                d.show(fh, "Date");
                etDate.requestFocus();
            }
        });


        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getActivity().getValuesFromFragment();
                //Castre de l'activite parent qui va recevoir des donnees du fragment
                SearchActivity searchActivity = (SearchActivity) getActivity();
                //transfert des donnees a l'activite parent via la methode getValuesFromFragment

                searchActivity.getValuesFromFragment(jours, mois, annee, spFilter.getSelectedItem().toString(), cbNone.isChecked(), cbForeign.isChecked(), cbNational.isChecked());
                dismiss();

            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }


    //methode recevant les donnees du fragment DatePicker
    public void getValueFromChild( int j, int m, int y) {
        jours = j;
        mois = m;
        annee = y;
        etDate.setText(j+"/"+m+"/"+y);

    }
}
