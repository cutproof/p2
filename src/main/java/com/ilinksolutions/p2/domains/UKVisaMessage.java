package com.ilinksolutions.p2.domains;

import java.io.Serializable;

/**
 *
 */
public class UKVisaMessage
{
    private Serializable id;
    private String summary;
    private String description;

    public UKVisaMessage() {
    }

    public UKVisaMessage(String summary, String description) {
        this(null, summary, description);
    }

    public UKVisaMessage(Serializable id, String summary, String description) {
        this.id = id;
        this.summary = summary;
        this.description = description;
    }


    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Serializable getId() {
        return id;
    }

    public void setId(Serializable id) {
        this.id = id;
    }
}
