package com.pcjinrong.pcjr.model;

import com.pcjinrong.pcjr.bean.BaseBean;
import com.pcjinrong.pcjr.bean.Product;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by Mario on 2016/7/25.
 */
public interface IApiModel {
    Observable<BaseBean<List<Product>>> getIndexProductList();
}