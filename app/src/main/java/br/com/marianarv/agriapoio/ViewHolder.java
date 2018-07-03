package br.com.marianarv.agriapoio;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ViewHolder extends RecyclerView.ViewHolder {
    View mView;
    private ViewHolder.ClickListener mClickListener;

    public ViewHolder(View itemView) {
        super(itemView);

        mView = itemView;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v,getAdapterPosition());
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mClickListener.onItemLongClick(v,getAdapterPosition());
                return true;
            }
        });
    }

    public void setDetails(Context ctx,String title,String image,
                           String data,String hour,String location,String description ){
        TextView titleView=(TextView) mView.findViewById(R.id.textTitleView);
        TextView dataView=(TextView) mView.findViewById(R.id.txtDataView);
        ImageView imageView = (ImageView) mView.findViewById(R.id.imageView);

        titleView.setText(title);
        dataView.setText(data);
        Picasso.get().load(image).into(imageView);
    }

    public interface ClickListener{
        void onItemClick(View view,int position);
        void onItemLongClick(View view,int position);
    }

    public void setOnClickListener(ViewHolder.ClickListener clickListener){
        mClickListener = clickListener;
    }
}
