package com.example.elvis.carleaseapp;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

/**
 * Created by LucyZhao on 2017/10/16.
 */

public class BackEndTask extends AsyncTask<Void, Void, Boolean> {
    private PostListAdapter postListAdapter;
    private List<Post> postList;
    private final int start_num;
    private final int end_num;

    BackEndTask(Context main, int start_num, int end_num) {
        this.start_num = start_num;
        this.end_num = end_num;
        this.postListAdapter = ((MainActivity) main).postListAdapter;
        this.postList = ((MainActivity) main).postList;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        List<Post> newList = BackEnd.getPosts(start_num, end_num);
        this.postList.addAll(newList);
        //todo return false for database error
        return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if(result) {
            postListAdapter.notifyDataSetChanged();
        }
    }
}
