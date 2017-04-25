package com.tgb.media.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.tgb.media.database.PersonModel;
import com.tgb.media.helper.PersonView;

import java.util.List;



public class CastAdapter extends RecyclerView.Adapter<CastAdapter.ViewHolder> {
    private List<PersonModel> personsList;
    private Activity mContext;

    public class ViewHolder extends RecyclerView.ViewHolder{

        private PersonView personView;

        public ViewHolder(View itemView) {
            super(itemView);

            this.personView = (PersonView) itemView;
        }

        public PersonView getPersonView(){
            return personView;
        }
    }

    public CastAdapter(Activity context, List<PersonModel> personsList) {
        this.mContext = context;
        this.personsList = personsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(
                new PersonView(mContext)
        );
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final PersonModel person = personsList.get(position);

        holder.getPersonView().setProfilePhoto(person.getPhoto());
        holder.getPersonView().setName(person.getName());
    }

    @Override
    public int getItemCount() {
        return personsList.size();
    }

}
