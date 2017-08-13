package software.pipas.oprecox.modules.message;

public enum MessageType
{
    ANNOUNCE("ANNOUNCE");

    private final String message;

    private MessageType(String operation)
    {
        this.message = operation;
    }

    @Override
    public String toString()
    {
        return message;
    }

}
