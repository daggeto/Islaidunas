package com.islaidunas.services.impl;

import android.os.AsyncTask;

import retrofit.RestAdapter;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by daggreto on 2014.08.12.
 */
public class RetrofitClient {

    private static final String API_URL = "https://api.github.com";

    interface GitHub {
        @GET("/repos/{owner}/{repo}/contributors")
        Response contributors(
                @Path("owner") String owner,
                @Path("repo") String repo
        );
    }

    public void getContributor(){

        RetroAsyncTask asyncTask = new RetroAsyncTask();
        asyncTask.execute(API_URL);

    }

    private class RetroAsyncTask extends AsyncTask<String, Integer, Response>{

        @Override
        protected Response doInBackground(String... params) {
            RestAdapter adapter = new RestAdapter.Builder()
                    .setEndpoint(params[0])
                    .build();

            GitHub github = adapter.create(GitHub.class);

            return github.contributors("square", "retrofit");
        }

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
        }
    }
}
