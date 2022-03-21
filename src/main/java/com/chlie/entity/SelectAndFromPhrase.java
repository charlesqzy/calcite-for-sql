package com.chlie.entity;

import lombok.Data;

import java.util.List;
/**
 * @author: Charlie Qzy
 * @date: 2022/3/21
 * @description:
 * @version: 1.0.0
 */

@Data
public class SelectAndFromPhrase {

    // select 部分
    private List<SelectPhrase> selectPhrases;
    // from 部分
    private List<FromPhrase> fromPhrases;
}
