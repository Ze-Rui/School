package com.example.njpischoolapp.bean;

public class LifeguideObject {
//                "id": 2,
//                        "title": "生活资讯",
//                        "content": "多读书多看报",
//                        "kind": "生活",
//                        "pubman": "王汪望",
//                        "pubtime": 1561104827487
    String id;
    String title;
    String content;
    String kind;
    String pubman;
    String pubtime;
    String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getPubman() {
        return pubman;
    }

    public void setPubman(String pubman) {
        this.pubman = pubman;
    }

    public String getPubtime() {
        return pubtime;
    }

    public void setPubtime(String pubtime) {
        this.pubtime = pubtime;
    }
}
