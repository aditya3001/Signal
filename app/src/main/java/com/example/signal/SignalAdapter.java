package com.example.signal;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SignalAdapter extends RecyclerView.Adapter<SignalAdapter.SignalViewHolder> {
    private Cursor mCursor;
    private Context mContext;
    public SignalAdapter(Context context, Cursor cursor){
        mContext = context;
        mCursor = cursor;
    }
    public class SignalViewHolder extends RecyclerView.ViewHolder{

        public TextView companyName;
        public TextView target;
        public TextView stopLoss;
        public SignalViewHolder(@NonNull View itemView) {
            super(itemView);
            companyName = (TextView) itemView.findViewById(R.id.textview_name);
            target = (TextView)itemView.findViewById(R.id.textview_target);
            stopLoss = (TextView)itemView.findViewById(R.id.textview_stoploss);

        }
    }

    @NonNull
    @Override
    public SignalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.signal_item,parent,false);
        return new SignalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SignalViewHolder holder, int position) {
        if(!mCursor.moveToPosition(position)){
            return;
        }
        String company = mCursor.getString(mCursor.getColumnIndex(MyDataBase.Col_2));
        String target = mCursor.getString(mCursor.getColumnIndex(MyDataBase.Col_3));
        String stopLoss = mCursor.getString(mCursor.getColumnIndex(MyDataBase.Col_4));
        long id = mCursor.getLong(mCursor.getColumnIndex(MyDataBase.Col_1));

        holder.companyName.setText(company);
        holder.target.setText(target);
        holder.stopLoss.setText(stopLoss);
        holder.itemView.setTag(id);
    }

    @Override
    public int getItemCount() {
        if(mCursor!=null)
            return mCursor.getCount();
        return 0;
    }
    public void swapCursor(Cursor newCursor){
        if(mCursor!=null){
            mCursor.close();
        }
        mCursor = newCursor;
        if(newCursor!=null){
            notifyDataSetChanged();
        }
    }
}