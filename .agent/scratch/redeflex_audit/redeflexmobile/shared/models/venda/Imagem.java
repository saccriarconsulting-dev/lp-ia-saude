package com.axys.redeflexmobile.shared.models.venda;

import android.net.Uri;

public class Imagem {
    private String uri;
    private String nome;
    private String path;
    private boolean fotoTirada;

    public Imagem() {
    }

    public Imagem(String uri, String nome, String path, boolean fotoTirada) {
        this.uri = uri;
        this.nome = nome;
        this.path = path;
        this.fotoTirada = fotoTirada;
    }

    public Uri getUri() {
        return Uri.parse(uri);
    }

    public void setUri(Uri uri) {
        this.uri = uri.toString();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean fotoTirada() {
        return fotoTirada;
    }

    public void setFotoTirada(boolean fotoTirada) {
        this.fotoTirada = fotoTirada;
    }
}
