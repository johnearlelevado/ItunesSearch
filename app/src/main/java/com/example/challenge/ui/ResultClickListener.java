package com.example.challenge.ui;

import com.example.challenge.api.itunes.dto.ResultModel;
import com.example.challenge.room.entities.ItunesResults;

public interface ResultClickListener {
    void onResultItemClick(ItunesResults result);
}
