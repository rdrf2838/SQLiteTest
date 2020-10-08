package com.example.sqlitetest.utility;

public class Description {
    String _content;
    int _lastmodified;
    public Description() {}
    public Description(String content, int lastmodified) {
        this._content = content;
        this._lastmodified = lastmodified;
    }
    public void setContent(String content) {
        this._content = content;
    }
    public String getContent() {
        return this._content;
    }
    public void setLastmodified(int lastmodified) {
        this._lastmodified = lastmodified;
    }
    public int getLastmodified() {
        return this._lastmodified;
    }
}

