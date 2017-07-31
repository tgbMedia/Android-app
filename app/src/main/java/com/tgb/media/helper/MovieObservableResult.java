package com.tgb.media.helper;

import android.os.Parcel;
import android.os.Parcelable;

import com.tgb.media.database.MovieOverviewModel;

public class MovieObservableResult implements Parcelable {

    public int position;
    public MovieOverviewModel movie;

    public MovieObservableResult(int position, MovieOverviewModel movie) {
        this.position = position;
        this.movie = movie;
    }

    public int getPosition() {
        return position;
    }

    public MovieOverviewModel getMovie() {
        return movie;
    }

    @Override
    public String toString() {
        return "MovieObservableResult{" +
                "position=" + position +
                ", movie=" + movie +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.position);
        dest.writeParcelable(this.movie, flags);
    }

    protected MovieObservableResult(Parcel in) {
        this.position = in.readInt();
        this.movie = in.readParcelable(MovieOverviewModel.class.getClassLoader());
    }

    public static final Parcelable.Creator<MovieObservableResult> CREATOR = new Parcelable.Creator<MovieObservableResult>() {
        @Override
        public MovieObservableResult createFromParcel(Parcel source) {
            return new MovieObservableResult(source);
        }

        @Override
        public MovieObservableResult[] newArray(int size) {
            return new MovieObservableResult[size];
        }
    };


}
