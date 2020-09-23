package personal.popy.application.lifecycle;

public interface EntrySet {
    int getState();

    void setState(int newState);

    <T> T getAttachment(AttachmentKey<T> key);

    <T> void addAttachment(AttachmentKey<T> key, T attachment);
}
