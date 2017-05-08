package com.example.misha.musicapp.Classes;


import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.misha.musicapp.R;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;



/**
 * Created by Misha on 25.04.2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private ArrayList<Music> mDataset;
    private static MediaPlayer mp;
    private static int previousPosition;
    private static Handler handler;
    private static int countProgress;



    public RecyclerAdapter(ArrayList<Music> dataset) {
        mDataset = dataset;
        mp = new MediaPlayer();
        previousPosition = 0;
        handler = new Handler();
        countProgress = 0;

    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView musicName;
        public ImageView playButton;
        public ArrayList<Music> dataset;
        public ProgressBar progress;

        public ViewHolder(View v, ArrayList<Music> ds) {
            super(v);
            dataset = ds;
            musicName = (TextView) v.findViewById(R.id.music_name);
            playButton = (ImageView) v.findViewById(R.id.play_button);
            progress = (ProgressBar) v.findViewById(R.id.music_progress);
            playButton.setOnClickListener(this);
            mp.setOnCompletionListener (new MediaPlayer.OnCompletionListener () {
                @Override
                public void onCompletion (MediaPlayer mediaplayer) {
                    mp.seekTo(0);
                    mp.pause();
                }
            });
        }

        @Override
        public void onClick(View v){
            final ProgressBar itemProgress = this.progress;
            if(v instanceof ImageView){
                play();
                startProgress(itemProgress);
            }




                }

            public void play(){
                if (mp != null && mp.isPlaying()) {
                    mp.seekTo(0);
                    mp.pause();

                    /* mp.stop();
                    mp.reset();
                    mp = dataset.get(previousPosition).getAudio(); */
                }

                mp = dataset.get(this.getAdapterPosition()).getAudio();
                // previousPosition = this.getAdapterPosition();
                try {
                    mp.prepare();

                }catch (Exception e){}
                mp.start();
            }

            public void startProgress(final ProgressBar itemProgress){
                final ProgressBar bar = itemProgress;
                final Runnable onStart = new Runnable() {
                    @Override
                    public void run() {
                        bar.setVisibility(View.VISIBLE);
                        bar.setIndeterminate(false);
                    }
                };

                final Runnable onFinish = new Runnable() {
                    @Override
                    public void run() {
                        bar.setVisibility(View.INVISIBLE);
                        bar.setIndeterminate(true);
                        countProgress = 0;
                    }
                };
                final Runnable onUpdate = new Runnable() {
                    @Override
                    public void run() {
                        Log.e("COUNT: ", String.valueOf(countProgress));
                        bar.setProgress(countProgress);
                    }
                };

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        handler.post(onStart);
                        handler.removeCallbacks(onStart);

                        long partDuration = mp.getDuration()/100;

                        if (partDuration!=-1) {
                            while (countProgress < 100) {
                                countProgress++;

                                handler.post(onUpdate);

                                try {
                                    TimeUnit.MILLISECONDS.sleep(partDuration);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                            }



                        }


                        handler.removeCallbacks(onUpdate);
                        handler.post(onFinish);
                        handler.removeCallbacks(onFinish);




                    }
                });
                thread.start();
            }





            }

       /* static class AsyncProgress extends AsyncTask<ProgressBar,Integer,Void>{

            ProgressBar bar;
            @Override
            protected Void doInBackground(ProgressBar... bar) {
                this.bar = bar[0];
                long partDuration = mp.getDuration()/100;

                if (mp.getDuration()!=-1) {
                    for (int i = 0;i<100;i++){
                        publishProgress(i);
                        try {
                            sleep(partDuration);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }


                return null;
            }
            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                bar.setVisibility(View.VISIBLE);
                bar.setIndeterminate(false);


            }
            @Override
            protected void onPostExecute(Void result){
                super.onPostExecute(result);
                bar.setIndeterminate(true);
                bar.setVisibility(View.INVISIBLE);

            }
            @Override
            protected void onProgressUpdate(Integer... values){
                super.onProgressUpdate(values);
                bar.setProgress(values[0]);

            }
            private void sleep(long sleepDuration) throws InterruptedException {
                TimeUnit.MILLISECONDS.sleep(sleepDuration);
            }

        }*/








    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item, parent, false);

        ViewHolder vh = new ViewHolder(v, mDataset);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.musicName.setText(mDataset.get(position).getName());


    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public ArrayList<Music> getmDataset() {
        return mDataset;
    }


}
