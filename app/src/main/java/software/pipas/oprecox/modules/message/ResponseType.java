package software.pipas.oprecox.modules.message;

/**
 * Created by nuno_ on 06-Sep-17.
 */

public enum ResponseType
{
    TIMEOUT("TIMEOUT"),
    CONNECTED("CONNECTED"),
    CLOSED("CLOSED"),
    INFO("INVITE");

    private final String response;

    private ResponseType(String operation)
    {
        this.response = operation;
    }

    @Override
    public String toString()
    {
        return response;
    }
}
