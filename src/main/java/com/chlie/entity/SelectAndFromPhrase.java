package com.chlie.entity;

import lombok.Data;

import java.util.List;

@Data
public class SelectAndFromPhrase {

    // select 部分
    private List<SelectPhrase> selectPhrases;
    // from 部分
    private List<FromPhrase> fromPhrases;
}
