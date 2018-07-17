package fr.emmanuelroodlyyahoo.nyts.model;

import android.widget.AbsListView;

/**
 * Created by Emmanuel Roodly on 29/07/2017.


public class EndlessScrollListener implements AbsListView.OnScrollListener {

    private int visibles = 5;
    private int currentPage = 0;
    private int previousTotal = 0;
    private boolean loading = true;
    private int startPage = 0;
    public EndlessScrollListener{

    }

    public EndlessScrollListener(int visible){
        this.visibles = visible;
    }
    public EndlessScrollListener(int visibles3, int startpage)
    {
        this.visibles = visibles3;
        this.currentPage = startpage;
        this.startPage = startpage;
    }

    @Override
    public void onScroll(AbsListView absListView, int firstItem, int totalItemVisible, int totalItem) {

    }
}
 */
