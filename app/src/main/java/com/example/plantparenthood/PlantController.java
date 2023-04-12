package com.example.plantparenthood;

public class PlantController
{
    private PlantCreator plantCreator;
    private Perenual perenual;
    private PlantSearcher plantSearcher;

    public PlantController(PlantCreator plantCreator, Perenual perenual, PlantSearcher plantSearcher)
    {
        this.plantCreator = plantCreator;
        this.perenual = perenual;
        this.plantSearcher = plantSearcher;
    }


}
