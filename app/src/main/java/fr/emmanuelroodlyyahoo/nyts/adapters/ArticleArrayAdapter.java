package fr.emmanuelroodlyyahoo.nyts.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.squareup.picasso.Picasso;

import java.util.List;

import fr.emmanuelroodlyyahoo.nyts.R;
import fr.emmanuelroodlyyahoo.nyts.model.Article;



/**
 * Created by Emmanuel Roodly on 28/07/2017.
 */

public class ArticleArrayAdapter extends ArrayAdapter<Article> {
    public ArticleArrayAdapter(Context context, List<Article> articles){
        super(context, android.R.layout.simple_list_item_1, articles);
    }


    @Override
    public View getView(int position,  View convertView, ViewGroup parent) {
        Article article = this.getItem(position);

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_article_result, parent, false);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.ivImage);


        imageView.setImageResource(0);

        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        tvTitle.setText(article.getHeadline());
        //TextView tvParagraphe = (TextView) convertView.findViewById(R.id.tvParagraphe);
        //tvParagraphe.setText(article.getParagraph());

        String thumbnail = article.getThumbnail();
        if(!TextUtils.isEmpty(thumbnail)){
            Glide.with(getContext()).load(thumbnail).placeholder(R.drawable.ic_nyt).centerCrop().into(imageView);
        }else{
            Glide.with(getContext()).load(R.mipmap.zebe).centerCrop().into(imageView);
        }

        return convertView;
    }
}
