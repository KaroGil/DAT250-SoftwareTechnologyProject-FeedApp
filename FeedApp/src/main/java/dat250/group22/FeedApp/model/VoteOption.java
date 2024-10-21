package dat250.group22.FeedApp.model;
import java.util.UUID;

public class VoteOption  implements java.io.Serializable {
    private String caption;
    private int presentationOrder;
    private UUID id;

    public VoteOption(String caption, int presentationOrder) {
        this.id = UUID.randomUUID();
        this.caption = caption;
        this.presentationOrder = presentationOrder;
    }

    public UUID getId() {
        return id;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public int getPresentationOrder() {
        return presentationOrder;
    }

    public void setPresentationOrder(int presentationOrder) {
        this.presentationOrder = presentationOrder;
    }

}
