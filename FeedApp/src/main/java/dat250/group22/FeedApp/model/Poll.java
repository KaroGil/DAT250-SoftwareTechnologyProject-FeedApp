package dat250.group22.FeedApp.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;

public class Poll implements java.io.Serializable {
    private UUID id;
    private UUID creatorUserID;
    private String question;
    private Instant publishedAt;
    private Instant validUntil;
    private ArrayList<VoteOption> options;
    private boolean isPublic;

    public Poll(String question, Instant validUntil, ArrayList<VoteOption> options, boolean isPublic, UUID creatorUserID) {
        this.id = UUID.randomUUID();
        this.creatorUserID = creatorUserID;
        this.question = question;
        this.publishedAt = Instant.now();
        this.validUntil = validUntil;
        this.options = options;
        this.isPublic = isPublic;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getCreatorUserID() {
        return creatorUserID;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Instant getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Instant publishedAt) {
        this.publishedAt = publishedAt;
    }

    public Instant getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(Instant validUntil) {
        this.validUntil = validUntil;
    }

    public ArrayList<VoteOption> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<VoteOption> options) {
        this.options = options;
    }

    public boolean getState() {
        return isPublic;
    }

    public void setstate(boolean isPublic) {
        this.isPublic = isPublic;
    }
}