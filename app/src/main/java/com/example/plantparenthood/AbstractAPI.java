package com.example.plantparenthood;

import com.android.volley.RequestQueue;

public abstract class AbstractAPI
{
    public abstract void queryAPI(RequestQueue queue, String queryParams, Integer page);
    public abstract void queryImageAPI(RequestQueue queue, Plant plant, AbstractCreatorAdapter plantAdapter, int plantLocation);
}
