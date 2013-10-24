package com.pilasvacias.yaba.modules.emt.pojos;

/**
 * Created by Fede on 24/10/13.
 */


public class BusStop {

    //TODO make class

    public String Node; //Número de parada EMT
    public String PosxNode; //Coordenada UTM X.
    public String PosyNode; //Coordenada UTM Y
    public String Name; //Nombre de la parada

    public String Lines; //Relación de las líneas que, en algún momento del día o tipo de día tienen parada allí. Se compone
                         //de grupos de valores separados por un slash (/). La primera parte es el número de línea EMT. La
                         //segunda parte es el sentido de viaje (1.- Ida 2.- Vuelta)



}
