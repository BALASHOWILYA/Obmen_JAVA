package com.sad_ballala_projects.obmenknigami_java.adapter;

import com.sad_ballala_projects.obmenknigami_java.NewPost;

import java.util.List;

public interface DataSender {
    public void onDataReceived(List<NewPost> listData);
}
