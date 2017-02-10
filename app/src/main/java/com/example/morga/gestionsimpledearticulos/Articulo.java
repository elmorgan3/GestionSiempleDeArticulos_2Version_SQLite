package com.example.morga.gestionsimpledearticulos;

/**
 * Created by morga on 19/01/2017.
 */

public class Articulo
{
    // Declaro los campos que tendra la base de datos
    private int id;
    private String codigo;
    private String descripcion;
    private float pvp;
    private float estoque;


    public Articulo() {}

    //////////¿¿¿Hace falta inicializar el codigo???
    public Articulo (String codigo, String descripcion, float pvp, float estoque)
    {
        super();

        this.codigo=codigo;
        this.descripcion=descripcion;
        this.pvp=pvp;
        this.estoque=estoque;
    }


    // Geter y seters de id
    public int getId() { return id; }
    public void setId(int id) { this.id=id; }

    // Geter y seters de CODIGO
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo=codigo; }

    // Geter y seters de DESCRIPCION
    public String getDescripcion(){ return descripcion; }
    public void setDescripcion(String descripcion){ this.descripcion=descripcion; }

    // Geter y seters de PVP
    public float getPvp(){ return pvp; }
    public void setPvp(float pvp){ this.pvp=pvp; }

    // Geter y seters de ESTOQUE
    public float getEstoque(){ return estoque; }
    public void setEstoque(float estoque){ this.estoque=estoque; }


    @Override
    public String toString() {
        return "Articulo [codigo=" + codigo + ", descripcion=" + descripcion + ", pvp=" + pvp + ", estoque=" + estoque + "]";
    }
}
